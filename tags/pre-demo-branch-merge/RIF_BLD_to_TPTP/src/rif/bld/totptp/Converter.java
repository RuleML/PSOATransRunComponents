/*
 * This software is a converter from the BLD dialect of the RIF language
 * to the TPTP format for the first-order predicate logic. 
 *
 * Copyright (C) 2009 Alexandre Riazanov
 *
 * @author Alexandre Riazanov <alexandre.riazanov@gmail.com>
 *
 * @date 17/10/2009
 * 
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Also add information on how to contact you by electronic and paper mail.
 */

package rif.bld.totptp;

import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import tptp_parser.*;

import rif.bld.*;


/** Converts OWL ontologies into the TPTP abstract syntax datastructures. */
public class Converter {

    /** Represents a descriptor of a TPTP symbol, such as a predicate,
     *  function or constant.
     */
    public abstract static class TPTPSymbolDescriptor {

	protected TPTPSymbolDescriptor(boolean predicate,
				       boolean slotted,
				       String name,
				       int arity) {
	    _isPredicate = predicate;
	    _isSlotted = slotted;
	    _name = name;
	    _arity = arity;
	}

	public boolean isPredicate() { return _isPredicate; }

	public boolean isSlotted() { return _isSlotted; }
	
	public String name() { return _name; }

	public int arity() { return _arity; }

	
	public abstract int hashCode();
	
	public abstract boolean equals(Object obj);

	public abstract String toString();


	protected boolean _isPredicate;

	protected boolean _isSlotted;

	protected String _name;
	
	protected int _arity;

    } // class TPTPSymbolDescriptor

    

    /** Represents a descriptor of a TPTP symbol representing a RIF constant 
     *  (but not a nullary function).
     */
    public static class TPTPConstantSymbolDescriptor extends TPTPSymbolDescriptor {
	
	public TPTPConstantSymbolDescriptor(String name) {
	    super(false,false,name,0);
	}

	public int hashCode() { return _name.hashCode(); }
	
	public boolean equals(Object obj) {
	    return (obj instanceof TPTPConstantSymbolDescriptor) &&
		_name.equals(((TPTPConstantSymbolDescriptor)obj)._name);
	}

	public String toString() {
	    return "CONST " + _name;
	}
    } // class TPTPConstantSymbolDescriptor 


    /** Represents a descriptor of a TPTP symbol representing a positional RIF
     *  predicate or function.
     */
    public static class TPTPPositionalSymbolDescriptor extends TPTPSymbolDescriptor {
	
	public TPTPPositionalSymbolDescriptor(boolean predicate,
					      String name,
					      int arity) {
	    super(predicate,false,name,arity);
	}
	
	public int hashCode() {
	    return _name.hashCode() + _arity + ((_isPredicate)? 1 : 0);
	}

	public String toString() {
	    return ((_isPredicate)? "PRED " : "FUNC ") + _name + ":" + _arity;
	}
	
	public boolean equals(Object obj) {

	    return (obj instanceof TPTPPositionalSymbolDescriptor) &&
		(_isPredicate == ((TPTPPositionalSymbolDescriptor)obj)._isPredicate) &&
		_name.equals(((TPTPPositionalSymbolDescriptor)obj)._name) &&
		(_arity == ((TPTPPositionalSymbolDescriptor)obj)._arity);		
	}

    } // class TPTPPositionalSymbolDescriptor 
    

    /** Represents a descriptor of a TPTP symbol representing a slotted RIF
     *  predicate or function.
     */
    public static class TPTPSlottedSymbolDescriptor extends TPTPSymbolDescriptor {
	
	public TPTPSlottedSymbolDescriptor(boolean predicate,
					   String name,
					   Collection<String> slots) {
	    super(predicate,true,name,slots.size());
	    _slots = new TreeSet<String>();
	    _slots.addAll(slots);
	}
	

	public SortedSet<String> slots() {
	    return _slots;
	}

	public int hashCode() {
	    return _name.hashCode() + _arity + ((_isPredicate)? 1 : 0) + 1;
	}

	public String toString() {
	    return ((_isPredicate)? "SLOTTED PRED " : "SLOTTED FUNC ") + _name + ":" + _arity;
	}
	
	public boolean equals(Object obj) {

	    return (obj instanceof TPTPSlottedSymbolDescriptor) &&
		(_isPredicate == ((TPTPSlottedSymbolDescriptor)obj)._isPredicate) &&
		_name.equals(((TPTPSlottedSymbolDescriptor)obj)._name) &&
		_slots.equals(((TPTPSlottedSymbolDescriptor)obj)._slots);		
	}

	private TreeSet<String> _slots;

    } // class TPTPSlottedSymbolDescriptor
    




    /** Various parameters that guide the conversion; some of 
     *  the parameters are modified by the conversion within this object
     *  and can be used afterwards in other objects; eg, mappings
     *  of IRIs to TPTP identifiers are augmented when new IRIs are
     *  encountered.
     */
    public static class Parameters {

	public Parameters() 
	{
	    _IRIsToTPTPSymbols = 
		new HashMap<String,Set<Converter.TPTPSymbolDescriptor>>();
	     _otherReservedSymbols = 
		 new HashSet<Converter.TPTPSymbolDescriptor>();
	     _dataDomainPredicate = null;
	     _equalityPredicate = null;
	     _intLitFunc = null;
	     _stringLitNoLangFunc = null;
	     _stringLitWithLangFunc = null;
	     _typedLitFunc = null;
	}


	/** Mapping that accumulates the correspondence between IRIs 
	 *  and TPTP symbols of different categories and arities.
	 *  @return nonnull
	 */
	public
	    Map<String,Set<Converter.TPTPSymbolDescriptor>> 
	    IRIsToTPTPSymbols() {
	    return _IRIsToTPTPSymbols;
	}
	
	
	/** Set of reserved TPTP identifiers that the converter
	 *  cannot use to map new IRIs.
	 */
	public 
	    Set<Converter.TPTPSymbolDescriptor> otherReservedSymbols() {
	    return _otherReservedSymbols;
	}

	/** Id for the predicate for all the data values; can be null. */
	public String dataDomainPredicate() {
	    return _dataDomainPredicate;
	}
	
	/** Sets the id for the predicate for all the data values. */
	public void setDataDomainPredicate(String pred) {
	    _dataDomainPredicate = pred;
	}
	
	/** Id for the equality predicate. */
	public String equalityPredicate() {
	    return _equalityPredicate;
	}
	
	/** Sets the id for the equality predicate. */
	public void setEqualityPredicate(String pred) {
	    _equalityPredicate = pred;
	}
	

	/** Id for the member predicate. */
	public String memberPredicate() {
	    return _memberPredicate;
	}
	
	/** Sets the id for the member predicate. */
	public void setMemberPredicate(String pred) {
	    _memberPredicate = pred;
	}
	

	/** Id for the subclass predicate. */
	public String subclassPredicate() {
	    return _subclassPredicate;
	}
	
	/** Sets the id for the subclass predicate. */
	public void setSubclassPredicate(String pred) {
	    _subclassPredicate = pred;
	}
	
	/** Id for the method application predicate. */
	public String methodApplicationPredicate() {
	    return _methodApplicationPredicate;
	}
	
	/** Sets the id for the method application predicate. */
	public void setMethodApplicationPredicate(String pred) {
	    _methodApplicationPredicate = pred;
	}
	

	/** Id for the constructor function for integer literals. */
	public String intLitFunc() {
	    return _intLitFunc;
	}

	/** Sets the id for the constructor function for integer literals. */
	public void setIntLitFunc(String func) {
	    _intLitFunc = func;
	}

	/** Id for the constructor function for string literals
	 *  with no language specified.
	 */
	public String stringLitNoLangFunc() {
	    return _stringLitNoLangFunc;
	}

	/** Sets the id for the constructor function for string literals
	 *  with no language specified.
	 */
	public void setStringLitNoLangFunc(String func) {
	    _stringLitNoLangFunc = func;
	}

       

	/** Id for the constructor function for string literals
	 *  with some language specified.
	 */
	public String stringLitWithLangFunc() { 
	    return _stringLitWithLangFunc;
	}
	
	/** Sets the id for the constructor function for string literals
	 *  with some language specified.
	 */
	public void setStringLitWithLangFunc(String func) { 
	    _stringLitWithLangFunc = func;
	}
	

	/** Id for the constructor function for typed data literals. */
	public String typedLitFunc() {
	    return _typedLitFunc;
	}

	/** Sets the id for the constructor function for typed data literals.
	 */
	public void setTypedLitFunc(String func) {
	    _typedLitFunc  = func;
	}


	/** Id for the empty list constant. */
	public String emptyListConstant() {
	    return _emptyListConstant;
	}

	/** Sets the id for the empty list constant. */
	public void setEmptyListConstant(String c) {
	    _emptyListConstant = c;
	}


	/** Id for the constructor function for lists. */
	public String listConsFunc() {
	    return _listConsFunc;
	}

	/** Sets the id for the constructor function for lists. */
	public void setListConsFunc(String func) {
	    _listConsFunc  = func;
	}



	//                 Data:


	/** Mapping that accumulates the correspondence between IRIs 
	 *  and TPTP symbols of different categories and arities.
	 */
	private 
	    Map<String,Set<Converter.TPTPSymbolDescriptor>>
	    _IRIsToTPTPSymbols;

	/** Set of reserved TPTP identifiers that the converter
	 *  cannot use to map new IRIs.
	 */
	private HashSet<Converter.TPTPSymbolDescriptor> _otherReservedSymbols;

	
	/** Id for the predicate for all the data values. */
	private String _dataDomainPredicate;
	
	/** Id for the equality predicate. */
	private String _equalityPredicate;


	/** Id for the member predicate. */
	private String _memberPredicate;

	/** Id for the subclass predicate. */
	private String _subclassPredicate;

	/** Id for the method application predicate. */
	private String _methodApplicationPredicate;

	

	/** Id for the constructor function for integer literals.*/
	private String _intLitFunc;

	/** Id for the constructor function for string literals
	 *  with no language specified.
	 */
	private String _stringLitNoLangFunc;

	/** Id for the constructor function for string literals
	 *  with some language specified.
	 */
	private String _stringLitWithLangFunc;


	/** Id for the constructor function for typed data literals. */
	private String _typedLitFunc;

	/** Id for the empty list constant. */
	private String _emptyListConstant;

	/** Id for the constructor function for lists. */
	private String _listConsFunc;
	
    } // class Parameters 

    
    /** @param param various parameters that guide the conversion,
     *         such as a mapping from IRIs to TPTP symbols
     *  @param generateCNF indicates that the results must be in CNF
     */
    public Converter(Parameters param,boolean generateCNF) {
	assert param != null;
	_parameters = param;
	_generateCNF = generateCNF;
	_syntaxFactory = new SimpleTptpParserOutput();
	_nextVarIndex = 0;
	_nextSkolemSymIndex = 0;
	_nextAxiomIndex = 0;

	_reservedSymbols = new HashSet<TPTPSymbolDescriptor>();
       

	// All symbols to which IRIs are mapped, are reserved:
	for (Set<TPTPSymbolDescriptor> symDescSet :
		 _parameters.IRIsToTPTPSymbols().values())
	    _reservedSymbols.addAll(symDescSet);
	

	// Explicitly reserved symbols:
	_reservedSymbols.addAll(_parameters.otherReservedSymbols());
	
	if (_parameters.dataDomainPredicate() != null)	    
	    _reservedSymbols.add(new TPTPPositionalSymbolDescriptor(true,
								    _parameters.dataDomainPredicate(),
								    1));

	if (_parameters.equalityPredicate() != null)	    
	    _reservedSymbols.add(new TPTPPositionalSymbolDescriptor(true,
								    _parameters.equalityPredicate(),
								    2));

	if (_parameters.stringLitNoLangFunc() != null)	    
	    _reservedSymbols.add(new TPTPPositionalSymbolDescriptor(false,
								    _parameters.stringLitNoLangFunc(),
								    1));

	if (_parameters.stringLitWithLangFunc() != null)	    
	    _reservedSymbols.add(new TPTPPositionalSymbolDescriptor(false,
								    _parameters.stringLitWithLangFunc(),
								    2));

	if (_parameters.typedLitFunc() != null)	    
	    _reservedSymbols.add(new TPTPPositionalSymbolDescriptor(false,
								    _parameters.typedLitFunc(),
								    2));


    } // Converter(Parameters param)


    /** Converts all entries of the RIF BLD document into TPTP annotated 
     *  formulas. 
     */
    public 
	Collection<? extends SimpleTptpParserOutput.TopLevelItem> 
	convert(AbstractSyntax.Document doc)  
	throws Exception 
    {
	
	if (doc.getGroup() != null)
	    return convert(doc.getGroup());

	return
	    new LinkedList<SimpleTptpParserOutput.AnnotatedFormula>();
    }



    /** Converts all entries of the RIF BLD group into TPTP annotated 
     *  formulas. 
     */
    public 
	Collection<SimpleTptpParserOutput.TopLevelItem> 
	convert(AbstractSyntax.Group group)  
	throws Exception 
    {
	
	LinkedList<SimpleTptpParserOutput.TopLevelItem> result =
	    new LinkedList<SimpleTptpParserOutput.TopLevelItem>();

	for (AbstractSyntax.Sentence s : group.getSentences())
	    result.addAll(convert(s));
	
	return result;    
    }
    

    public
	Collection<? extends SimpleTptpParserOutput.TopLevelItem> 
	convertConjecture(AbstractSyntax.Formula form)  
	throws Exception 
    {
	TreeMap<String,String> varRenaming = new TreeMap<String,String>();
	_nextVarIndex = 0;

	TptpParserOutput.FofFormula fof = convert(form,varRenaming);
	
	LinkedList<SimpleTptpParserOutput.AnnotatedFormula> result =
	    new LinkedList<SimpleTptpParserOutput.AnnotatedFormula>();

	SimpleTptpParserOutput.AnnotatedFormula annotatedFOF =
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'dummy conjecture name'",
			       TptpParserOutput.FormulaRole.Conjecture,
			       fof,
			       null,
			       0); 
	    
	result.addLast(annotatedFOF);
	
	if (_generateCNF) return clausify(result);
	
	return result;   

    } // convertConjecture(AbstractSyntax.Formula form,


    /** Converts the RIF BLD sentence into TPTP annotated formulas. 
     */
    public 
	Collection<SimpleTptpParserOutput.TopLevelItem> 
	convert(AbstractSyntax.Sentence sent)  
	throws Exception 
    {

	if (sent.isGroup()) return convert(sent.asGroup());
	
	assert sent.isRule();

	LinkedList<SimpleTptpParserOutput.TopLevelItem> result =
	    new LinkedList<SimpleTptpParserOutput.TopLevelItem>();
	result.addAll(convert(sent.asRule()));
	return result;
    }
    

    /** Converts the RIF BLD rule into a TPTP annotated 
     *  formula. 
     */
    public 
	Collection<? extends SimpleTptpParserOutput.TopLevelItem> 
	convert(AbstractSyntax.Rule rule)  
	throws Exception 
    {
	
	TreeMap<String,String> varRenaming = new TreeMap<String,String>();
	_nextVarIndex = 0;

	TptpParserOutput.FofFormula formula = 
	    convert(rule.getClause(),varRenaming);

	for (AbstractSyntax.Var v : rule.getVariables())
	    {
		String TPTPVar = varRenaming.get(v.getName());
		if (TPTPVar != null)
		    formula = forAll(TPTPVar,formula);
	    }; 
	
	SimpleTptpParserOutput.AnnotatedFormula axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'dummy axiom name'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);

	LinkedList<SimpleTptpParserOutput.AnnotatedFormula> result =
	    new LinkedList<SimpleTptpParserOutput.AnnotatedFormula>();
	
	result.addLast(axiom);

	if (_generateCNF) return clausify(result);

	return result;
    } // convert(AbstractSyntax.Rule rule)  






    /** Axioms that implicitly belong to any rule base translation.
     *  The TPTP identifiers are assigned consistently with all other
     *  TPTP formulas produced by this object.
     */
    public 
	Collection<? extends SimpleTptpParserOutput.TopLevelItem>
	commonAxioms() {
	
	
	LinkedList<SimpleTptpParserOutput.AnnotatedFormula> result =
	    new LinkedList<SimpleTptpParserOutput.AnnotatedFormula>();



	// ! [X,Y]: intLit(X) !=  stringLitNoLang(Y)

	TptpParserOutput.Term varX =
	    _syntaxFactory.createVariableTerm("X");

	TptpParserOutput.Term varY =
	    _syntaxFactory.createVariableTerm("Y");

	LinkedList<TptpParserOutput.Term> args =
	    new LinkedList<TptpParserOutput.Term>();
	args.add(varX);

	TptpParserOutput.Term intLitX = 
	    _syntaxFactory.createPlainTerm(intLitFunc(),args);

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varY);
	TptpParserOutput.Term stringLitNoLangY = 
	    _syntaxFactory.createPlainTerm(stringLitNoLangFunc(),args);
	

	TptpParserOutput.FofFormula formula = 
	    forAll("X","Y",not(equalityApplication(intLitX,stringLitNoLangY)));
	SimpleTptpParserOutput.AnnotatedFormula axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'integer literals are distinct from string-without-language literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);
	

	
	// ! [X,Y]: intLit(X) !=  stringLitWithLang(Y)

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varY);
	TptpParserOutput.Term stringLitWithLangY = 
	    _syntaxFactory.createPlainTerm(stringLitWithLangFunc(),args);

	formula = 
	    forAll("X","Y",not(equalityApplication(intLitX,stringLitWithLangY)));
	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'integer literals are distinct from string-with-language literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);
	


	// ! [X,Y]: intLit(X) !=  typedLit(Y)

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varY);
	TptpParserOutput.Term typedLitY = 
	    _syntaxFactory.createPlainTerm(typedLitFunc(),args);

	formula = 
	    forAll("X","Y",not(equalityApplication(intLitX,typedLitY)));
	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'integer literals are distinct from general typed literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);
	



	// ! [X,Y]: stringLitNoLang(X) !=  stringLitWithLang(Y)

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varX);
	TptpParserOutput.Term stringLitNoLangX = 
	    _syntaxFactory.createPlainTerm(stringLitNoLangFunc(),args);
	
	formula = 
	    forAll("X","Y",not(equalityApplication(stringLitNoLangX,stringLitWithLangY)));
	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'string-without-language literals are distinct from string-with-language literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);



	// ! [X,Y]: stringLitNoLang(X) !=  typedLit(Y)

	formula = 
	    forAll("X","Y",not(equalityApplication(stringLitNoLangX,typedLitY)));
	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'string-without-language literals are distinct from general typed literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);



	// ! [X,Y]: stringLitWithLang(X) !=  typedLit(Y)
	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varX);
	TptpParserOutput.Term stringLitWithLangX = 
	    _syntaxFactory.createPlainTerm(stringLitWithLangFunc(),args);
	
	formula = forAll("X",not(equalityApplication(stringLitWithLangX,typedLitY)));
	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'string-with-language literals are distinct from general typed literals'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);




	// ! [X,Y]: intLit(X) = intLit(Y) => X = Y
	

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varY);

	TptpParserOutput.Term intLitY = 
	    _syntaxFactory.createPlainTerm(intLitFunc(),args);

	formula = 
	    forAll("X",
		   "Y",
		   implies(equalityApplication(intLitX,intLitY),
			   equalityApplication(varX,varY)));

	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'intLit is a constructor'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);
	

	
	// ! [X,Y]: stringLitNoLang(X) = stringLitNoLang(Y) => X = Y

	formula = 
	    forAll("X",
		   "Y",
		   implies(equalityApplication(stringLitNoLangX,stringLitNoLangY),
			   equalityApplication(varX,varY)));

	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'stringLitNoLang is a constructor'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);
       
	

	// ! [X,Y]: stringLitWithLang(X) = stringLitWithLang(Y) => X = Y


	formula = 
	    forAll("X",
		   "Y",
		   implies(equalityApplication(stringLitWithLangX,stringLitWithLangY),
			   equalityApplication(varX,varY)));

	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'stringLitWithLang is a constructor'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);



	// ! [X,Y]: typedLit(X) = typedLit(Y) => X = Y

	args = new LinkedList<TptpParserOutput.Term>();
	args.add(varX);
	TptpParserOutput.Term typedLitX = 
	    _syntaxFactory.createPlainTerm(typedLitFunc(),args);

	formula = 
	    forAll("X",
		   "Y",
		   implies(equalityApplication(typedLitX,typedLitY),
			   equalityApplication(varX,varY)));

	axiom = 
	    (SimpleTptpParserOutput.AnnotatedFormula)
	    _syntaxFactory.
	    createFofAnnotated("'typedLit is a constructor'",
			       TptpParserOutput.FormulaRole.Axiom,
			       formula,
			       null,
			       0);
	result.add(axiom);


	
	if (_generateCNF) return clausify(result);

	return result;

    } // commonAxioms()

    





    

    /** Converts the RIF BLD clause into a TPTP annotated 
     *  formula. 
     */
    private TptpParserOutput.FofFormula convert(AbstractSyntax.Clause cl,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	
	TptpParserOutput.FofFormula result = 
	    convertAsConjunction(cl.getHead(),varRenaming);

	if (cl.getBody() != null) 
	    result = implies(convert(cl.getBody(),varRenaming),result);
	

	return result;
    }
    

    private TptpParserOutput.FofFormula convert(AbstractSyntax.Formula form,
						Map<String,String> varRenaming)  
	throws Exception 
    {

	if (form instanceof AbstractSyntax.And)
	    {
		return convert(form.asAnd(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Or)
	    {
		return convert(form.asOr(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Exists)
	    {
		return convert(form.asExists(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Atomic)
	    {
		return convert(form.asAtomic(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.ExternalFormula)
	    {
		throw new Error("Bad kind of rif.bld.AbstractSyntax.Formula: rif.bld.AbstractSyntax.ExternalFormula is not supported.");
	    }
	else 
	    throw new Error("Bad kind of rif.bld.AbstractSyntax.Formula .");
	
    } // convert(AbstractSyntax.Formula form,..)



    private TptpParserOutput.FofFormula convert(AbstractSyntax.And form,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	return convertAsConjunction(form.getFormulas(),varRenaming);
    }



    private TptpParserOutput.FofFormula convert(AbstractSyntax.Or form,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	Iterator<? extends AbstractSyntax.Formula> iter = 
	    form.getFormulas().iterator();
	assert iter.hasNext();
	
	TptpParserOutput.FofFormula result = convert(iter.next(),varRenaming);
	
	while (iter.hasNext())
	    result = or(result,convert(iter.next(),varRenaming));
	
	return result;
	
    }



    private TptpParserOutput.FofFormula convert(AbstractSyntax.Exists form,
						Map<String,String> varRenaming)  
	throws Exception 
    {

	TptpParserOutput.FofFormula result = 
	    convert(form.getFormula(),varRenaming);
	
	for (AbstractSyntax.Var v : form.getVariables())
	    {
		String TPTPVar = varRenaming.get(v.getName());
		if (TPTPVar != null)
		    result = exist(TPTPVar,result);
	    }; 
	
	return result;
    }


    private TptpParserOutput.FofFormula convert(AbstractSyntax.Atomic form,
						Map<String,String> varRenaming)  
	throws Exception 
    {

	if (form instanceof AbstractSyntax.Atom)
	    {
		return convert(form.asAtom(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Equal)
	    {
		return convert(form.asEqual(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Member)
	    {
		return convert(form.asMember(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Subclass)
	    {
		return convert(form.asSubclass(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.Frame)
	    {
		return convert(form.asFrame(),varRenaming);
	    }
	else
	    throw new Error("Bad kind of rif.bld.AbstractSyntax.Atomic .");

    } // convert(AbstractSyntax.Atomic form,..)



    private TptpParserOutput.FofFormula convert(AbstractSyntax.Atom form,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	if (form instanceof AbstractSyntax.PositionalAtom)
	    {
		return convert(form.asPositionalAtom(),varRenaming);
	    }
	else if (form instanceof AbstractSyntax.SlottedAtom)
	    {
		return convert(form.asSlottedAtom(),varRenaming);
	    }
	else
	    throw new Error("Bad kind of rif.bld.AbstractSyntax.Atom .");
	    
    }


    private 
	TptpParserOutput.FofFormula convert(AbstractSyntax.PositionalAtom form,
					    Map<String,String> varRenaming)  
	throws Exception 
    {

	String pred = convertAsPositionalPredicate(form.getPredicate(),
						   form.getArguments().size());
	
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();
	
	for (AbstractSyntax.Term arg : form.getArguments())
	    args.addLast(convert(arg,varRenaming));

	assert !args.isEmpty(); // no nullary positional predicates

	return atom(pred,args);
    }

    /** p(n1->t1,_,nk->tk) is converted into p(n1,t1,_,nk,tk), assuming
     *  that the names are ordered, ie, n1 < n2 < _ < nk.
     */
    private 
	TptpParserOutput.FofFormula convert(AbstractSyntax.SlottedAtom form,
					    Map<String,String> varRenaming)  
	throws Exception 
    {
	String pred = convertAsSlottedPredicate(form.getPredicate(),
						form.getSlots());
	
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();

	for (AbstractSyntax.ArgumentSlot slot : form.getSlots())
	    args.addLast(convert(slot.getValue(),varRenaming));

	// args may be empty here: we allow nullary slotted predicates
	return atom(pred,args);
    }



    private TptpParserOutput.FofFormula convert(AbstractSyntax.Equal form,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	return equalityApplication(convert(form.getLeft(),varRenaming),
				   convert(form.getRight(),varRenaming));
    }

    private TptpParserOutput.FofFormula convert(AbstractSyntax.Member form,
						Map<String,String> varRenaming)  
	throws Exception 
    {
	return atom(memberPredicate(),
		    convert(form.getInstance(),varRenaming),
		    convert(form.getClassExpr(),varRenaming));
    }

    private TptpParserOutput.FofFormula convert(AbstractSyntax.Subclass form,
						Map<String,String> varRenaming) 
	throws Exception 
    {
	return atom(subclassPredicate(),
		    convert(form.getSubclass(),varRenaming),
		    convert(form.getSuperclass(),varRenaming));
    }

    private TptpParserOutput.FofFormula convert(AbstractSyntax.Frame form,
						Map<String,String> varRenaming) 
	throws Exception 
    {
	TptpParserOutput.Term obj = 
	    convert(form.getObject(),varRenaming);
	if (form.getSlots().isEmpty()) return builtInTrue();

	Iterator<? extends AbstractSyntax.FrameSlot> iter = 
	    form.getSlots().iterator();
	AbstractSyntax.FrameSlot slot = iter.next();

	TptpParserOutput.FofFormula result = 
	    atom(methodApplicationPredicate(),
		 obj,
		 convert(slot.getMethod(),varRenaming),
		 convert(slot.getValue(),varRenaming));
		 
	while (iter.hasNext())
	    {
		slot = iter.next();
		result = and(result,
			     atom(methodApplicationPredicate(),
				  obj,
				  convert(slot.getMethod(),varRenaming),
				  convert(slot.getValue(),varRenaming)));
	    };
	
	return result;
	
    } // convert(AbstractSyntax.Frame form,



    private 
	TptpParserOutput.FofFormula 
	convertAsConjunction(Collection<? extends AbstractSyntax.Formula> subformulas,
			     Map<String,String> varRenaming) 
	throws Exception 
    {
	Iterator<? extends AbstractSyntax.Formula> iter = subformulas.iterator();
	assert iter.hasNext();
	
	TptpParserOutput.FofFormula result = convert(iter.next(),varRenaming);
	
	while (iter.hasNext())
	    result = and(result,convert(iter.next(),varRenaming));
	
	return result;
    }




    private TptpParserOutput.Term convert(AbstractSyntax.Term term,
					  Map<String,String> varRenaming)
	throws Exception  
    {
	
	if (term instanceof AbstractSyntax.Const)
	    {
		return convert(term.asConst(),varRenaming);
	    }
	else if (term instanceof AbstractSyntax.Var)
	    {
		return convert(term.asVar(),varRenaming);
	    }
	else if (term instanceof AbstractSyntax.Expr)
	    {
		return convert(term.asExpr(),varRenaming);
	    }
	else if (term instanceof AbstractSyntax.List)
	    {
		return convert(term.asList(),varRenaming);
	    }
	else if (term instanceof AbstractSyntax.ExternalTerm)
	    {
		throw new Error("Bad kind of rif.bld.AbstractSyntax.Term:  rif.bld.AbstractSyntax.ExternalTerm is not supported.");
	    }
	else 
	    throw new Error("Bad kind of rif.bld.AbstractSyntax.Term .");

    } // convert(AbstractSyntax.Term term,



    private TptpParserOutput.Term convert(AbstractSyntax.Const c,
					  Map<String,String> varRenaming) 
	throws Exception 
    {

	String type = c.getType();

	if (type.equals("http://www.w3.org/2007/rif#iri"))
	    {
		String id = individualConstantForIRI(c.getContent());
		
		// The language tag is ignored even if it is attached.

		return _syntaxFactory.createPlainTerm(id,null);
	    }
	else if (type.equals("http://www.w3.org/2001/XMLSchema#string"))
	    {
		LinkedList<TptpParserOutput.Term> args =
		    new LinkedList<TptpParserOutput.Term>();
		
		TptpParserOutput.Term arg1 =
		    _syntaxFactory.createPlainTerm("\"" + 
						   c.getContent() +
						   "\"" ,
						   null);	
		args.addLast(arg1);

		if (c.getLang() == null)		
		    return
			_syntaxFactory.createPlainTerm(stringLitNoLangFunc(),
						       args);
		
		
		TptpParserOutput.Term arg2 =
		    _syntaxFactory.createPlainTerm("\"" + 
						   c.getLang()+
						   "\"" ,
						   null);
		args.addLast(arg2);

		return 
		    (SimpleTptpParserOutput.Term)
		    _syntaxFactory.createPlainTerm(stringLitWithLangFunc(),
						   args);
	    }
	else if (type.equals("http://www.w3.org/2001/XMLSchema#integer"))
	    {

		LinkedList<TptpParserOutput.Term> args =
		    new LinkedList<TptpParserOutput.Term>();
		
		TptpParserOutput.Term arg1 = 
		    _syntaxFactory.createPlainTerm(c.getContent(),null);

		args.addLast(arg1);
		
		return 
		    (SimpleTptpParserOutput.Term)
		    _syntaxFactory.createPlainTerm(intLitFunc(),
						   args);
	    }
	else // General case of a typed literal:
	    {
		LinkedList<TptpParserOutput.Term> args =
		    new LinkedList<TptpParserOutput.Term>();
		
		TptpParserOutput.Term arg1 = 
		    _syntaxFactory.createPlainTerm(c.getContent(),null);

		args.addLast(arg1);

		TptpParserOutput.Term arg2 =
		    _syntaxFactory.createPlainTerm("\"" + type + "\"",
						   null);

		args.addLast(arg2);
		    
		return 
		    (SimpleTptpParserOutput.Term)
		    _syntaxFactory.createPlainTerm(typedLitFunc(),
						   args);
	    }
	
	
    } // convert(AbstractSyntax.Const term,


    private TptpParserOutput.Term convert(AbstractSyntax.Var term,
					  Map<String,String> varRenaming) {

	String TPTPVar = varRenaming.get(term.getName());

	if (TPTPVar == null)
	    {
		TPTPVar = generateNewVariable("X");

		varRenaming.put(term.getName(),TPTPVar);
	    };

	return _syntaxFactory.createVariableTerm(TPTPVar);
    }


    private TptpParserOutput.Term convert(AbstractSyntax.Expr term,
					  Map<String,String> varRenaming)  
	throws Exception 
    {
	if (term instanceof AbstractSyntax.PositionalExpr)
	    {
		return convert(term.asPositionalExpr(),varRenaming);
	    }
	else if (term instanceof AbstractSyntax.SlottedExpr)
	    {
		return convert(term.asSlottedExpr(),varRenaming);
	    }
	else
	    throw new Error("Bad kind of rif.bld.AbstractSyntax.Expr .");
    }


    private TptpParserOutput.Term convert(AbstractSyntax.PositionalExpr term,
					  Map<String,String> varRenaming)  
	throws Exception 
    {
	
	String func = convertAsPositionalFunc(term.getFunction(),
					      term.getArguments().size());
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();
	
	for (AbstractSyntax.Term arg : term.getArguments())
	    args.addLast(convert(arg,varRenaming));

	return _syntaxFactory.createPlainTerm(func,args);
    }


    /** f(n1->t1,_,nk->tk) is converted into f(t1,_,tk), assuming
     *  that the names are ordered, ie, n1 < n2 < _ < nk.
     */
    private TptpParserOutput.Term convert(AbstractSyntax.SlottedExpr term,
					  Map<String,String> varRenaming)  
	throws Exception 
    {
	
	String func = convertAsSlottedFunc(term.getFunction(),
					   term.getSlots());
	
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();

	for (AbstractSyntax.ArgumentSlot slot : term.getSlots())
	    args.addLast(convert(slot.getValue(),varRenaming));

	if (args.isEmpty()) args = null;
	// because SimpleTptpParserOutput.createPlainTerm() requires this

	return _syntaxFactory.createPlainTerm(func,args);

    } // convert(AbstractSyntax.SlottedExpr term,



    private TptpParserOutput.Term convert(AbstractSyntax.List term,
					  Map<String,String> varRenaming)  
	throws Exception 
    {
	if (term.isEmpty())
	    return _syntaxFactory.createPlainTerm(emptyListConstant(),null);
	
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();
	
	args.addLast(convert(term.getHead(),varRenaming));
	args.addLast(convert(term.getTail(),varRenaming));

	return 
	    _syntaxFactory.createPlainTerm(listConsFunc(),args);
    }


    

    private 
	String convertAsPositionalPredicate(AbstractSyntax.Const pred,int arity) {
	
	assert arity > 0;

	String type = pred.getType();
		
	if (type.equals("http://www.w3.org/2007/rif#iri"))
	    {
		// The language tag is ignored even if it is attached.

		return positionalSymbolForIRI(true,pred.getContent(),arity);
	    }
	else
	    throw new Error("Non-IRI predicate names are not supported.");

    }



    private 
	String 
	convertAsSlottedPredicate(AbstractSyntax.Const pred,
				  Collection<? extends AbstractSyntax.ArgumentSlot> slots) 
    {
	String type = pred.getType();
		
	if (type.equals("http://www.w3.org/2007/rif#iri"))
	    {
		// The language tag is ignored even if it is attached.

		TreeSet<String> slotNames = new TreeSet<String>();
		for (AbstractSyntax.ArgumentSlot slot : slots)
		    slotNames.add(slot.getName());
		
		return slottedSymbolForIRI(true,pred.getContent(),slotNames);
	    }
	else
	    throw new Error("Non-IRI predicate names are not supported.");
    }



    private 
	String convertAsPositionalFunc(AbstractSyntax.Const func,int arity) {

	assert arity > 0;
	
	String type = func.getType();
		
	if (type.equals("http://www.w3.org/2007/rif#iri"))
	    {
		// The language tag is ignored even if it is attached.

		return positionalSymbolForIRI(false,func.getContent(),arity);
	    }
	else
	    throw new Error("Non-IRI function names are not supported.");
    }



    private 
	String 
	convertAsSlottedFunc(AbstractSyntax.Const func,
			     Collection<? extends AbstractSyntax.ArgumentSlot> slots)
    {
	String type = func.getType();
		
	if (type.equals("http://www.w3.org/2007/rif#iri"))
	    {
		// The language tag is ignored even if it is attached.

		TreeSet<String> slotNames = new TreeSet<String>();
		for (AbstractSyntax.ArgumentSlot slot : slots)
		    slotNames.add(slot.getName());
		
		return slottedSymbolForIRI(false,func.getContent(),slotNames);
	    }
	else
	    throw new Error("Non-IRI function names are not supported.");
    }




    private
	LinkedList<SimpleTptpParserOutput.AnnotatedClause> 
	clausify(LinkedList<SimpleTptpParserOutput.AnnotatedFormula> annotatedFormulas) {

	LinkedList<SimpleTptpParserOutput.AnnotatedClause> result = 
	    new LinkedList<SimpleTptpParserOutput.AnnotatedClause>();

	for (SimpleTptpParserOutput.AnnotatedFormula form : annotatedFormulas)
	    result.addAll(clausify(form));
	
	return result;

    } // clausify(LinkedList<SimpleTptpParserOutput.AnnotatedFormula> annotatedFormulas)


    private 
	LinkedList<SimpleTptpParserOutput.AnnotatedClause> 
	clausify(SimpleTptpParserOutput.AnnotatedFormula annotatedFormula) {
	
	LinkedList<SimpleTptpParserOutput.AnnotatedClause> result = 
	    new LinkedList<SimpleTptpParserOutput.AnnotatedClause>();

	if (annotatedFormula.getRole() == TptpParserOutput.FormulaRole.Axiom)
	    {
		LinkedList<LinkedList<TptpParserOutput.Literal>> clauses = 
		    clausify(annotatedFormula.getFormula(),true,new LinkedList<String>());
	
		int n = 0;

		for (LinkedList<TptpParserOutput.Literal> clauseLiterals : clauses)
		    {
		
			TptpParserOutput.CnfFormula clause = 
			    _syntaxFactory.createClause(clauseLiterals);

			String newName = annotatedFormula.getName();
		
			if (clauses.size() > 1)
			    newName = 
				newName.substring(0,newName.length() - 1) +
				" CL " + ++n + "'";

			SimpleTptpParserOutput.AnnotatedClause newAxiom = 
			    (SimpleTptpParserOutput.AnnotatedClause)
			    _syntaxFactory.
			    createCnfAnnotated(newName,
					       TptpParserOutput.FormulaRole.Axiom,
					       clause,
					       null,
					       0);
		
			result.add(newAxiom);
 
		    } // for (SimpleTptpParserOutput.Clause cl : clauses)
	    }
	else
	    {
		assert annotatedFormula.getRole() == 
		    TptpParserOutput.FormulaRole.Conjecture;

		LinkedList<LinkedList<TptpParserOutput.Literal>> clauses = 
		    clausify(annotatedFormula.getFormula(),false,new LinkedList<String>());
	
		int n = 0;

		for (LinkedList<TptpParserOutput.Literal> clauseLiterals : clauses)
		    {
		
			TptpParserOutput.CnfFormula clause = 
			    _syntaxFactory.createClause(clauseLiterals);

			String newName = annotatedFormula.getName();
		
			if (clauses.size() > 1)
			    newName = 
				newName.substring(0,newName.length() - 1) +
				" CL " + ++n + "'";

			SimpleTptpParserOutput.AnnotatedClause newNegatedConjecture = 
			    (SimpleTptpParserOutput.AnnotatedClause)
			    _syntaxFactory.
			    createCnfAnnotated(newName,
					       TptpParserOutput.FormulaRole.NegatedConjecture,
					       clause,
					       null,
					       0);
		
			result.add(newNegatedConjecture);
 
		    } // for (SimpleTptpParserOutput.Clause cl : clauses)
	    }; // if (annotatedFormula.getRole() == TptpParserOutput.FormulaRole.Axiom)

	return result;

    } // clausify(SimpleTptpParserOutput.AnnotatedFormula axiom)


    private 
	LinkedList<LinkedList<TptpParserOutput.Literal>>
	clausify(SimpleTptpParserOutput.Formula formula,
		 boolean positive,
		 LinkedList<String> universalVariables) {


	switch (formula.getKind())
	    {
	    case Atomic:
		{
		    TptpParserOutput.Literal literal =
			_syntaxFactory.
			createLiteral(positive,
				      (SimpleTptpParserOutput.Formula.Atomic)formula);

		    LinkedList<LinkedList<TptpParserOutput.Literal>> 
			result =
			new LinkedList<LinkedList<TptpParserOutput.Literal>>();
		    
		    result.add(new LinkedList<TptpParserOutput.Literal>());
		    result.getFirst().add(literal);
		    
		    return result;

		} // case Atomic:
	

	    case Binary:
		return 
		    clausifyBinary((SimpleTptpParserOutput.Formula.Binary)formula,
				   positive,
				   universalVariables);
				   

	    case Negation:
		return 
		    clausify(((SimpleTptpParserOutput.Formula.Negation)formula).
			     getArgument(),
			     !positive,
			     universalVariables);
		
	    case Quantified:
		return 
		    clausifyQuantified((SimpleTptpParserOutput.Formula.Quantified)formula,
				       positive,
				       universalVariables);
		


	    } // switch (formula.getKind())
	
	assert false;
	
	return null;

    } // clausify(SimpleTptpParserOutput.Formula formula)


    
    private 
	LinkedList<LinkedList<TptpParserOutput.Literal>>
	clausifyBinary(SimpleTptpParserOutput.Formula.Binary formula,
		       boolean positive,
		       LinkedList<String> universalVariables) {

	switch (formula.getConnective())
	    {
	    case And:
		{
		    LinkedList<LinkedList<TptpParserOutput.Literal>> clausifiedLHS =
			clausify(formula.getLhs(),positive,universalVariables);
		    LinkedList<LinkedList<TptpParserOutput.Literal>> clausifiedRHS =
			clausify(formula.getRhs(),positive,universalVariables);
		    
		    if (positive)
			{
			    clausifiedLHS.addAll(clausifiedRHS);
			    return clausifiedLHS;
			}
		    else
			{
			    // "Multiply" clausifiedLHS and clausifiedRHS:

			    LinkedList<LinkedList<TptpParserOutput.Literal>> result =
				new LinkedList<LinkedList<TptpParserOutput.Literal>>();

			    for (LinkedList<TptpParserOutput.Literal> cl1 : 
				     clausifiedLHS)
				for (LinkedList<TptpParserOutput.Literal> cl2 : 
					 clausifiedRHS)
				    {
					LinkedList<TptpParserOutput.Literal> cl = 
					    new LinkedList<TptpParserOutput.Literal>();
					cl.addAll(cl1);
					cl.addAll(cl2);
					result.add(cl);
				    };
			    
			    return result;

			} // if (positive)

		} // case And:

	    case Disequivalence:
		{
		    // Treat it as a negation of equivalence:

		    SimpleTptpParserOutput.Formula equivalence =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getLhs(),
					    TptpParserOutput.BinaryConnective.Equivalence,
					    formula.getRhs());
		    
		    return clausify(equivalence,!positive,universalVariables);

		} // case Disequivalence:


	    case Equivalence:
		{
		    // Treat it as a conjunction of implications:
		    
		    SimpleTptpParserOutput.Formula implication =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getLhs(),
					    TptpParserOutput.BinaryConnective.Implication,
					    formula.getRhs());
		    
		    SimpleTptpParserOutput.Formula reverseImplication =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getRhs(),
					    TptpParserOutput.BinaryConnective.Implication,
					    formula.getLhs());
		    

		    SimpleTptpParserOutput.Formula conjunction =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(implication,
					    TptpParserOutput.BinaryConnective.And,
					    reverseImplication);

		    return clausify(conjunction,positive,universalVariables);

		} // case Equivalence:


	    case Implication:
		{
		    // Treat F => G as ~F \/ G:

		    SimpleTptpParserOutput.Formula negationOfLHS =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.createNegationOf(formula.getLhs());

		    
		    SimpleTptpParserOutput.Formula disjunction =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(negationOfLHS,
					    TptpParserOutput.BinaryConnective.Or,
					    formula.getRhs());
		    

		    return clausify(disjunction,positive,universalVariables);

		} // case Implication:


	    case NotAnd:
		{
		    // Treat it as a negation of conjunction:
		    

		    SimpleTptpParserOutput.Formula conjunction =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getLhs(),
					    TptpParserOutput.BinaryConnective.And,
					    formula.getRhs());

		    return clausify(conjunction,!positive,universalVariables);

		} // case NotAnd:
		

	    case NotOr:
		{
		    // Treat it as a negation of disjunction:
		    

		    SimpleTptpParserOutput.Formula disjunction =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getLhs(),
					    TptpParserOutput.BinaryConnective.Or,
					    formula.getRhs());

		    return clausify(disjunction,!positive,universalVariables);

		} // case NotOr:
		


	    case Or:
		{
		    LinkedList<LinkedList<TptpParserOutput.Literal>> clausifiedLHS =
			clausify(formula.getLhs(),positive,universalVariables);
		    LinkedList<LinkedList<TptpParserOutput.Literal>> clausifiedRHS =
			clausify(formula.getRhs(),positive,universalVariables);
		    
		    if (!positive)
			{
			    // Can be treated as positive conjunction:
			    clausifiedLHS.addAll(clausifiedRHS);
			    return clausifiedLHS;
			}
		    else
			{
			    // "Multiply" clausifiedLHS and clausifiedRHS:

			    LinkedList<LinkedList<TptpParserOutput.Literal>> result =
				new LinkedList<LinkedList<TptpParserOutput.Literal>>();

			    for (LinkedList<TptpParserOutput.Literal> cl1 : 
				     clausifiedLHS)
				for (LinkedList<TptpParserOutput.Literal> cl2 : 
					 clausifiedRHS)
				    {
					LinkedList<TptpParserOutput.Literal> cl = 
					    new LinkedList<TptpParserOutput.Literal>();
					cl.addAll(cl1);
					cl.addAll(cl2);
					result.add(cl);
				    };
			    
			    return result;

			} // if (positive)

		} // case Or:


	    case ReverseImplication:
		{
		    // Treat F <= G as  F \/ ~G:

		    SimpleTptpParserOutput.Formula negationOfRHS =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.createNegationOf(formula.getRhs());

		    
		    SimpleTptpParserOutput.Formula disjunction =
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createBinaryFormula(formula.getLhs(),
					    TptpParserOutput.BinaryConnective.Or,
					    negationOfRHS);
		    

		    return clausify(disjunction,positive,universalVariables);
		    
		} // case ReverseImplication:
		
	    } // switch (formula.getConnective())

	assert false;
	
	return null;

    } // clausifyBinary(SimpleTptpParserOutput.Formula.Binary formula,




    private 
	LinkedList<LinkedList<TptpParserOutput.Literal>>
	clausifyQuantified(SimpleTptpParserOutput.Formula.Quantified formula,
			   boolean positive,
			   LinkedList<String> universalVariables) {

	if ((positive && 
	     formula.getQuantifier() == TptpParserOutput.Quantifier.Exists) ||
	    (!positive && 
	     formula.getQuantifier() == TptpParserOutput.Quantifier.ForAll))
	    {
		// Skolemise: 
		SimpleTptpParserOutput.Term skolemTerm = 
		    skolemTerm(universalVariables);
		
		SimpleTptpParserOutput.Formula newMatrix = 
		    replace((SimpleTptpParserOutput.Formula)formula.getMatrix(),
			    formula.getVariable(),
			    skolemTerm);
		return clausify(newMatrix,positive,universalVariables);
	    };
	
	// Essentially universal quantifier. Just drop it.
	
	assert !universalVariables.contains(formula.getVariable());
	universalVariables.addLast(formula.getVariable());

	LinkedList<LinkedList<TptpParserOutput.Literal>> result =
	    clausify(formula.getMatrix(),positive,universalVariables);

	universalVariables.removeLast();

	return result;

    } // clausifyQuantified(SimpleTptpParserOutput.Formula.Quantified formula,





    private 
	SimpleTptpParserOutput.Formula 
	atom(String pred,LinkedList<TptpParserOutput.Term> args) {
	
	assert args != null;
	if (args.isEmpty()) args = null; 
	// because SimpleTptpParserOutput.createPlainAtom() requires this

	return 
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    atomAsFormula(_syntaxFactory.
			  createPlainAtom(pred,
					  args));
    } 




    private 
	SimpleTptpParserOutput.Formula 
	atom(String pred,TptpParserOutput.Term arg) {

	LinkedList<TptpParserOutput.Term> arguments = 
	    new LinkedList<TptpParserOutput.Term>();
	arguments.addLast(arg);

	return atom(pred,arguments);
    } 


    private 
	SimpleTptpParserOutput.Formula 
	atom(String pred,
	     TptpParserOutput.Term arg1,
	     TptpParserOutput.Term arg2) {

	LinkedList<TptpParserOutput.Term> arguments = 
	    new LinkedList<TptpParserOutput.Term>();
	arguments.addLast(arg1);
	arguments.addLast(arg2);

	return atom(pred,arguments);
    } 

    private 
	SimpleTptpParserOutput.Formula 
	atom(String pred,
	     TptpParserOutput.Term arg1,
	     TptpParserOutput.Term arg2,
	     TptpParserOutput.Term arg3) {

	LinkedList<TptpParserOutput.Term> arguments = 
	    new LinkedList<TptpParserOutput.Term>();
	arguments.addLast(arg1);
	arguments.addLast(arg2);
	arguments.addLast(arg3);

	return atom(pred,arguments);
    } 


    
    private SimpleTptpParserOutput.Formula builtInFalse() {
	return 
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    atomAsFormula(_syntaxFactory.builtInFalse());
    }
    
    private SimpleTptpParserOutput.Formula builtInTrue() {
	return 
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    atomAsFormula(_syntaxFactory.builtInTrue());
    }

    private 
	SimpleTptpParserOutput.Formula 
	forAll(LinkedList<String> vars,
	       TptpParserOutput.FofFormula matrix) {

	return 
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createQuantifiedFormula(TptpParserOutput.Quantifier.ForAll,
				    vars,
				    matrix);
    }


    private 
	SimpleTptpParserOutput.Formula 
	forAll(String var,
	       TptpParserOutput.FofFormula matrix) {
	
	LinkedList<String> vars = new LinkedList<String>();
	vars.addLast(var);
	return forAll(vars,matrix);
    }

    private 
	SimpleTptpParserOutput.Formula 
	forAll(String var1,
	       String var2,
	       TptpParserOutput.FofFormula matrix) {
	
	LinkedList<String> vars = new LinkedList<String>();
	vars.addLast(var2);
	vars.addLast(var1);
	return forAll(vars,matrix);
    }

    private 
	SimpleTptpParserOutput.Formula 
	forAll(String var1,
	       String var2,
	       String var3,
	       TptpParserOutput.FofFormula matrix) {
	
	LinkedList<String> vars = new LinkedList<String>();
	vars.addLast(var3);
	vars.addLast(var2);
	vars.addLast(var1);
	return forAll(vars,matrix);
    }



    private 
	SimpleTptpParserOutput.Formula 
	exist(LinkedList<String> vars,
	      TptpParserOutput.FofFormula matrix) {
	
	return 
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createQuantifiedFormula(TptpParserOutput.Quantifier.Exists,
				    vars,
				    matrix);
    }

    private 
	SimpleTptpParserOutput.Formula 
	exist(String var,
	      TptpParserOutput.FofFormula matrix) {
	
	LinkedList<String> vars = new LinkedList<String>();
	vars.addLast(var);
	return exist(vars,matrix);
    }

    private 
	SimpleTptpParserOutput.Formula 
	not(TptpParserOutput.FofFormula form) {
	
	
	return
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createNegationOf(form);

    } // not(TptpParserOutput.FofFormula form)


    private 
	SimpleTptpParserOutput.Formula 
	and(TptpParserOutput.FofFormula form1,
	    TptpParserOutput.FofFormula form2) {
	
	return
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createBinaryFormula(form1,
				TptpParserOutput.BinaryConnective.And,
				form2);

    } // and(TptpParserOutput.FofFormula form1,..)


    private 
	SimpleTptpParserOutput.Formula 
	or(TptpParserOutput.FofFormula form1,
	   TptpParserOutput.FofFormula form2) {
	
	return
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createBinaryFormula(form1,
				TptpParserOutput.BinaryConnective.Or,
				form2);

    } // or(TptpParserOutput.FofFormula form1,..)


    private 
	SimpleTptpParserOutput.Formula 
	implies(TptpParserOutput.FofFormula form1,
		TptpParserOutput.FofFormula form2) {
	
	return
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createBinaryFormula(form1,
				TptpParserOutput.BinaryConnective.Implication,
				form2);

    } // implies(TptpParserOutput.FofFormula form1,..)



    private 
	SimpleTptpParserOutput.Formula 
	equivalent(TptpParserOutput.FofFormula form1,
		   TptpParserOutput.FofFormula form2) {
	
	return
	    (SimpleTptpParserOutput.Formula)
	    _syntaxFactory.
	    createBinaryFormula(form1,
				TptpParserOutput.BinaryConnective.Equivalence,
				form2);

    } // equivalent(TptpParserOutput.FofFormula form1,..)




    /** Creater the formula (var1 == var2) */
    private
	SimpleTptpParserOutput.Formula 
	equalityApplication(String var1,String var2) {
	return 
	    equalityApplication(_syntaxFactory.createVariableTerm(var1),
				_syntaxFactory.createVariableTerm(var2));

    } // equalityApplication(String var1,String var2)
	
    
    /** Creater the formula (term1 == term2) */
    private
	SimpleTptpParserOutput.Formula 
	equalityApplication(TptpParserOutput.Term term1,
			    TptpParserOutput.Term term2) {

	return atom(equalityPredicate(),term1,term2);

    } // equalityApplication(TptpParserOutput.Term term1,..)
	


    
    private 
	SimpleTptpParserOutput.Term 
	skolemTerm(LinkedList<String> universalVariables) {

	if (universalVariables.isEmpty())
	    return 
		(SimpleTptpParserOutput.Term)
		_syntaxFactory.createPlainTerm(freshSkolemIndividualConstant(),
					       null);
	String func = freshSkolemFunction(universalVariables.size());
	
	LinkedList<TptpParserOutput.Term> args = 
	    new LinkedList<TptpParserOutput.Term>();

	for (String var : universalVariables)
	    args.addLast(_syntaxFactory.createVariableTerm(var));

	return 
	    (SimpleTptpParserOutput.Term)
	    _syntaxFactory.createPlainTerm(func,args);

    } // skolemTerm(LinkedList<String> universalVariables)



    /** Replaces all the occurences of the variable <code>var</code>
     *  in <code>form</code> by the term <code>replacement</code>.
     */
    private 
	SimpleTptpParserOutput.Formula 
	replace(SimpleTptpParserOutput.Formula form,
		String var,
		SimpleTptpParserOutput.Term replacement) {

       
	switch (form.getKind())
	    {
	    case Atomic:
		{
		    String pred = 
			((SimpleTptpParserOutput.Formula.Atomic)form).
			getPredicate();
		    
		    LinkedList<TptpParserOutput.Term> newArgs = 
			new LinkedList<TptpParserOutput.Term>();

		    if (((SimpleTptpParserOutput.Formula.Atomic)form).
			getArguments() == null ||
			!((SimpleTptpParserOutput.Formula.Atomic)form).
			getArguments().iterator().hasNext())
			{
			    newArgs = null;
			}
		    else
			for (TptpParserOutput.Term arg : 
				 ((SimpleTptpParserOutput.Formula.Atomic)form).
				 getArguments())
			    newArgs.addLast(replace((SimpleTptpParserOutput.Term)arg,
						    var,
						    replacement));
		    return 
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.createPlainAtom(pred,newArgs);
		}

	    case Binary:
		{ 
		    TptpParserOutput.BinaryConnective connective =
			((SimpleTptpParserOutput.Formula.Binary)form).
			getConnective();
		    

		    SimpleTptpParserOutput.Formula newLHS = 
			replace(((SimpleTptpParserOutput.Formula.Binary)form).
				getLhs(),
				var,
				replacement);
		    SimpleTptpParserOutput.Formula newRHS = 
			replace(((SimpleTptpParserOutput.Formula.Binary)form).
				getRhs(),
				var,
				replacement);

			return 
			    (SimpleTptpParserOutput.Formula)
			    _syntaxFactory.createBinaryFormula(newLHS,
							       connective,
							       newRHS);
		}

	    case Negation:
		return
		    (SimpleTptpParserOutput.Formula)
		    _syntaxFactory.
		    createNegationOf(replace(((SimpleTptpParserOutput.Formula.Negation)form).
					     getArgument(),
					     var,
					     replacement));
		
	    case Quantified:
		{
		    LinkedList<String> quantifiedVarAsList = new LinkedList<String>();
		    
		    quantifiedVarAsList.add(((SimpleTptpParserOutput.Formula.Quantified)form).
					    getVariable());
		    
		    return
			(SimpleTptpParserOutput.Formula)
			_syntaxFactory.
			createQuantifiedFormula(((SimpleTptpParserOutput.Formula.Quantified)form).
						getQuantifier(),
						quantifiedVarAsList,
						replace(((SimpleTptpParserOutput.Formula.Quantified)form).
							getMatrix(),
							var,
							replacement));
		}
					    
	    } // switch (formula.getKind())
	
	assert false;
	
	return null;

    } // replace(SimpleTptpParserOutput.Formula form,
	       

    /** Replaces all the occurences of the variable <code>var</code>
     *  in <code>term/code> by the term <code>replacement</code>.
     */
    private 
	SimpleTptpParserOutput.Term 
	replace(SimpleTptpParserOutput.Term term,
		String var,
		SimpleTptpParserOutput.Term replacement) {

	if (term.getTopSymbol().isVariable())
	    if (term.getTopSymbol().getText().equals(var))
		{
		    return replacement;
		}
	    else 
		return term;
	    
	if (term.getNumberOfArguments() == 0) 
	    return term;


	LinkedList<TptpParserOutput.Term> newArgs = 
	    new LinkedList<TptpParserOutput.Term>();
	
	for (TptpParserOutput.Term arg : term.getArguments())
	    newArgs.addLast(replace((SimpleTptpParserOutput.Term)arg,var,replacement));

	
	return 
	    (SimpleTptpParserOutput.Term)
	    _syntaxFactory.
	    createPlainTerm(term.getTopSymbol().getText(),
			    newArgs);

    } // replace(SimpleTptpParserOutput.Term term,
	       










    private String tptpfyIRI(String iri,String prefix) {

	URI uri;

	try
	    {
		uri = new URI(iri);
	    }
	catch (URISyntaxException ex)
	    {
		return "'" + iri + "'";
	    };

	if (uri.getFragment() == null)
	    {
		String pathSuffix = pathSuffix(uri.getPath());
		if (!pathSuffix.equals(""))
		    return prefix + pathSuffix;		    
		
		return "'" + iri + "'";
	    };
	
	return tptpfyId(prefix + uri.getFragment()); 

    } // tptpfyIRI(String iri,String prefix)
    

    
    private String tptpfyId(String id) {

	String res = id;

	res = res.replace('-','_');
	res = res.replace('%','_');
	res = res.replace('.','_');
	
	return res;

    } // tptpfyId(String id)
    
   

    /** Maps the IRI as a positional predicate or function of the specified 
     *  arity (> 0) into a TPTP identifier; the mapping is consistent with 
     *  the previous identifier assignments.
     */
    private 
	String positionalSymbolForIRI(boolean predicate,String iri,int arity) {

	assert arity > 0;
		
	Set<TPTPSymbolDescriptor> symDescSet = 
	    _parameters.IRIsToTPTPSymbols().get(iri);

	if (symDescSet == null)
	    {
		symDescSet = new HashSet<TPTPSymbolDescriptor>();
		_parameters.IRIsToTPTPSymbols().put(iri,symDescSet);
	    }
	else
	    {
		// Check if the predicate has already been mapped:
		for (TPTPSymbolDescriptor desc : symDescSet)
		    if ((desc instanceof TPTPPositionalSymbolDescriptor) &&
			(desc.isPredicate() == predicate) && 
			(desc.arity() == arity))
			return desc.name();
	    };

	String result = tptpfyIRI(iri,(predicate)? "p_" : "f_");
	
	if (symbolIsReserved(predicate,result,arity))
	    {
		int i;
		for (i = 0;
		     symbolIsReserved(predicate,result + i,arity);
		     ++i);
		result = result + i;
	    };

	TPTPPositionalSymbolDescriptor desc = 
	    new TPTPPositionalSymbolDescriptor(predicate,result,arity);

	symDescSet.add(desc);

	_reservedSymbols.add(desc);
     
	return result;

    } // positionalSymbolForIRI(String iri,int arity) 



    /** Maps the IRI as a slotted predicate or function whose arguments
     *  are specified with <code>slotNames</code>,
     *  into a TPTP identifier; the mapping is consistent with 
     *  the previous identifier assignments.
     */
    private 
	String 
	slottedSymbolForIRI(boolean predicate,
			    String iri,
			    SortedSet<String> slotNames) {

	Set<TPTPSymbolDescriptor> symDescSet = 
	    _parameters.IRIsToTPTPSymbols().get(iri);

	if (symDescSet == null)
	    {
		symDescSet = new HashSet<TPTPSymbolDescriptor>();
		_parameters.IRIsToTPTPSymbols().put(iri,symDescSet);
	    }
	else
	    {
		// Check if the predicate has already been mapped:
		for (TPTPSymbolDescriptor desc : symDescSet)
		    if ((desc instanceof TPTPSlottedSymbolDescriptor) &&
			(desc.isPredicate() == predicate) && 
			(((TPTPSlottedSymbolDescriptor)desc).slots().
			 equals(slotNames)))
			return desc.name();
	    };

	int arity = slotNames.size();

	String result = 
	    tptpfyIRI(iri,
		      (predicate)? 
		      "sp_" 
		      : 
		      ((arity == 0)? "sc_" : "sf_"));
	

	if (symbolIsReserved(predicate,result,arity))
	    {
		int i;
		for (i = 0;
		     symbolIsReserved(predicate,result + i,arity);
		     ++i);
		result = result + i;
	    };

	TPTPSlottedSymbolDescriptor desc = 
	    new TPTPSlottedSymbolDescriptor(predicate,result,slotNames);

	symDescSet.add(desc);

	_reservedSymbols.add(desc);
     
	return result;

    } // slottedSymbolForIRI(boolean predicate,..




    /** Maps the IRI as an individual constant;
     *  the mapping is consistent with the previous identifier assignments.
     */
    private String individualConstantForIRI(String iri) throws Exception  {

	Set<TPTPSymbolDescriptor> symDescSet = 
	    _parameters.IRIsToTPTPSymbols().get(iri);

	if (symDescSet == null)
	    {
		symDescSet = new HashSet<TPTPSymbolDescriptor>();
		_parameters.IRIsToTPTPSymbols().put(iri,symDescSet);
	    }
	else
	    {
		// Check if the constant has already been mapped:
		for (TPTPSymbolDescriptor desc : symDescSet)
		    if (!desc.isPredicate() && desc.arity() == 0)
			return desc.name();
	    };

	String result = tptpfyIRI(iri,"c_");
	
	if (symbolIsReserved(false,result,0))
	    {
		int i;
		for (i = 0;
		     symbolIsReserved(false,result + i,0);
		     ++i);
		result = result + i;
	    };


	TPTPConstantSymbolDescriptor desc = 
	    new TPTPConstantSymbolDescriptor(result);

	symDescSet.add(desc);

	_reservedSymbols.add(desc);
     
	return result;

    } // individualConstantForIRI(String iri) 

    
    private String freshSkolemIndividualConstant() {

	while (symbolIsReserved(false,"skc" + _nextSkolemSymIndex,0))
	    ++_nextSkolemSymIndex;

	String result = "skc" + _nextSkolemSymIndex++;

	TPTPConstantSymbolDescriptor desc = 
	    new TPTPConstantSymbolDescriptor(result);

	_reservedSymbols.add(desc);
	_parameters.otherReservedSymbols().add(desc);

	return result;

    } // freshSkolemIndividualConstant()
    

    /** Works for constants too. */
    private String freshSkolemFunction(int arity) {
	if (arity == 0) return freshSkolemIndividualConstant();
	
	while (symbolIsReserved(false,"skf" + _nextSkolemSymIndex,arity))
	    ++_nextSkolemSymIndex;

	String result = "skf" + _nextSkolemSymIndex++;

	TPTPPositionalSymbolDescriptor desc = 
	    new TPTPPositionalSymbolDescriptor(false,result,arity);

	_reservedSymbols.add(desc);
	_parameters.otherReservedSymbols().add(desc);

	return result;
	

    } // freshSkolemFunction(int arity)


    
    

    private String equalityPredicate() {

	String result = _parameters.equalityPredicate();
	if (result != null) return result;
	
	result = "=";

	assert !symbolIsReserved(true,result,2);

	_parameters.setEqualityPredicate(result);

	return result;


    } // equalityPredicate() 
    
    

    private String memberPredicate() {

	String result = _parameters.memberPredicate();
	if (result != null) return result;
	
	result = "member";

	assert !symbolIsReserved(true,result,2);

	_parameters.setMemberPredicate(result);

	return result;
    } 


    private String subclassPredicate() {

	String result = _parameters.subclassPredicate();
	if (result != null) return result;
	
	result = "subclass";

	assert !symbolIsReserved(true,result,2);

	_parameters.setSubclassPredicate(result);

	return result;
    } 


    private String methodApplicationPredicate() {

	String result = _parameters.methodApplicationPredicate();
	if (result != null) return result;
	
	result = "methodApplication";

	assert !symbolIsReserved(true,result,3);

	_parameters.setMethodApplicationPredicate(result);

	return result;
    } 

    
    
    /** Id for the constructor function for integer literals. */
    private String intLitFunc() {
	String result = _parameters.intLitFunc();
	if (result != null) return result;
	
	result = "intLit";

	if (symbolIsReserved(false,result,1))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };

	_parameters.setIntLitFunc(result);

	return result;
    }




    /** Id for the constructor function for string literals
     *  with no language specified.
     */
    private String stringLitNoLangFunc() {
	String result = _parameters.stringLitNoLangFunc();
	if (result != null) return result;
	
	result = "stringLitNoLang";

	if (symbolIsReserved(false,result,1))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };


	_parameters.setStringLitNoLangFunc(result);

	return result;
    }

    /** Id for the constructor function for string literals
     *  with some language specified.
     */
    private String stringLitWithLangFunc() { 

	String result = _parameters.stringLitWithLangFunc();
	if (result != null) return result;
	
	result = "stringLitWithLang";

	if (symbolIsReserved(false,result,2))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };


	_parameters.setStringLitWithLangFunc(result);

	return result;
    }
 


    private String typedLitFunc() {

	String result = _parameters.typedLitFunc();
	if (result != null) return result;
	
	result = "typedLit";

	if (symbolIsReserved(false,result,2))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };

	_parameters.setTypedLitFunc(result);

	return result;
    }
    


    private String emptyListConstant() {

	String result = _parameters.emptyListConstant();
	if (result != null) return result;
	
	result = "emptyListConstant";

	if (symbolIsReserved(false,result,2))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };

	_parameters.setEmptyListConstant(result);

	return result;
    }
    

    private String listConsFunc() {

	String result = _parameters.listConsFunc();
	if (result != null) return result;
	
	result = "listCons";

	if (symbolIsReserved(false,result,2))
	    {
		int i;
		for (i = 0; symbolIsReserved(false,result + i,1); ++i);
		result = result + i;
	    };

	_parameters.setListConsFunc(result);

	return result;
    }
    


    private String generateNewVariable(String prefix) {
	return prefix + _nextVarIndex++;
    }
    
    
    /** Extracts the last segment of the URI path, ie, the substring
     *  (possibly empty) following the last '/' in the path.
     *  @param path can be null, in which case the result is ""
     */
    private String pathSuffix(String path) {
	
	if (path == null) return "";
	
	for (int i = path.length() - 1; i >= 0; --i)
	    if (path.charAt(i) == '/')
		return path.substring(i + 1);
	
	// No '/'
	
	return path;

    } // pathSuffix(String path)


    /** Checks if a symbol with the specified parameters 
     *  has already been reserved in some way, so that a new
     *  symbol with these parameters cannot be introduced;
     *  note that we do not distinguish constants and nullary 
     *  functions here: if a constant "a" was reserved,
     *  we do not allow to introduce "a" as a nullary functor.
     */
    private boolean symbolIsReserved(boolean predicate,
				     String name,
				     int arity) {

	for (TPTPSymbolDescriptor desc : _reservedSymbols)
	    if (desc.isPredicate() == predicate &&
		desc.name().equals(name) &&
		desc.arity() == arity)
		return true;

	return false;

    } // symbolIsReserved(boolean predicate,..)







    //            Data:

    /** Various parameters that guide the conversion; some of 
     *  the parameters are modified by the conversion within this object
     *  and can be used afterwards in other objects; eg, mappings
     *  of IRIs to TPTP identifiers are augmented when new IRIs are
     *  encountered.
     */
    private Parameters _parameters;

    private boolean _generateCNF;


    /** Factory for TPTP abstract syntax datastructures. */
    private SimpleTptpParserOutput _syntaxFactory;

    // The value is reset to 0 every time a new axiom is converted.
    private int _nextVarIndex;

    private int _nextSkolemSymIndex;

    private int _nextAxiomIndex;

    /** Fast access to all reserved symbols. */
    private HashSet<TPTPSymbolDescriptor> _reservedSymbols;
    
} // class Converter
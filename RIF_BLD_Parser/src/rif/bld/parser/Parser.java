/* 
 * This software is a part of a parser for the BLD dialect 
 * of the RIF language. 
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


package rif.bld.parser;

import java.io.*;
import java.math.*;
import java.util.*;
import java.net.*;
import javax.xml.bind.*;
import javax.xml.bind.util.*;
import javax.xml.validation.*;

import rif.bld.parser.jaxb.*;
import rif.bld.parser.*;
import rif.bld.AbstractSyntax;

/** Parses RIF BLD documents. */
public class Parser {


    public Parser() throws java.lang.Exception {

	JAXBContext jc = 
	    JAXBContext.newInstance("rif.bld.parser.jaxb");
	_unmarshaller = jc.createUnmarshaller();
	

	SchemaFactory schemaFactory = 
	    SchemaFactory.
	    newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	URL schemaURL = 
	    ClassLoader.
	    getSystemResource("BLDRule.xsd");
	
	Schema schema = schemaFactory.newSchema(schemaURL);

	_unmarshaller.setSchema(schema);	
    }

    /** @param file to be parsed
     *  @param absSynFactory factory of abstract syntax objects
     *         to be used to create the parsed objects
     */
    public 
	AbstractSyntax.Document 
	parse(File file, AbstractSyntax absSynFactory) 
	throws java.lang.Exception {

	try
	    {
		Document doc = (Document)_unmarshaller.unmarshal(file);
		
		Payload payload = doc.getPayload();

		if (payload != null)
		    {
			Group topLevelGroup = payload.getGroup();

			return 
			    absSynFactory.
			    createDocument(convert(topLevelGroup,
						   absSynFactory));
		    }
		else
		    return absSynFactory.createDocument(null);

	    }
	catch (javax.xml.bind.UnmarshalException ex)
	    {
		throw 
		    new java.lang.Exception("RIF BLD file cannot be read: " +
					    ex);
	    }

    } // parse(File file,AbstractSyntax absSynFactory)

    /** Parse a separate RIF BLD formula.
     *  @param file to be parsed
     *  @param absSynFactory factory of abstract syntax objects
     *         to be used to create the parsed objects
     */
    public 
	AbstractSyntax.Formula
	parseFormula(File file,AbstractSyntax absSynFactory) 
	throws java.lang.Exception {

	try
	    {
		java.lang.Object form = _unmarshaller.unmarshal(file);
		
		if (form instanceof And)
		    {
			return convert((And)form,absSynFactory);
		    }
		else if (form instanceof Or)
		    {
			return convert((Or)form,absSynFactory);
		    }
		else if (form instanceof Exists)
		    {
			return convert((Exists)form,absSynFactory);
		    }
		else if (form instanceof Atom)
		    {
			return convert((Atom)form,absSynFactory);
		    }
		else if (form instanceof Equal)
		    {
			return convert((Equal)form,absSynFactory);
		    }
		else if (form instanceof Member)
		    {
			return convert((Member)form,absSynFactory);
		    }	
		else if (form instanceof Subclass)
		    {
			return convert((Subclass)form,absSynFactory);
		    }
		else if (form instanceof Frame)
		    {
			return convert((Frame)form,absSynFactory);
		    }
		else if (form instanceof ExternalFORMULAType)
		    {
			return convert((ExternalFORMULAType)form,absSynFactory);
		    }
		else 
		    throw new java.lang.Exception("Input cannot be parsed as a formula: a single conjecture formula is expected because of the -j (--conjecture) switch.");

	    }
	catch (javax.xml.bind.UnmarshalException ex)
	    {
		throw  
		    new java.lang.Exception("RIF BLD file cannot be read: " +
					    ex);
	    }

    } // parseFormula(File file,AbstractSyntax absSynFactory)



    private 
	AbstractSyntax.Group 
	convert(Group group,AbstractSyntax absSynFactory) {
	
	LinkedList<AbstractSyntax.Sentence> sentences = 
	    new LinkedList<AbstractSyntax.Sentence>();

	for (Sentence sent : group.getSentence())
	    sentences.addLast(convert(sent,absSynFactory));

	return absSynFactory.createGroup(sentences);
	
    } // convert(Group group)

    

    private 
	AbstractSyntax.Sentence 
	convert(Sentence sent,AbstractSyntax absSynFactory) {
	
	if (sent.getForall() != null)
	    return 
		absSynFactory.
		createSentence(convert(sent.getForall(),absSynFactory));
	
	if (sent.getImplies() != null)
	    {
		AbstractSyntax.Clause clause = 
		    convert(sent.getImplies(),absSynFactory);

		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		    
		return absSynFactory.createSentence(rule);
	    };

	if (sent.getAtom() != null)
	    {
		AbstractSyntax.Atom atom = 
		    convert(sent.getAtom(),absSynFactory);
		
		LinkedList<AbstractSyntax.Atom> head = 
		    new LinkedList<AbstractSyntax.Atom>();
		head.addLast(atom);

		AbstractSyntax.Clause clause = 
		    absSynFactory.createClause(head,null);
		
		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		
		return absSynFactory.createSentence(rule);
	    };
		

	if (sent.getEqual() != null)
	    {
		AbstractSyntax.Equal equal = 
		    convert(sent.getEqual(),absSynFactory);
		
		LinkedList<AbstractSyntax.Equal> head = 
		    new LinkedList<AbstractSyntax.Equal>();
		head.addLast(equal);

		AbstractSyntax.Clause clause = 
		    absSynFactory.createClause(head,null);
		
		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		
		return absSynFactory.createSentence(rule);
	    };
		

	if (sent.getMember() != null)
	    {
		AbstractSyntax.Member member = 
		    convert(sent.getMember(),absSynFactory);
		
		LinkedList<AbstractSyntax.Member> head = 
		    new LinkedList<AbstractSyntax.Member>();
		head.addLast(member);

		AbstractSyntax.Clause clause = 
		    absSynFactory.createClause(head,null);
		
		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		
		return absSynFactory.createSentence(rule);
	    };
		

	if (sent.getSubclass() != null)
	    {
		AbstractSyntax.Subclass subclass = 
		    convert(sent.getSubclass(),absSynFactory);
		
		LinkedList<AbstractSyntax.Subclass> head = 
		    new LinkedList<AbstractSyntax.Subclass>();
		head.addLast(subclass);

		AbstractSyntax.Clause clause = 
		    absSynFactory.createClause(head,null);
		
		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		
		return absSynFactory.createSentence(rule);
	    };
		

	if (sent.getFrame() != null)
	    {
		AbstractSyntax.Frame frame = 
		    convert(sent.getFrame(),absSynFactory);
		
		LinkedList<AbstractSyntax.Frame> head = 
		    new LinkedList<AbstractSyntax.Frame>();
		head.addLast(frame);

		AbstractSyntax.Clause clause = 
		    absSynFactory.createClause(head,null);
		
		AbstractSyntax.Rule rule =
		    absSynFactory.createRule(null,clause);
		
		return absSynFactory.createSentence(rule);
	    };
		


	
	if (sent.getGroup() != null)
	    return 
		absSynFactory.
		createSentence(convert(sent.getGroup(),absSynFactory));

	throw new Error("Bad instance of rif.bld.parser.jaxb.Sentence.");

    } // convert(Sentence sent,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.Rule 
	convert(Forall forall,AbstractSyntax absSynFactory) {
	
	LinkedList<AbstractSyntax.Var> vars = 
	    convertVars(forall.getDeclare(),absSynFactory);
	
	Forall.Formula form = forall.getFormula();

	AbstractSyntax.Clause clause;

	if (form.getImplies() != null)
	    {
		clause = convert(form.getImplies(),absSynFactory);
	    }
	else if (form.getAtom() != null)
	    {
		AbstractSyntax.Atom atom = 
		    convert(form.getAtom(),absSynFactory);
		
		LinkedList<AbstractSyntax.Atom> head = 
		    new LinkedList<AbstractSyntax.Atom>();
		head.addLast(atom);

		clause = absSynFactory.createClause(head,null);
	    }
	else if (form.getEqual() != null)
	    {
		AbstractSyntax.Equal equal = 
		    convert(form.getEqual(),absSynFactory);
		
		LinkedList<AbstractSyntax.Equal> head = 
		    new LinkedList<AbstractSyntax.Equal>();
		head.addLast(equal);

		clause = absSynFactory.createClause(head,null);
	    }
	else if (form.getMember() != null)
	    {
		AbstractSyntax.Member member = 
		    convert(form.getMember(),absSynFactory);
		
		LinkedList<AbstractSyntax.Member> head = 
		    new LinkedList<AbstractSyntax.Member>();
		head.addLast(member);

		clause = absSynFactory.createClause(head,null);
	    }	
	else if (form.getSubclass() != null)
	    {
		AbstractSyntax.Subclass subclass = 
		    convert(form.getSubclass(),absSynFactory);
		
		LinkedList<AbstractSyntax.Subclass> head = 
		    new LinkedList<AbstractSyntax.Subclass>();
		head.addLast(subclass);

		clause = absSynFactory.createClause(head,null);
	    }
	else if (form.getFrame() != null)
	    {
		AbstractSyntax.Frame frame = 
		    convert(form.getFrame(),absSynFactory);
		
		LinkedList<AbstractSyntax.Frame> head = 
		    new LinkedList<AbstractSyntax.Frame>();
		head.addLast(frame);

		clause = absSynFactory.createClause(head,null);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Forall.Formula");
	
	return absSynFactory.createRule(vars,clause);

    } // convert(Forall forall,AbstractSyntax absSynFactory)



    private 
	AbstractSyntax.Clause 
	convert(Implies implies,AbstractSyntax absSynFactory) {
	

	Then conclusion = implies.getThen();
	assert conclusion != null;

	LinkedList<AbstractSyntax.Atomic> head = 
	    new LinkedList<AbstractSyntax.Atomic>();	
	
	if (conclusion.getAtom() != null)
	    {
		head.addLast(convert(conclusion.getAtom(),absSynFactory));
	    }
	else if (conclusion.getEqual() != null)
	    {
		head.addLast(convert(conclusion.getEqual(),absSynFactory));
	    }
	else if (conclusion.getMember() != null)
	    {
		head.addLast(convert(conclusion.getMember(),absSynFactory));
	    }	
	else if (conclusion.getSubclass() != null)
	    {
		head.addLast(convert(conclusion.getSubclass(),absSynFactory));
	    }
	else if (conclusion.getFrame() != null)
	    {
		head.addLast(convert(conclusion.getFrame(),absSynFactory));
	    }
	else if (conclusion.getAnd() != null)
	    {
		java.util.List<FormulaThenType> formulas = 
		    conclusion.getAnd().getFormula(); 

		for (FormulaThenType form : formulas)
		    {
			if (form.getAtom() != null)
			    {
				head.addLast(convert(form.getAtom(),absSynFactory));
			    }
			else if (form.getEqual() != null)
			    {
				head.addLast(convert(form.getEqual(),absSynFactory));
			    }
			else if (form.getMember() != null)
			    {
				head.addLast(convert(form.getMember(),absSynFactory));
			    }	
			else if (form.getSubclass() != null)
			    {
				head.addLast(convert(form.getSubclass(),absSynFactory));
			    }
			else if (form.getFrame() != null)
			    {
				head.addLast(convert(form.getFrame(),absSynFactory));
			    }
			else 
			    throw new Error("Bad instance of rif.bld.parser.jaxb.FormulaThenType.");

		    }; // for (FormulaThenType form : formulas)
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Then.");
	




	If condition = implies.getIf();
	assert condition != null;

	
	AbstractSyntax.Formula body;

	if (condition.getAnd() != null)
	    {
		body = convert(condition.getAnd(),absSynFactory);
	    }
	else if (condition.getOr() != null)
	    {
		body = convert(condition.getOr(),absSynFactory);
	    }
	else if (condition.getExists() != null)
	    {
		body = convert(condition.getExists(),absSynFactory);
	    }
	else if (condition.getAtom() != null)
	    {
		body = convert(condition.getAtom(),absSynFactory);
	    }
	else if (condition.getEqual() != null)
	    {
		body = convert(condition.getEqual(),absSynFactory);
	    }
	else if (condition.getMember() != null)
	    {
		body = convert(condition.getMember(),absSynFactory);
	    }	
	else if (condition.getSubclass() != null)
	    {
		body = convert(condition.getSubclass(),absSynFactory);
	    }
	else if (condition.getFrame() != null)
	    {
		body = convert(condition.getFrame(),absSynFactory);
	    }
	else if (condition.getExternal() != null)
	    {
		body = convert(condition.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.If.");
	


	return absSynFactory.createClause(head,body);
	
    } // convert(Implies implies,AbstractSyntax absSynFactory)





    private 
	AbstractSyntax.Formula
	convert(Formula form,AbstractSyntax absSynFactory) {

	if (form.getAnd() != null)
	    {
		return convert(form.getAnd(),absSynFactory);
	    }
	else if (form.getOr() != null)
	    {
		return convert(form.getOr(),absSynFactory);
	    }
	else if (form.getExists() != null)
	    {
		return convert(form.getExists(),absSynFactory);
	    }
	else if (form.getAtom() != null)
	    {
		return convert(form.getAtom(),absSynFactory);
	    }
	else if (form.getEqual() != null)
	    {
		return convert(form.getEqual(),absSynFactory);
	    }
	else if (form.getMember() != null)
	    {
		return convert(form.getMember(),absSynFactory);
	    }	
	else if (form.getSubclass() != null)
	    {
		return convert(form.getSubclass(),absSynFactory);
	    }
	else if (form.getFrame() != null)
	    {
		return convert(form.getFrame(),absSynFactory);
	    }
	else if (form.getExternal() != null)
	    {
		return convert(form.getExternal(),absSynFactory);
	    }
	else 
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Formula.");


    } // convert(Formula form,AbstractSyntax absSynFactory)



    private 
	AbstractSyntax.And
	convert(And form,AbstractSyntax absSynFactory) {

	LinkedList<AbstractSyntax.Formula> args = 
	    new LinkedList<AbstractSyntax.Formula>();

	for (Formula f : form.getFormula())
	    {
		if (f.getAnd() != null)
		    {
			args.addLast(convert(f.getAnd(),absSynFactory));
		    }
		else if (f.getOr() != null)
		    {
			args.addLast(convert(f.getOr(),absSynFactory));
		    }
		else if (f.getExists() != null)
		    {
			args.addLast(convert(f.getExists(),absSynFactory));
		    }
		else if (f.getAtom() != null)
		    {
			args.addLast(convert(f.getAtom(),absSynFactory));
		    }
		else if (f.getEqual() != null)
		    {
			args.addLast(convert(f.getEqual(),absSynFactory));
		    }
		else if (f.getMember() != null)
		    {
			args.addLast(convert(f.getMember(),absSynFactory));
		    }	
		else if (f.getSubclass() != null)
		    {
			args.addLast(convert(f.getSubclass(),absSynFactory));
		    }
		else if (f.getFrame() != null)
		    {
			args.addLast(convert(f.getFrame(),absSynFactory));
		    }
		else if (f.getExternal() != null)
		    {
			args.addLast(convert(f.getExternal(),absSynFactory));
		    }
		else 
		    throw new Error("Bad instance of rif.bld.parser.jaxb.Formula.");
		
	    }; // for (Formula f : formulas)

	return absSynFactory.createAnd(args);
	
    } // convert(And form,AbstractSyntax absSynFactory)


    private 
	AbstractSyntax.Or
	convert(Or form,AbstractSyntax absSynFactory) {

	LinkedList<AbstractSyntax.Formula> args = 
	    new LinkedList<AbstractSyntax.Formula>();

	for (Formula f : form.getFormula())
	    {
		if (f.getAnd() != null)
		    {
			args.addLast(convert(f.getAnd(),absSynFactory));
		    }
		else if (f.getOr() != null)
		    {
			args.addLast(convert(f.getOr(),absSynFactory));
		    }
		else if (f.getExists() != null)
		    {
			args.addLast(convert(f.getExists(),absSynFactory));
		    }
		else if (f.getAtom() != null)
		    {
			args.addLast(convert(f.getAtom(),absSynFactory));
		    }
		else if (f.getEqual() != null)
		    {
			args.addLast(convert(f.getEqual(),absSynFactory));
		    }
		else if (f.getMember() != null)
		    {
			args.addLast(convert(f.getMember(),absSynFactory));
		    }	
		else if (f.getSubclass() != null)
		    {
			args.addLast(convert(f.getSubclass(),absSynFactory));
		    }
		else if (f.getFrame() != null)
		    {
			args.addLast(convert(f.getFrame(),absSynFactory));
		    }
		else if (f.getExternal() != null)
		    {
			args.addLast(convert(f.getExternal(),absSynFactory));
		    }
		else 
		    throw new Error("Bad instance of rif.bld.parser.jaxb.Formula.");
		
	    }; // for (Formula f : formulas)

	return absSynFactory.createOr(args);
	
    } // convert(Or form,AbstractSyntax absSynFactory)



    private 
	AbstractSyntax.Exists
	convert(Exists form,AbstractSyntax absSynFactory) {

	LinkedList<AbstractSyntax.Var> vars = 
	    convertVars(form.getDeclare(),absSynFactory);

	AbstractSyntax.Formula matrix =
	    convert(form.getFormula(),absSynFactory);

	return absSynFactory.createExists(vars,matrix);
	
    } // convert(Exists form,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.Atom 
	convert(Atom atom,AbstractSyntax absSynFactory) {
	
	AbstractSyntax.Const predicate = 
	    convert(atom.getOp().getConst(),absSynFactory);
	    
	if (atom.getArgs() != null)
	    {
		return
		    absSynFactory.
		    createPositionalAtom(predicate,
					 convert(atom.getArgs(),absSynFactory));
	    }
	else 
	    {
		assert atom.getSlot()  != null;
		return 
		    absSynFactory.
		    createSlottedAtom(predicate,
				      convertArgSlots(atom.getSlot(),absSynFactory));
	    }

    } // convert(Atom atom,AbstractSyntax absSynFactory) 




    private 
	AbstractSyntax.Equal 
	convert(Equal equal,AbstractSyntax absSynFactory) {
	
	Left left = equal.getLeft();
	assert left != null;

	AbstractSyntax.Term lhs;

	if (left.getConst() != null)
	    {
		lhs = convert(left.getConst(),absSynFactory);
	    }
	else if (left.getVar() != null)
	    {
		lhs = convert(left.getVar(),absSynFactory);
	    }
	else if (left.getExpr() != null)
	    {
		lhs = convert(left.getExpr(),absSynFactory);
	    }
	else if (left.getList() != null)
	    {
		lhs = convert(left.getList(),absSynFactory);
	    }
	else if (left.getExternal() != null)
	    {
		lhs = convert(left.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Left");
	    
	

	Right right = equal.getRight();
	assert right != null;

	AbstractSyntax.Term rhs;

	if (right.getConst() != null)
	    {
		rhs = convert(right.getConst(),absSynFactory);
	    }
	else if (right.getVar() != null)
	    {
		rhs = convert(right.getVar(),absSynFactory);
	    }
	else if (right.getExpr() != null)
	    {
		rhs = convert(right.getExpr(),absSynFactory);
	    }
	else if (right.getList() != null)
	    {
		rhs = convert(right.getList(),absSynFactory);
	    }
	else if (right.getExternal() != null)
	    {
		rhs = convert(right.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Right");
	    
		

	return absSynFactory.createEqual(lhs,rhs);
	

    } // convert(Equal equal,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.Member
	convert(Member member,AbstractSyntax absSynFactory) {
	
	Instance instance = member.getInstance();
	assert instance != null;

	AbstractSyntax.Term inst;

	if (instance.getConst() != null)
	    {
		inst = convert(instance.getConst(),absSynFactory);
	    }
	else if (instance.getVar() != null)
	    {
		inst = convert(instance.getVar(),absSynFactory);
	    }
	else if (instance.getExpr() != null)
	    {
		inst = convert(instance.getExpr(),absSynFactory);
	    }
	else if (instance.getList() != null)
	    {
		inst = convert(instance.getList(),absSynFactory);
	    }
	else if (instance.getExternal() != null)
	    {
		inst = convert(instance.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Instance");
	    
	

	rif.bld.parser.jaxb.Class clazz = member.getClazz();
	assert clazz != null;

	AbstractSyntax.Term cls;

	if (clazz.getConst() != null)
	    {
		cls = convert(clazz.getConst(),absSynFactory);
	    }
	else if (clazz.getVar() != null)
	    {
		cls = convert(clazz.getVar(),absSynFactory);
	    }
	else if (clazz.getExpr() != null)
	    {
		cls = convert(clazz.getExpr(),absSynFactory);
	    }
	else if (clazz.getList() != null)
	    {
		cls = convert(clazz.getList(),absSynFactory);
	    }
	else if (clazz.getExternal() != null)
	    {
		cls = convert(clazz.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Class");
	    
		
	
	return absSynFactory.createMember(inst,cls);
	

    } // convert(Member member,AbstractSyntax absSynFactory)





    private 
	AbstractSyntax.Subclass
	convert(Subclass subclass,AbstractSyntax absSynFactory) {

	Sub sub = subclass.getSub();
	assert sub != null;

	AbstractSyntax.Term subcls;

	if (sub.getConst() != null)
	    {
		subcls = convert(sub.getConst(),absSynFactory);
	    }
	else if (sub.getVar() != null)
	    {
		subcls = convert(sub.getVar(),absSynFactory);
	    }
	else if (sub.getExpr() != null)
	    {
		subcls = convert(sub.getExpr(),absSynFactory);
	    }
	else if (sub.getList() != null)
	    {
		subcls = convert(sub.getList(),absSynFactory);
	    }
	else if (sub.getExternal() != null)
	    {
		subcls = convert(sub.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Sub");
	    
	

	Super sup = subclass.getSuper();
	assert sup != null;

	AbstractSyntax.Term supcls;

	if (sup.getConst() != null)
	    {
		supcls = convert(sup.getConst(),absSynFactory);
	    }
	else if (sup.getVar() != null)
	    {
		supcls = convert(sup.getVar(),absSynFactory);
	    }
	else if (sup.getExpr() != null)
	    {
		supcls = convert(sup.getExpr(),absSynFactory);
	    }
	else if (sup.getList() != null)
	    {
		supcls = convert(sup.getList(),absSynFactory);
	    }
	else if (sup.getExternal() != null)
	    {
		supcls = convert(sup.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Super");
	    
		
	
	return absSynFactory.createSubclass(subcls,supcls);
	
	
    } // convert(Subclass subclass,AbstractSyntax absSynFactory) 





    private 
	AbstractSyntax.Frame
	convert(Frame frame,AbstractSyntax absSynFactory) {
	

	rif.bld.parser.jaxb.Object object = frame.getObject();
	assert object != null;

	AbstractSyntax.Term obj;

	if (object.getConst() != null)
	    {
		obj = convert(object.getConst(),absSynFactory);
	    }
	else if (object.getVar() != null)
	    {
		obj = convert(object.getVar(),absSynFactory);
	    }
	else if (object.getExpr() != null)
	    {
		obj = convert(object.getExpr(),absSynFactory);
	    }
	else if (object.getList() != null)
	    {
		obj = convert(object.getList(),absSynFactory);
	    }
	else if (object.getExternal() != null)
	    {
		obj = convert(object.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Object");
	    


	LinkedList<AbstractSyntax.FrameSlot> slots = 
	    convertFrameSlots(frame.getSlot(),absSynFactory);


	return absSynFactory.createFrame(obj,slots);
	

    } // convert(Frame frame,AbstractSyntax absSynFactory)






    private 
	AbstractSyntax.ExternalFormula
	convert(ExternalFORMULAType ext,AbstractSyntax absSynFactory) {
	
	return 
	    absSynFactory.
	    createExternalFormula(convert(ext.getContent().getAtom(),
					  absSynFactory));

    } 



    private 
	LinkedList<AbstractSyntax.Var> 
	convertVars(java.util.List<Declare> vars,AbstractSyntax absSynFactory) {

	LinkedList<AbstractSyntax.Var> result = 
	    new LinkedList<AbstractSyntax.Var>();

	for (Declare var : vars)
	    result.addLast(convert(var.getVar(),absSynFactory));
	
	return result;

    } // convertVars(List<Declare> dec,AbstractSyntax absSynFactory)

    
    private 
	AbstractSyntax.Var
	convert(Var var,AbstractSyntax absSynFactory) {
    
	for (java.lang.Object obj : var.getContent())
	    if (obj instanceof String)
		return absSynFactory.createVar((String)obj);
		
	throw new Error("Bad instance of rif.bld.parser.jaxb.Var");

    } // convert(Var var,AbstractSyntax absSynFactory)


    private 
	AbstractSyntax.Const
	convert(Const c,AbstractSyntax absSynFactory) {
	
	for (java.lang.Object obj : c.getContent())
	    if (obj instanceof String)
		return absSynFactory.createConst((String)obj,
						 c.getType(),
						 c.getLang());
		
	throw new Error("Bad instance of rif.bld.parser.jaxb.Const");

    } // convert(Const c,AbstractSyntax absSynFactory)

    

    private 
	AbstractSyntax.Expr
	convert(Expr ex,AbstractSyntax absSynFactory) {
	
	AbstractSyntax.Const function = 
	    convert(ex.getOp().getConst(),absSynFactory);
	    
	if (ex.getArgs() != null)
	    {
		return
		    absSynFactory.
		    createPositionalExpr(function,
					 convert(ex.getArgs(),absSynFactory));
	    }
	else 
	    {
		assert ex.getSlot()  != null;
		return 
		    absSynFactory.
		    createSlottedExpr(function,
				      convertArgSlots(ex.getSlot(),absSynFactory));
	    }

    } // convert(Expr ex,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.Term
	convert(rif.bld.parser.jaxb.List lst,AbstractSyntax absSynFactory) {
	
	AbstractSyntax.Term result;
	
	if (lst.getRest() == null)
	    {
		// closed list
		result = absSynFactory.createEmptyList();
	    }
	else 
	    {
		// open list
		result = convert(lst.getRest(),absSynFactory);
	    };	
       
	java.util.List<java.lang.Object> terms = lst.getTERM();
	
	for (int i = terms.size() - 1; i >= 0; ++i)
	    {
		java.lang.Object obj = terms.get(i);
		
		AbstractSyntax.Term term;

		if (obj instanceof rif.bld.parser.jaxb.Var)
		    {
			term = 
			    convert((rif.bld.parser.jaxb.Var)obj,
				    absSynFactory);
		    }
		else if (obj instanceof rif.bld.parser.jaxb.Expr) 
		    {
			term = 
			    convert((rif.bld.parser.jaxb.Expr)obj,
				    absSynFactory);
		    }
		else if (obj instanceof rif.bld.parser.jaxb.ExternalTERMType) 
		    {
			term = 
			    convert((rif.bld.parser.jaxb.ExternalTERMType)obj,
				    absSynFactory);
		    }
		else if (obj instanceof rif.bld.parser.jaxb.List) 
		    {
			term = 
			    convert((rif.bld.parser.jaxb.List)obj,
				    absSynFactory);
		    }
		else if (obj instanceof rif.bld.parser.jaxb.Const) 
		    {
			term = 
			    convert((rif.bld.parser.jaxb.Const)obj,
				    absSynFactory);
		    }
		else 
		    throw new Error("Bad instance of rif.bld.parser.jaxb.List.");
	

		result = absSynFactory.createList(term,result);

	    }; // for (int i = terms.size() - 1; i >= 0; ++i)
	
	return result;

    } // convert(List lst,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.Term
	convert(rif.bld.parser.jaxb.Rest lst,AbstractSyntax absSynFactory) {
		
	if (lst.getConst() != null)
	    {
		return convert(lst.getConst(),absSynFactory);
	    }
	else if (lst.getVar() != null)
	    {
		return convert(lst.getVar(),absSynFactory);
	    }
	else if (lst.getExpr() != null)
	    {
		return convert(lst.getExpr(),absSynFactory);
	    }
	else if (lst.getList() != null)
	    {
		return convert(lst.getList(),absSynFactory);
	    }
	else if (lst.getExternal() != null)
	    {
		return convert(lst.getExternal(),absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.Rest.");
	    

    } // convert(rif.bld.parser.jaxb.Rest lst,AbstractSyntax absSynFactory)




    private 
	AbstractSyntax.ExternalTerm
	convert(ExternalTERMType ext,AbstractSyntax absSynFactory) {

	return absSynFactory.
	    createExternalTerm(convert(ext.getContent().getExpr(),
				       absSynFactory));

    } 


    
    private 
	LinkedList<AbstractSyntax.Term>
	convert(Args args,AbstractSyntax absSynFactory) {
       
	assert args.getOrdered().equals("yes");

	LinkedList<AbstractSyntax.Term> result =	
	    new LinkedList<AbstractSyntax.Term>();

	for (java.lang.Object obj : args.getTERM())
	    {
		if (obj instanceof rif.bld.parser.jaxb.Expr)
		    {				
			result.addLast(convert((rif.bld.parser.jaxb.Expr)obj,
					       absSynFactory));
		    }
		else if (obj instanceof rif.bld.parser.jaxb.List)
		    {
			result.addLast(convert((rif.bld.parser.jaxb.List)obj,
					       absSynFactory));
		    }
		else if (obj instanceof rif.bld.parser.jaxb.Var)
		    {
			result.addLast(convert((rif.bld.parser.jaxb.Var)obj,
					       absSynFactory));
		    }
		else if (obj instanceof rif.bld.parser.jaxb.ExternalTERMType)
		    {
			result.addLast(convert((rif.bld.parser.jaxb.ExternalTERMType)obj,
					       absSynFactory));
		    }
		else if (obj instanceof rif.bld.parser.jaxb.Const)
		    {
			result.addLast(convert((rif.bld.parser.jaxb.Const)obj,
					       absSynFactory));
		    }
		else 
		    throw new Error("Bad instance of rif.bld.parser.jaxb.Args.");
		
	    }; // for (java.lang.Object obj : args.getTERM())
	

	return result;

    } // convert(Args args,AbstractSyntax absSynFactory)

    
    private 
	LinkedList<AbstractSyntax.ArgumentSlot>
	convertArgSlots(java.util.List<SlotUNITERMType> slots,
			AbstractSyntax absSynFactory) {
	
	LinkedList<AbstractSyntax.ArgumentSlot> result =
	    new LinkedList<AbstractSyntax.ArgumentSlot>();
	
	for (SlotUNITERMType slot : slots)
	    {
		String name = slot.getName();
		AbstractSyntax.Term value;
		
		if (slot.getConst() != null)
		    {
			value = convert(slot.getConst(),absSynFactory);
		    }
		else if (slot.getVar() != null)
		    {
			value = convert(slot.getVar(),absSynFactory);
		    }
		else if (slot.getExpr() != null)
		    {
			value = convert(slot.getExpr(),absSynFactory);
		    }
		else if (slot.getList() != null)
		    {
			value = convert(slot.getList(),absSynFactory);
		    }
		else if (slot.getExternal() != null)
		    {
			value = convert(slot.getExternal(),absSynFactory);
		    }
		else
		    throw new Error("Bad instance of rif.bld.parser.jaxb.SlotUNITERMType");
	    
		    
		result.
		    addLast(absSynFactory.createArgumentSlot(name,value));
	    }; // for (SlotUNITERMType slot : slots)

	return result;	

    } // convertArgSlots(java.util.List<SlotUNITERMType> slots,AbstractSyntax absSynFactory)
	
    
    private 
	LinkedList<AbstractSyntax.FrameSlot>
	convertFrameSlots(java.util.List<SlotFrameType> slots,
			  AbstractSyntax absSynFactory) {
	
	LinkedList<AbstractSyntax.FrameSlot> result = 
	    new LinkedList<AbstractSyntax.FrameSlot>();
	
	for (SlotFrameType slot : slots)
	    result.addLast(convert(slot,absSynFactory));
	
	return result;

    } // convertFrameSlots(java.util.List<SlotFrameType> slots,AbstractSyntax absSynFactory)


    private
	AbstractSyntax.FrameSlot
	convert(SlotFrameType slot,AbstractSyntax absSynFactory) {

	assert slot.getContent().size() == 2;

	AbstractSyntax.Term method;

	if (slot.getContent().get(0) instanceof ExternalTERMType)
	    {
		method = convert((ExternalTERMType)slot.getContent().get(0),
				 absSynFactory);
	    }
	else if (slot.getContent().get(0) instanceof Var)
	    {
		method = convert((Var)slot.getContent().get(0),
				 absSynFactory);
	    }
	else if (slot.getContent().get(0) instanceof Expr)
	    {
		method = convert((Expr)slot.getContent().get(0),
				 absSynFactory);
	    }
	else if (slot.getContent().get(0) instanceof Const)
	    {
		method = convert((Const)slot.getContent().get(0),
				 absSynFactory);
	    }
	else if (slot.getContent().get(0) instanceof rif.bld.parser.jaxb.List)
	    {
		method = convert((rif.bld.parser.jaxb.List)slot.getContent().get(0),
				 absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.SlotFrameType");
	    


	AbstractSyntax.Term value;
	
	
	if (slot.getContent().get(1) instanceof ExternalTERMType)
	    {
		value = convert((ExternalTERMType)slot.getContent().get(1),
				absSynFactory);
	    }
	else if (slot.getContent().get(1) instanceof Var)
	    {
		value = convert((Var)slot.getContent().get(1),
				absSynFactory);
	    }
	else if (slot.getContent().get(1) instanceof Expr)
	    {
		value = convert((Expr)slot.getContent().get(1),
				absSynFactory);
	    }
	else if (slot.getContent().get(1) instanceof Const)
	    {
		value = convert((Const)slot.getContent().get(1),
				absSynFactory);
	    }
	else if (slot.getContent().get(1) instanceof rif.bld.parser.jaxb.List)
	    {
		value = convert((rif.bld.parser.jaxb.List)slot.getContent().get(1),
				absSynFactory);
	    }
	else
	    throw new Error("Bad instance of rif.bld.parser.jaxb.SlotFrameType");
	    
	return 
	    absSynFactory.createFrameSlot(method,value);

    } // convert(SlotFrameType slot,AbstractSyntax absSynFactory) 





    //                   Data:

    private Unmarshaller _unmarshaller;

} // class Parser
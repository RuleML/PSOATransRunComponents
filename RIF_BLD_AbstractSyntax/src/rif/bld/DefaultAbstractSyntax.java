/* 
 * This software is a part of an abstract syntax for the BLD dialect 
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

package rif.bld;

import java.util.*;


/** Simple default implementation of {@link AbstractSyntax}.
 *  @author Alexandre Riazanov
 *  @since Jul 22, 2009
 */
public class DefaultAbstractSyntax implements AbstractSyntax {


    public static class Document implements AbstractSyntax.Document {

	public Document(AbstractSyntax.Group group) {
	    _group = group;
	}

	public AbstractSyntax.Group getGroup() {
	    return _group;
	} 

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + "Document";
	    if (_group == null) return result + " ()";
		
	    result += "\n" + indent + " (" + 
                "\n" + _group.toStringInPresSyntax(indent + "  ") + 
		"\n" + indent + " )";

	    return result;
	}
	
	private final AbstractSyntax.Group _group; 

    } // class Document



    public static class Group implements AbstractSyntax.Group {
	
	/** @param sentences may be null */
	public 
	    Group(Iterable<? extends AbstractSyntax.Sentence> sentences) {
	    _sentences = new LinkedList<AbstractSyntax.Sentence>();
	    if (sentences != null)
		for (AbstractSyntax.Sentence sent : sentences)
		    _sentences.addLast(sent);
	}
	
	public Collection<? extends AbstractSyntax.Sentence> getSentences() {
	    return _sentences;
	}
	
	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + "Group";
	    if (_sentences.isEmpty()) return result + " ()";
		
	    result += "\n" + indent + " (\n";

	    for (AbstractSyntax.Sentence s : _sentences) 
		result += indent + "\n" + s.toStringInPresSyntax(indent + "  ") + "\n";

	    result += "\n" + indent + " )";

	    return result;
	}

	private LinkedList<AbstractSyntax.Sentence> _sentences;
	
    } // class Group



    public static class Sentence implements AbstractSyntax.Sentence {
	
	/** @param rule must be nonnull */
	public Sentence(AbstractSyntax.Rule rule) {
	    assert rule != null;
	    _content = rule; 
	}

	/** @param group must be nonnull */
	public Sentence(AbstractSyntax.Group group) {
	    assert group != null;
	    _content = group; 
	}

	public boolean isRule() {
	    return _content instanceof AbstractSyntax.Rule;
	}
	
	/** <b>pre:</b> isRule() */
	public AbstractSyntax.Rule asRule() {
	    assert isRule();
	    return (AbstractSyntax.Rule)_content;
	}

	public boolean isGroup() {
	    return _content instanceof AbstractSyntax.Group;
	}

	/** <b>pre:</b> isGroup() */
	public AbstractSyntax.Group asGroup() {
	    assert isGroup();
	    return (AbstractSyntax.Group)_content;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    if (isRule()) 
		return asRule().toStringInPresSyntax(indent);
	    assert isGroup();
	    return asGroup().toStringInPresSyntax(indent);
	}

	private Object _content;

    } // class Sentence


    public static class Rule implements AbstractSyntax.Rule {
	
	
	/** @param variables may be null 
	 *  @param matrix must be nonnull 
	 */
	public Rule(Iterable<AbstractSyntax.Var> variables,AbstractSyntax.Clause matrix) {
	    _variables = new LinkedList<AbstractSyntax.Var>();
	    if (variables != null)
		for (AbstractSyntax.Var var : variables)	
		    _variables.addLast(var);
	    _clause = matrix;
	}

	public Collection<AbstractSyntax.Var> getVariables() {
	    return _variables;
	}

	public AbstractSyntax.Clause getClause() {
	    return _clause;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    
	    Set<AbstractSyntax.Var> vars = getClause().variables();
	    if (!vars.isEmpty())
		{
		    String result = indent + "Forall ";
		    for (AbstractSyntax.Var v : vars) 
			result += v.toStringInPresSyntax("") + " ";
		    result += "\n" + indent + " (\n" + 
			getClause().toStringInPresSyntax(indent + "   ") + 
                        "\n" + indent + " )";
		    return result;
		}
	    else
		return getClause().toStringInPresSyntax(indent);

	} // toStringInPresSyntax(String indent)


	private LinkedList<AbstractSyntax.Var> _variables;
	
	private AbstractSyntax.Clause _clause;

    } // class Rule


    public static class Clause implements AbstractSyntax.Clause {
	
	/** @param head nonempty sequence
	 *  @param body may be null
	 */
	public Clause(Iterable<? extends AbstractSyntax.Atomic> head,
		      AbstractSyntax.Formula body) 
	{
	    assert head != null;
	    assert head.iterator().hasNext();
	    _head = new LinkedList();
	    for (AbstractSyntax.Atomic at : head)
		_head.addLast(at);
	    _body = body;
	}
	
	public Collection<? extends AbstractSyntax.Atomic> getHead() {
	    return _head;
	}
	
	public AbstractSyntax.Formula getBody()  {
	    return _body;
	}

	/** The set of all free variables in the clause. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
	    if (_body != null) result.addAll(_body.variables());
	    for (AbstractSyntax.Atomic a : _head) 
		result.addAll(a.variables());
	    return result;
	}


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    
	    String result = "";
	    
	    if (_head.size() > 1)
		{
		    result += indent + "And\n" + indent + " (";
	    
		    for (AbstractSyntax.Atomic a : _head)
			result += "\n" + indent + a.toStringInPresSyntax(indent + "  ");
		    
		    result += "\n" + indent + " )";
		}
	    else
		{
		    assert _head.size() == 1;
		    result += _head.get(0).toStringInPresSyntax(indent);
		};



	    if (_body != null)
		{
		    result += " :-\n" + 
			_body.toStringInPresSyntax(indent + "  ");
		};
	    return result;

	} // toStringInPresSyntax(String indent)

	private LinkedList<AbstractSyntax.Atomic> _head;

	private AbstractSyntax.Formula _body;

    } // class Clause
	


    public static abstract class Formula implements AbstractSyntax.Formula {
	       
	/** <b>pre:</b> <code>this instanceof And</code> */
	public AbstractSyntax.And asAnd() {
	    assert this instanceof And;
	    return (And)this;
	}
	
	/** <b>pre:</b> <code>this instanceof Or</code> */
	public Or asOr() {
	    assert this instanceof Or;
	    return (Or)this;
	}

	/** <b>pre:</b> <code>this instanceof Exists</code> */
	public Exists asExists() {
	    assert this instanceof Exists;
	    return (Exists)this;
	}

	/** <b>pre:</b> <code>this instanceof Atomic</code> */
	public Atomic asAtomic() {
	    assert this instanceof Atomic;
	    return (Atomic)this;
	}
	
	/** <b>pre:</b> <code>this instanceof External</code> */
	public ExternalFormula asExternal() {
	    assert this instanceof ExternalFormula;
	    return (ExternalFormula)this;
	}	

	/** The set of all free variables in the formula. */
	public abstract Set<AbstractSyntax.Var> variables();

	/** Rendering in presentation syntax. */
	public abstract String toStringInPresSyntax(String indent);
		
    } // class Formula
	

    public static class And extends Formula implements AbstractSyntax.And {

	/** @param formulas can be null or an empty sequence */
	public And(Iterable<? extends AbstractSyntax.Formula> formulas) {
	    _formulas = new LinkedList<AbstractSyntax.Formula>();
	    if (formulas != null)
		for (AbstractSyntax.Formula form : formulas)	
		    _formulas.addLast(form);
	}

	public Collection<? extends AbstractSyntax.Formula> getFormulas() {
	    return _formulas;
	}
	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.Formula f : _formulas)
		result.addAll(f.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + "And\n" + indent + " (";
	    
	    for (AbstractSyntax.Formula f : _formulas)
		result += "\n" + f.toStringInPresSyntax(indent + "  ");

	    result += "\n" + indent + " )";

	    return result;
	}		


	private LinkedList<AbstractSyntax.Formula> _formulas;
	
    } // class And


    public static class Or extends Formula implements AbstractSyntax.Or {

	/** @param formulas can be null or an empty sequence */
	public Or(Iterable<? extends AbstractSyntax.Formula> formulas) {
	    _formulas = new LinkedList<AbstractSyntax.Formula>();
	    if (formulas != null)
		for (AbstractSyntax.Formula form : formulas)	
		    _formulas.addLast(form);
	}

	public Collection<? extends AbstractSyntax.Formula> getFormulas() {
	    return _formulas;
	}
	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.Formula f : _formulas)
		result.addAll(f.variables());
	    return result;
	}



	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + "Or\n" + indent + " (";
	    
	    for (AbstractSyntax.Formula f : _formulas)
		result += "\n" + indent + f.toStringInPresSyntax(indent + "  ");

	    result += "\n" + indent + " )";

	    return result;
	}		

	private LinkedList<AbstractSyntax.Formula> _formulas;

    } // class Or


    public static class Exists extends Formula implements AbstractSyntax.Exists {

	/** @param variables must be nonempty				
	 *  @param matrix			
	 */
	public Exists(Iterable<AbstractSyntax.Var> variables,AbstractSyntax.Formula matrix) {
	    assert variables != null;
	    assert variables.iterator().hasNext();
	    _variables = new LinkedList<AbstractSyntax.Var>();
	    for (AbstractSyntax.Var var : variables)	
		_variables.addLast(var);
	    _formula = matrix;
	}
	
	
	/** @return non-empty sequence */
	public Collection<AbstractSyntax.Var> getVariables() {
	    return _variables;
	}
	
	public AbstractSyntax.Formula getFormula() {
	    return _formula;
	}
	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    Set<AbstractSyntax.Var> result = 
		_formula.variables();
	    result.removeAll(_variables);
	    return result;
	}


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + "Exist ";
	    for (AbstractSyntax.Var v : _variables) 
		result += v.getName() + " ";
	    result += "\n" + indent + " (\n" + 
		_formula.toStringInPresSyntax(indent + "   ") + 
		"\n" + indent + " )";
	    return result;
	}

	private LinkedList<AbstractSyntax.Var> _variables;

	private AbstractSyntax.Formula  _formula;

    } // class Exists


    public static abstract class Atomic 
	extends Formula 
	implements AbstractSyntax.Atomic {

	public AbstractSyntax.Atom asAtom() {
	    return (AbstractSyntax.Atom)this;
	}

	public AbstractSyntax.Equal asEqual() {
	    return (AbstractSyntax.Equal)this;
	}

	public AbstractSyntax.Member asMember() {
	    return (AbstractSyntax.Member)this;
	}

	public AbstractSyntax.Subclass asSubclass() {
	    return (AbstractSyntax.Subclass)this;
	}

	public AbstractSyntax.Frame asFrame() {
	    return (AbstractSyntax.Frame)this;
	}
	
	/** The set of all free variables in the formula. */
	public abstract Set<AbstractSyntax.Var> variables();


	/** Rendering in presentation syntax. */
	public abstract String toStringInPresSyntax(String indent);
	
    } // class Atomic





    public static class ExternalFormula 
	extends Formula 
	implements AbstractSyntax.ExternalFormula {
	
	/** @param content must be nonnull */
	public ExternalFormula(AbstractSyntax.Atomic content) {
	    _content = content;
	}
	
	public AbstractSyntax.Atomic getContent() {
	    return _content;
	}
	
	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    return _content.variables();
	}


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + "External(" + _content + ")";
	}


	private AbstractSyntax.Atomic _content;
	
    } // class ExternalFormula


    public static abstract class Atom 
	extends Atomic 
	implements AbstractSyntax.Atom {
	
	/** @param predicate must be nonnull */
	protected Atom(AbstractSyntax.Const predicate) {
	    assert predicate != null;
	    _predicate = predicate;
	}
	
	public AbstractSyntax.Const getPredicate() {
	    return _predicate;
	}

	public AbstractSyntax.PositionalAtom asPositionalAtom() {
	    return (AbstractSyntax.PositionalAtom)this;
	}

	public AbstractSyntax.SlottedAtom asSlottedAtom() {
	    return (AbstractSyntax.SlottedAtom)this;
	}
	
	
	/** The set of all free variables in the formula. */
	public abstract Set<AbstractSyntax.Var> variables();


	/** Rendering in presentation syntax. */
	public abstract String toStringInPresSyntax(String indent);


	protected AbstractSyntax.Const _predicate;

    } // class Atom


    public static class Equal 
	extends Atomic 
	implements AbstractSyntax.Equal {
	
	
	/** @param left must be nonnull
	 *  @param right must be nonnull
	 */
	public Equal(AbstractSyntax.Term left,AbstractSyntax.Term right) {
	    assert left != null;
	    assert right != null;
	    _left = left;
	    _right = right;
	}

 	public AbstractSyntax.Term getLeft() {
	    return _left;
	}

	public AbstractSyntax.Term getRight() {
	    return _right;
	}

	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    Set<AbstractSyntax.Var> result = _left.variables();
	    result.addAll(_right.variables());
	    return result;
	}


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + _left.toStringInPresSyntax("") + " = " + 
		_right.toStringInPresSyntax("");
	}


	private AbstractSyntax.Term _left;

	private AbstractSyntax.Term _right;
	
    } // class Equal



    public static class Member 
	extends Atomic 
	implements AbstractSyntax.Member {

	/** @param instance must be nonnull
	 *  @param classExpr must be nonnull
	 */
	public Member(AbstractSyntax.Term instance,
		      AbstractSyntax.Term classExpr) {
	    assert instance != null;
	    assert classExpr != null;
	    _instance = instance;
	    _classExpr = classExpr;
	}

	public AbstractSyntax.Term getInstance() {
	    return _instance;
	}

	public AbstractSyntax.Term getClassExpr() {
	    return _classExpr;
	}

	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    Set<AbstractSyntax.Var> result = _instance.variables();
	    result.addAll(_classExpr.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + _instance.toStringInPresSyntax("") + " # " + 
		_classExpr.toStringInPresSyntax("");
	}

	private AbstractSyntax.Term _instance;

	private AbstractSyntax.Term _classExpr;

    } // class Member


    public static class Subclass
	extends Atomic 
	implements AbstractSyntax.Subclass {
	
	/** @param subclass must be nonnull
	 *  @param superclass must be nonnull
	 */
	public Subclass(AbstractSyntax.Term subclass,
			AbstractSyntax.Term superclass) {
	    assert subclass != null;
	    assert superclass != null;
	    _subclass = subclass;
	    _superclass = superclass;
	}

	public AbstractSyntax.Term getSubclass() {
	    return _subclass;
	}

	public AbstractSyntax.Term getSuperclass() {
	    return _superclass;
	}

	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    Set<AbstractSyntax.Var> result = _subclass.variables();
	    result.addAll(_superclass.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + _subclass.toStringInPresSyntax("") + " ## " + 
		_superclass.toStringInPresSyntax("");
	}


	private AbstractSyntax.Term _subclass;

	private AbstractSyntax.Term _superclass;

    } // class Subclass


    public static class Frame 
	extends Atomic 
	implements AbstractSyntax.Frame {

	/** @param object must be nonnull
	 *  @param slots may be null or an empty sequence
	 */
	public Frame(AbstractSyntax.Term object,
		     Iterable<? extends AbstractSyntax.FrameSlot> slots) {
	    assert object != null;
	    
	    _object = object;
	    _slots = new LinkedList<AbstractSyntax.FrameSlot>();
	    if (slots != null)
		for (AbstractSyntax.FrameSlot slot : slots)	
		    _slots.addLast(slot);
	}

	public AbstractSyntax.Term getObject() {
	    return _object;
	}

	public Collection<? extends AbstractSyntax.FrameSlot> getSlots() {
	    return _slots;
	}
	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    HashSet<AbstractSyntax.Var> result = 
		new HashSet<AbstractSyntax.Var>();
	    
	    if (_slots != null)
		for (AbstractSyntax.FrameSlot slot : _slots)
		    result.addAll(slot.variables());

	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = _object.toStringInPresSyntax(indent) + "[ ";
	    
	    
	    if (_slots != null)
		for (AbstractSyntax.FrameSlot slot : _slots)
		    result += slot.toStringInPresSyntax("") + " ";

	    return result + "]";
	}

	
	private AbstractSyntax.Term _object;

	private LinkedList<AbstractSyntax.FrameSlot> _slots;

    } // class Frame



    public static class PositionalAtom 
	extends Atom 
	implements AbstractSyntax.PositionalAtom {

	/** @param predicate must be nonnull
	 *  @param arguments must be a nonempty sequence
	 */
	public PositionalAtom(AbstractSyntax.Const predicate,
			      Iterable<? extends AbstractSyntax.Term> arguments) {
	    super(predicate);
	    assert arguments != null;
	    assert arguments.iterator().hasNext();
	    _arguments = new LinkedList<AbstractSyntax.Term>();
	    for (AbstractSyntax.Term arg : arguments) 
		_arguments.addLast(arg);
	}

	public Collection<? extends AbstractSyntax.Term> getArguments() {
	    return _arguments;
	}

	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.Term arg : _arguments)
		result.addAll(arg.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = 
		indent + _predicate.toStringInPresSyntax("") + " ( ";

	    for (AbstractSyntax.Term arg : _arguments)
		result += arg.toStringInPresSyntax("") + " ";

	    return result + ")";
	}

	private LinkedList<AbstractSyntax.Term> _arguments;
	
    } // class PositionalAtom


    public static class SlottedAtom
	extends Atom 
	implements AbstractSyntax.SlottedAtom {

        /** @param predicate must be nonnull
	 *  @param slots may be null or an empty sequence
	 */
	public SlottedAtom(AbstractSyntax.Const predicate,
			   Iterable<? extends AbstractSyntax.ArgumentSlot> slots) {
	    super(predicate);
	    
	    TreeSet<AbstractSyntax.ArgumentSlot> sortedSlots = 
		new TreeSet<AbstractSyntax.ArgumentSlot>();

	    if (slots != null)
		for (AbstractSyntax.ArgumentSlot slot : slots)
		    sortedSlots.add(slot);

	    _slots = new LinkedList<AbstractSyntax.ArgumentSlot>(sortedSlots);
	}


	public Collection<? extends AbstractSyntax.ArgumentSlot> getSlots() {
	    return _slots;
	}

	
	/** The set of all free variables in the formula. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.ArgumentSlot slot : _slots)
		result.addAll(slot.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = 
		indent + _predicate.toStringInPresSyntax("") + " ( ";

	    for (AbstractSyntax.ArgumentSlot slot : _slots)
		result += slot.toStringInPresSyntax("") + " ";

	    return result + ")";
	}

	private LinkedList<AbstractSyntax.ArgumentSlot> _slots;

    } // class SlottedAtom
    
    


    public static class ArgumentSlot implements AbstractSyntax.ArgumentSlot {

	/** @param name must be nonnull
	 *  @param value must be nonnull
	 */
	public ArgumentSlot(String name,AbstractSyntax.Term value) {
	    assert name != null;
	    assert value != null;
	    _name = name;
	    _value = value;
	}
	
	public String getName() {
	    return _name;
	}
	
	public AbstractSyntax.Term getValue() {
	    return _value;
	}

	public int compareTo(AbstractSyntax.ArgumentSlot s) {
	    int cmp = getName().compareTo(s.getName());
	    if (cmp != 0) return cmp;
	    return getValue().compareTo(s.getValue());
	}

	
	/** The set of all free variables in the slot. */
	public Set<AbstractSyntax.Var> variables() {
	    return _value.variables();
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + _name + " -> " + _value;
	}

       

	private String _name;
	
	private AbstractSyntax.Term _value;

    } // class ArgumentSlot
    


    public static class FrameSlot implements AbstractSyntax.FrameSlot {

	/** @param method must be nonnull
	 *  @param value must be nonnull
	 */
	public FrameSlot(AbstractSyntax.Term method,AbstractSyntax.Term value) {
	    assert method != null;
	    assert value != null;
	    _method = method;
	    _value = value;
	}
	
	public AbstractSyntax.Term getMethod() {
	    return _method;
	}
	
	public AbstractSyntax.Term getValue() {
	    return _value;
	}


	public int compareTo(AbstractSyntax.FrameSlot s) {
	    int cmp = getMethod().compareTo(s.getMethod());
	    if (cmp != 0) return cmp;
	    return getValue().compareTo(s.getValue());
	}

	
	/** The set of all free variables in the slot. */
	public Set<AbstractSyntax.Var> variables() {
	    Set<AbstractSyntax.Var> result = 
		_method.variables();
	    result.addAll(_value.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return _method.toStringInPresSyntax(indent) + " -> " + _value;
	}

	
	private AbstractSyntax.Term _method;
	
	private AbstractSyntax.Term _value;
		
    } // class FrameSlot
    

    
    public static abstract class Term implements AbstractSyntax.Term {
	
	public AbstractSyntax.Const asConst() {
	    return (AbstractSyntax.Const)this;
	}
	
	public AbstractSyntax.Var asVar() {
	    return (AbstractSyntax.Var)this;
	}
	
	public AbstractSyntax.Expr asExpr() {
	    return (AbstractSyntax.Expr)this;
	}
	
	public AbstractSyntax.List asList() {
	    return (AbstractSyntax.List)this;
	}
	
	public AbstractSyntax.ExternalTerm asExternal() {
	    return (AbstractSyntax.ExternalTerm)this;
	}
	
	public abstract int compareTo(AbstractSyntax.Term t);

	/** The set of all free variables in the term. */
	public abstract Set<AbstractSyntax.Var> variables();


	/** Rendering in presentation syntax. */
	public abstract String toStringInPresSyntax(String indent);

    } // class Term




    public static class Const extends Term implements AbstractSyntax.Const {
	
	// TODO: Syntax checks in the content, type and language tag;
	// possibly, escapes in output.
       


	/** @param content must be nonnull
	 *  @param type must be nonnull
	 *  @param lang may be null
	 */
	public Const(String content,String type,String lang) {
	    assert content != null;
	    assert type != null;
	    _content = content;			
	    _type = type;
	    _lang = null;
	}

	public String getContent() {
	    return _content;
	}
  
	public String getType() {
	    return _type;
	}	

	public String getLang() {
	    return _lang;
	}	

	public int compareTo(AbstractSyntax.Term t) {
	    if (t instanceof AbstractSyntax.Const)
		{
		    int cmp = getContent().compareTo(t.asConst().getContent());
		    if (cmp != 0) return cmp;
		    cmp = getType().compareTo(t.asConst().getType());
		    if (cmp != 0) return cmp;
		    if (getLang() == null)
			{
			    if (t.asConst().getLang() == null)
				return 0;
			    return -1;
			}
		    else if (t.asConst().getLang() == null)
			{
			    return 1;
			}
		    else 
			{
			    return getLang().compareTo(t.asConst().getLang());
			}
		}
	    else
		return -1; // constants are smaller than anything else

	} // compareTo(AbstractSyntax.Term t)

	
	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    return new TreeSet<AbstractSyntax.Var>();
	}


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    if (_lang == null || _lang.equals(""))
		return 
		    indent + "\"" + _content + "\"" + "^^" + _type;
		
	    return 
		indent + "\"" + _content + "@" + _lang + "\"" + "^^" + _type;
	}

	private String _content;

	private String _type;

	private String _lang;

    } // class Const




    public static class Var extends Term implements AbstractSyntax.Var {
	
	/** @param name must be nonnull */
	public Var(String name) {
	    assert name != null;
	    _name = name;
	}
	
	
	public String getName() {
	    return _name;
	}	
	
	public int compareTo(AbstractSyntax.Term t) {
	    if (t instanceof AbstractSyntax.Const)
		{
		    return 1; 
		}
	    else if (t instanceof AbstractSyntax.Var) 
		{
		    return getName().compareTo(t.asVar().getName());
		}
	    else 
		return -1;

	} // compareTo(AbstractSyntax.Term t)


	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    result.add(this);
	    return result;
	}

	
	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return indent + "?" + _name;
	}

	private String _name;

    } // class Var



    public static abstract class Expr extends Term implements AbstractSyntax.Expr {

	/** @param function must be nonnull */ 
	protected Expr(AbstractSyntax.Const function) {
	    assert function != null;
	    _function = function;
	}

	public AbstractSyntax.Const getFunction() {
	    return _function;
	}	 

	public AbstractSyntax.PositionalExpr asPositionalExpr() {
	    return (AbstractSyntax.PositionalExpr)this;
	}	
	
	public AbstractSyntax.SlottedExpr asSlottedExpr() {
	    return (AbstractSyntax.SlottedExpr)this;
	}	

	
	/** The set of all free variables in the term. */
	public abstract Set<AbstractSyntax.Var> variables();

	/** Rendering in presentation syntax. */
	public abstract String toStringInPresSyntax(String indent);

	protected AbstractSyntax.Const _function;

    } // class Expr

    
    public static class PositionalExpr 
	extends Expr 
	implements AbstractSyntax.PositionalExpr {

	/** @param function must be nonnull
	 *  @param arguments must be a nonempty sequence
	 */
	public PositionalExpr(AbstractSyntax.Const function,
			      Iterable<? extends AbstractSyntax.Term> arguments) {
	    super(function);
	    assert arguments != null;
	    assert arguments.iterator().hasNext();
	    _arguments = new LinkedList<AbstractSyntax.Term>();
	    for (AbstractSyntax.Term arg : arguments) 
		_arguments.addLast(arg);
	}

	public Collection<? extends AbstractSyntax.Term> getArguments() {
	    return _arguments;
	}	
	   
	
	public int compareTo(AbstractSyntax.Term t) {

	    if (t instanceof AbstractSyntax.Const || 
		t instanceof AbstractSyntax.Var) 
		{
		    return 1;
		}
	    else if (t instanceof AbstractSyntax.PositionalExpr)
		{
		    // Compare the functions first:
		    int cmp = 
			getFunction().
			compareTo(t.asExpr().getFunction());
		    if (cmp != 0) return cmp;

		    // Compare the arities:
		    cmp = 
			getArguments().size() -
			t.asExpr().asPositionalExpr().getArguments().size();
		    if (cmp != 0) return cmp;
		    
		    // Compare the arguments lexicographically:

		    Iterator<? extends AbstractSyntax.Term> iter1 = 
			getArguments().iterator();
		    Iterator<? extends AbstractSyntax.Term> iter2 = 
			t.asExpr().asPositionalExpr().getArguments().iterator();

		    while (iter1.hasNext())
			{
			    cmp = iter1.next().compareTo(iter2.next());
			    if (cmp != 0) return cmp;
			};

		    assert !iter2.hasNext();
		    
		    return 0;
		}
	    else		
		return -1;

	} // compareTo(AbstractSyntax.Term t)

	
	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.Term arg : _arguments)
		result.addAll(arg.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + _function.toStringInPresSyntax("") + " ( ";
	    for (AbstractSyntax.Term arg : _arguments)
		result += arg.toStringInPresSyntax("") + " ";
	    return result + ")";
	}

	private LinkedList<AbstractSyntax.Term> _arguments;

    } // class PositionalExpr



    public static class SlottedExpr 
	extends Expr 
	implements AbstractSyntax.SlottedExpr {
    
	/** @param function must be nonnull
	 *  @param slots may be null or an empty sequence
	 */
	public SlottedExpr(AbstractSyntax.Const function,
			   Iterable<? extends AbstractSyntax.ArgumentSlot> slots) {
	    super(function);
	    _slots = new LinkedList<AbstractSyntax.ArgumentSlot>();
	    if (slots != null)
		for (AbstractSyntax.ArgumentSlot slot : slots)
		    _slots.addLast(slot);
	}
	    

	public Collection<? extends AbstractSyntax.ArgumentSlot> getSlots() {
	    return _slots;
	}	

	public int compareTo(AbstractSyntax.Term t) {

	    if (t instanceof AbstractSyntax.Const || 
		t instanceof AbstractSyntax.Var ||
		t instanceof AbstractSyntax.PositionalExpr) 
		{
		    return 1;
		}
	    else if (t instanceof AbstractSyntax.SlottedExpr)
		{
		    // Compare the functions first:
		    int cmp = 
			getFunction().
			compareTo(t.asExpr().getFunction());
		    if (cmp != 0) return cmp;

		    // Compare the arities:
		    cmp = 
			getSlots().size() -
			t.asExpr().asSlottedExpr().getSlots().size();
		    if (cmp != 0) return cmp;
		    
		    // Compare the slots lexicographically.
		    // The slots must be ordered!

		    Iterator<? extends AbstractSyntax.ArgumentSlot> iter1 = 
			getSlots().iterator();
		    Iterator<? extends AbstractSyntax.ArgumentSlot> iter2 = 
			t.asExpr().asSlottedExpr().getSlots().iterator();

		    while (iter1.hasNext())
			{
			    cmp = iter1.next().compareTo(iter2.next());
			    if (cmp != 0) return cmp;
			};

		    assert !iter2.hasNext();
		    
		    return 0;
		}
	    else		
		return -1;

	} // compareTo(AbstractSyntax.Term t)



	
	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    for (AbstractSyntax.ArgumentSlot slot : _slots)
		result.addAll(slot.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    String result = indent + _function.toStringInPresSyntax("") + " ( ";
	    for (AbstractSyntax.ArgumentSlot slot : _slots)
		result += slot.toStringInPresSyntax("") + " ";
	    return result + ")";
	}

	private LinkedList<AbstractSyntax.ArgumentSlot> _slots;

    } // class SlottedExpr 



    public static class List extends Term implements AbstractSyntax.List {

	/** Constructs an empty list. */
	private List() {
	    _head = null;
	}
	
	/** The only available empty list. */
	public static final List EMPTY = new List();

	/** @param head must be nonnull
	 *  @param tail must be nonnull
	 */
	public List(AbstractSyntax.Term head,AbstractSyntax.Term tail) {
	    assert head != null;
	    assert tail != null;
	    _head = head;
	    _tail = tail;
	}

	public boolean isEmpty() {
	    return _head == null;
	}	

	/** <b>pre:</b> <code>!isEmpty()</code> */
	public AbstractSyntax.Term getHead() {
	    assert !isEmpty();
	    return _head;
	}	
	
	/** <b>pre:</b> <code>!isEmpty()</code>
	 *  @return nonnull 
	 */
	public AbstractSyntax.Term getTail() {
	    assert !isEmpty();
	    return _tail;
	}	
	
	public int compareTo(AbstractSyntax.Term t) {

	    if (t instanceof AbstractSyntax.Const || 
		t instanceof AbstractSyntax.Var ||
		t instanceof AbstractSyntax.PositionalExpr ||
		t instanceof AbstractSyntax.SlottedExpr) 
		{
		    return 1;
		}
	    else if (t instanceof AbstractSyntax.List)
		{
		    if (isEmpty())
			{
			    if (t.asList().isEmpty()) return 0;
			    return -1;
			}
		    else if (t.asList().isEmpty())
			{
			    return 1;
			}
		    else 
			{
			    int cmp = getHead().compareTo(t.asList().getHead());
			    if (cmp != 0) return cmp;
			    
			    return getTail().compareTo(t.asList().getTail());
			}
		}
	    else
		return -1;
	} // compareTo(AbstractSyntax.Term t)

	
	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    TreeSet<AbstractSyntax.Var> result = 
		new TreeSet<AbstractSyntax.Var>();
	    if (isEmpty()) return result;
	    result.addAll(_head.variables());
	    result.addAll(_tail.variables());
	    return result;
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    
	    if (isEmpty()) return indent + "List()";
	    
	    LinkedList<AbstractSyntax.Term> elements = 
		new LinkedList<AbstractSyntax.Term>();
	    elements.addLast(_head);

	    AbstractSyntax.Term tail;

	    for (tail = _tail;
		 (tail instanceof AbstractSyntax.List) && 
		     !((AbstractSyntax.List)tail).isEmpty();
		 tail = ((AbstractSyntax.List)tail).getTail())
		{
		    elements.addLast(((AbstractSyntax.List)tail).getHead());
		};
		 
	    String result = indent + "List( ";
	    for (AbstractSyntax.Term el : elements)
		result += el.toStringInPresSyntax("") + " ";
	    
	    if (tail instanceof AbstractSyntax.List)
		{
		    assert ((AbstractSyntax.List)tail).isEmpty();
		    result += ")";
		}
	    else
		result += "| " + tail.toStringInPresSyntax("") + ")";

	    return result;
	} // toStringInPresSyntax(String indent)


	private AbstractSyntax.Term _head;

	private AbstractSyntax.Term _tail;


    } // class List



    public static class ExternalTerm extends Term implements AbstractSyntax.ExternalTerm {	

	/** @param content must be nonnull */
	public ExternalTerm(AbstractSyntax.Expr content) {
	    assert content != null;
	    _content = content;
	}


	public AbstractSyntax.Expr getContent() {
	    return _content;
	}	

	public int compareTo(AbstractSyntax.Term t) {

	    if (t instanceof AbstractSyntax.Const || 
		t instanceof AbstractSyntax.Var ||
		t instanceof AbstractSyntax.PositionalExpr ||
		t instanceof AbstractSyntax.SlottedExpr ||
		t instanceof AbstractSyntax.List) 
		{
		    return 1;
		}
	    else if (t instanceof AbstractSyntax.ExternalTerm)
		{
		    return getContent().compareTo(t.asExternal().getContent());
		}
	    else
		throw new Error("Bad instance of rif.bld.parser.AbstractSyntax.Term.");
	} // compareTo(AbstractSyntax.Term t)

	
	/** The set of all free variables in the term. */
	public Set<AbstractSyntax.Var> variables() {
	    return _content.variables();
	}

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent) {
	    return 
		indent + "External(" + _content.toStringInPresSyntax("") + ")";
	}

	private AbstractSyntax.Expr _content;

    } // class ExternalTerm









    public Document createDocument(AbstractSyntax.Group group) {
	return new Document(group);
    }

    public 
	Group 
	createGroup(Iterable<? extends AbstractSyntax.Sentence> sentences) {
	return new Group(sentences);
    }

    public Sentence createSentence(AbstractSyntax.Group group) {
	return new Sentence(group);
    }
    
    public Sentence createSentence(AbstractSyntax.Rule rule) {
	return new Sentence(rule);
    }
    
    
    public Rule createRule(Iterable<AbstractSyntax.Var> variables,
			   AbstractSyntax.Clause matrix) {
	return new Rule(variables,matrix);
    }

    public Clause createClause(Iterable<? extends AbstractSyntax.Atomic> head,
			       AbstractSyntax.Formula body) {
	return new Clause(head,body);
    }


    public And createAnd(Iterable<? extends AbstractSyntax.Formula> formulas) {
	return new And(formulas);
    }

    public Or createOr(Iterable<? extends AbstractSyntax.Formula> formulas) {
	return new Or(formulas);
    } 

    public 
	Exists 
	createExists(Iterable<AbstractSyntax.Var> variables,AbstractSyntax.Formula formula) {
	return new Exists(variables,formula);
    }
    
    public ExternalFormula createExternalFormula(AbstractSyntax.Atomic content) {
	return new ExternalFormula(content);
    }

    public Equal createEqual(AbstractSyntax.Term left,
			     AbstractSyntax.Term right) {
	return new Equal(left,right);
    }
    
    public Member createMember(AbstractSyntax.Term instance,
			       AbstractSyntax.Term classExpr) {
	return new Member(instance,classExpr);
    }

    public Subclass createSubclass(AbstractSyntax.Term subclass,
				   AbstractSyntax.Term superclass) {
	return new Subclass(subclass,superclass);
    }

    public					
	Frame
	createFrame(AbstractSyntax.Term object,
		    Iterable<? extends AbstractSyntax.FrameSlot> slots) {
	return new Frame(object,slots);
    }

    public
	PositionalAtom
	createPositionalAtom(AbstractSyntax.Const predicate,
			     Iterable<? extends AbstractSyntax.Term> arguments) {
	return new PositionalAtom(predicate,arguments);
    }

    
    public
	SlottedAtom
	createSlottedAtom(AbstractSyntax.Const predicate,
			  Iterable<? extends AbstractSyntax.ArgumentSlot> slots) {
	return new SlottedAtom(predicate,slots);
    }


    public ArgumentSlot createArgumentSlot(String name,
					   AbstractSyntax.Term value) {
	return new ArgumentSlot(name,value);
    }
    
    public FrameSlot createFrameSlot(AbstractSyntax.Term method,
				     AbstractSyntax.Term value) {
	return new FrameSlot(method,value);
    }


    public Const createConst(String content,String type,String lang) {
	return new Const(content,type,lang);
    }
    
    public Var createVar(String name) {
	return new Var(name);
    }

    public 
	PositionalExpr 
	createPositionalExpr(AbstractSyntax.Const function,
			     Iterable<? extends AbstractSyntax.Term> arguments) {
	return new PositionalExpr(function,arguments);
    }
    
    public 
	SlottedExpr
	createSlottedExpr(AbstractSyntax.Const function,
			  Iterable<? extends AbstractSyntax.ArgumentSlot> slots) {
	return new SlottedExpr(function,slots);
    }
			  
    public List createEmptyList() {
	return List.EMPTY;
    }

    public List createList(AbstractSyntax.Term head,
			   AbstractSyntax.Term tail) {
	return new List(head,tail);
    }

    public ExternalTerm createExternalTerm(AbstractSyntax.Expr content) {
	return new ExternalTerm(content);
    }


} // class DefaultAbstractSyntax
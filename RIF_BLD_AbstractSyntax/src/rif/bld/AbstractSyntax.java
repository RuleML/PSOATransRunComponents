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

/** Abstract syntax for RIF BLD. Most of the features declared
 *  here can be used, eg, by a RIF BLD parser to communicate the results 
 *  of parsing. The client code is supposed to provide implementations
 *  of all the interfaces declared here (for many uses the default
 *  implementation {@link DefaultAbstractSyntax}
 *  may be good enough).
 *
 *  This approach is taken to nicely isolate parsers' code
 *  from the environment. It makes parsers highly alienable. A new user 
 *  only has to provide an implementation of these interfaces in order to link
 *  a parser to his/her code. No matter how the data returned from 
 *  the parser is processed, no modification of the parser code is 
 *  required. The requirements imposed by the interface are quite
 *  liberal, so that writing modules glueing the parser with practically
 *  any application is easy.
 *
 *
 *  Instances of classes modeling this interface are factories of 
 *  abstract syntax objects for various syntaxtic categories.
 *
 *  Another use of such factories is to allow client code to generate RIF 
 *  documents. However, currently only rendering in presentation syntax is 
 *  supported.
 *  
 *
 *  TODO: rendering in XML; support for annotations.
 *
 *  @author Alexandre Riazanov
 *  @since Jan 06, 2009
 */
public interface AbstractSyntax {

    public interface Document {
	/** May be null. */
	public Group getGroup();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface Group {
	
	/** @return nonnull */
	public Collection<? extends Sentence> getSentences();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    } 

    public interface Sentence {
	
	public boolean isRule();
	
	/** <b>pre:</b> isRule() */
	public Rule asRule();

	public boolean isGroup();

	/** <b>pre:</b> isGroup() */
	public Group asGroup();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface Rule {
	
	/** @return nonnull */
	public Collection<Var> getVariables();

	/** @return nonnull */
	public Clause getClause(); 

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);	
    }
	
    
    public interface Clause {
	
	/** @return nonempty sequence */
	public Collection<? extends Atomic> getHead();
	
	/** @return possibly null, if there is no condition  */
	public Formula getBody();

	/** The set of all free variables in the clause. */
	public Set<Var> variables();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    
    public interface Formula {
	
	/** <b>pre:</b> <code>this instanceof And</code> */
	public And asAnd();
	
	/** <b>pre:</b> <code>this instanceof Or</code> */
	public Or asOr();

	/** <b>pre:</b> <code>this instanceof Exists</code> */
	public Exists asExists();

	/** <b>pre:</b> <code>this instanceof Atomic</code> */
	public Atomic asAtomic();
	
	/** <b>pre:</b> <code>this instanceof External</code> */
	public ExternalFormula asExternal();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface And extends Formula {
	
	/** @return nonnull */
	public Collection<? extends Formula> getFormulas();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }

    public interface Or extends Formula {

	/** @return nonnull */
	public Collection<? extends Formula> getFormulas();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface Exists extends Formula {

	/** @return non-empty sequence */
	public Collection<Var> getVariables();
	
	/** The matrix. */
	public Formula getFormula();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }


    public interface Atomic extends Formula {

	public Atom asAtom();

	public Equal asEqual();

	public Member asMember();

	public Subclass asSubclass();

	public Frame asFrame();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }


    public interface ExternalFormula extends Formula {
	
	/** @return nonnull */
	public Atomic getContent();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }


    public interface Atom extends Atomic {
	
	/** @return nonnull */
	public Const getPredicate();

	public PositionalAtom asPositionalAtom();

	public SlottedAtom asSlottedAtom();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }


    public interface Equal extends Atomic {
	
	/** @return nonnull */
	public Term getLeft();

	/** @return nonnull */
	public Term getRight();

	/** The set of all free variables in the formula. */
	public Set<Var> variables();

	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface Member extends Atomic {
	
	/** @return nonnull */
	public Term getInstance();

	/** @return nonnull */
	public Term getClassExpr();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface Subclass extends Atomic {
	
	/** @return nonnull */
	public Term getSubclass();

	/** @return nonnull */
	public Term getSuperclass();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface Frame extends Atomic {
	
	/** @return nonnull */
	public Term getObject();

	/** @return nonnull, possibly an empty sequence  */
	public Collection<? extends FrameSlot> getSlots();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }



    public interface PositionalAtom extends Atom {
	
	/** @return nonempty sequence */
	public Collection<? extends Term> getArguments();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }


    public interface SlottedAtom extends Atom {
    
	/** @return nonnull, possibly an empty sequence; 
	 *  the slots are ordered
	 */
	public Collection<? extends ArgumentSlot> getSlots();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }
    
    

    /** The ordering on slots must respect the ordering on names,
     *  ie, if <code>s1.getName().compareTo(s2.getName() > 0)</code>,
     *  then <code>s1.compareTo(s2 > 0)</code>.
     */
    public interface ArgumentSlot extends Comparable<ArgumentSlot> {

	/** @return nonnull */
	public String getName();
	
	/** @return nonnull */
	public Term getValue();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }
    

    public interface FrameSlot extends Comparable<FrameSlot> {

	/** @return nonnull */
	public Term getMethod();
	
	/** @return nonnull */
	public Term getValue();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }
    
    
    public interface Term extends Comparable<Term> {
	
	public Const asConst();
	
	public Var asVar();
	
	public Expr asExpr();
	
	public List asList();
	
	public ExternalTerm asExternal();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }

    public interface Const extends Term {

	/** @return nonnull */
	public String getContent();
  
	/** @return nonnull */
	public String getType();
	
	/** @return possibly null */
	public String getLang();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface Var extends Term {
	
	/** @return nonnull */
	public String getName();


	/** A set containing just this variable. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface Expr extends Term {
	
	/** @return nonnull */
	public Const getFunction();

	public PositionalExpr asPositionalExpr();
	
	public SlottedExpr asSlottedExpr();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface PositionalExpr extends Expr {
	
	/** @return nonempty sequence */
	public Collection<? extends Term> getArguments();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    public interface SlottedExpr extends Expr {
    
	/** @return nonnull, possibly an empty sequence */
	public Collection<? extends ArgumentSlot> getSlots();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);

    }

    

    public interface List extends Term {
	
	public boolean isEmpty();

	/** <b>pre:</b> <code>!isEmpty()</code> */
	public Term getHead();
	
	/** <b>pre:</b> <code>!isEmpty()</code>
	 *  @return nonnull 
	 */
	public Term getTail();


	/** The set of all free variables in the term. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
    }


    public interface ExternalTerm extends Term {

	/** @return nonnull */
	public Expr getContent();


	/** The set of all free variables in the formula. */
	public Set<Var> variables();


	/** Rendering in presentation syntax. */
	public String toStringInPresSyntax(String indent);
	
    }







    /** @param group may be null */
    public Document createDocument(Group group);

    /** @param sentences may be null */
    public Group createGroup(Iterable<? extends Sentence> sentences);

    /** @param group must be nonnull */
    public Sentence createSentence(Group group);
    
    /** @param rule must be nonnull */
    public Sentence createSentence(Rule rule);
    
    /** @param variables may be null 
     *  @param matrix must be nonnull 
     */
    public Rule createRule(Iterable<Var> variables,
			   Clause matrix);

    /** @param head nonempty sequence
     *  @param body may be null
     */
    public Clause createClause(Iterable<? extends Atomic> head,
			       Formula body);

    /** @param formulas may be null or an empty sequence */
    public And createAnd(Iterable<? extends Formula> formulas); 

    /** @param formulas may be null or an empty sequence */
    public Or createOr(Iterable<? extends Formula> formulas); 

    /** @param variables must be nonempty 
     *  @param formula the matrix
     */
    public 
	Exists 
	createExists(Iterable<Var> variables,Formula formula);
    
    /** @param content must be nonnull */
    public ExternalFormula createExternalFormula(Atomic content);

    /** @param left must be nonnull
     *  @param right must be nonnull
     */
    public Equal createEqual(Term left,Term right);
    
    /** @param instance must be nonnull
     *  @param classExpr must be nonnull
     */
    public Member createMember(Term instance,Term classExpr);

    /** @param subclass must be nonnull
     *  @param superclass must be nonnull
     */
    public Subclass createSubclass(Term subclass,Term superclass);

    /** @param object must be nonnull
     *  @param slots may be null or an empty sequence
     */
    public					
	Frame
	createFrame(Term object,Iterable<? extends FrameSlot> slots);

    
    /** @param predicate must be nonnull
     *  @param arguments must be a nonempty sequence
     */
    public
	PositionalAtom
	createPositionalAtom(Const predicate,
			     Iterable<? extends Term> arguments);

    
    /** @param predicate must be nonnull
     *  @param slots may be null or an empty sequence
     */
    public
	SlottedAtom
	createSlottedAtom(Const predicate,
			  Iterable<? extends ArgumentSlot> slots);


    
    /** @param name must be nonnull
     *  @param value must be nonnull
     */
    public ArgumentSlot createArgumentSlot(String name,Term value);
    
    /** @param method must be nonnull
     *  @param value must be nonnull
     */
    public FrameSlot createFrameSlot(Term method,Term value);


    /** @param content must be nonnull
     *  @param type must be nonnull
     *  @param lang may be null
     */
    public Const createConst(String content,String type,String lang);
    

    /** @param name must be nonnull */
    public Var createVar(String name);

    /** @param function must be nonnull
     *  @param arguments must be a nonempty sequence
     */
    public 
	PositionalExpr 
	createPositionalExpr(Const function,
			     Iterable<? extends Term> arguments);
    
    /** @param function must be nonnull
     *  @param slots may be null or an empty sequence
     */
    public 
	SlottedExpr
	createSlottedExpr(Const function,
			  Iterable<? extends ArgumentSlot> slots);
			  
    public List createEmptyList();

    /** @param head must be nonnull
     *  @param tail must be nonnull
     */
    public List createList(Term head,Term tail);

    /** @param content must be nonnull */
    public ExternalTerm createExternalTerm(Expr content);

} // interface AbstractSyntax
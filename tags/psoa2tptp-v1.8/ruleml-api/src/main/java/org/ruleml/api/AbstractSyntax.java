package org.ruleml.api;

import java.util.*;

/**
 * Instances of this interface are factories for abstract syntax objects; the
 * nested interfaces represent various abstract syntax constructs.
 */
public interface AbstractSyntax {

	/** Common interface for all abstract syntax constructs. */
	public interface Construct {

		/**
		 * Renders the object in the presentation syntax.
		 * 
		 * @return toString("")
		 */
		public String toString();

		/**
		 * Renders the object in the presentation syntax with the specified base
		 * indentation.
		 */
		public String toString(String indent);

	} // interface Construcrt

	public interface Document extends Construct {

		/** @return possibly null */
		public Base getBase();

		/** @return possibly null or empty */
		public Iterable<Prefix> getPrefixes();

		/** @return possibly null or empty */
		public Iterable<Import> getImports();

		/** @return possibly null */
		public Group getGroup();
	} // interface Document

	public interface Base extends Construct {

		public String getIRI();

	}

	/** interface for prefix */
	public interface Prefix extends Construct {
		/** @return nonnull */
		public Name getName();

		/** @return nonnull */
		public String getIRI();

	} // interface Prefix

	/** Shorthands of IRI prefixes. */
	public interface Name extends Construct {

		public String getShortIRI();

	} // interface Name

	public interface Import extends Construct {

		/** @return nonnull */
		public String getIRI();

		/** @return nonnull or empty */
		public Profile getProfile();

	} // interface Import

	public interface Group extends GroupElement {

		/** @return nonnull */
		public Collection<? extends GroupElement> getGroupElement();

	} // interface Group

	/** Common base for Group and Rule. */
	public interface GroupElement extends Construct {

		public Rule asRule();

		public Group asGroup();

	} // interface GroupElement

	public interface Rule extends GroupElement {

		/** @return nonnull */
		public Collection<Var> getVariables();

		/** @return nonnull */
		public Clause getClause();

	} // interface Rule

	public interface Clause extends Construct {

		public boolean isImplies();

		public Implies asImplies();

		public boolean isAtomic();

		public Atomic asAtomic();

	} // interface Clause

	public interface Implies extends Construct {

		/** @return nonempty sequence */
		public Collection<? extends Head> getHead();

		/** @return possibly null, if there is no condition */
		public Formula getBody();

		/** The set of all free variables in the clause. */
		public Set<Var> variables();

	} // interface Implies

	public interface Atomic extends Formula {

		public Atom asAtom();

		public Equal asEqual();

		public Subclass asSubclass();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();

	}

	public interface Head extends Construct {

		/** @return nonnull */
		public Collection<Var> getVariables();

		/** @return nonnull */
		public Atomic getAtomic();

	} // interface Head

	public interface Profile extends Construct {

		public String getIRI();
	}

	public interface Formula extends Construct {

		public Formula_And asAndFormula();

		public Formula_Or asOrFormula();

		public Formula_Exists asExistsFormula();

		public Atomic asAtomic();

		public Formula_External asExternalFormula();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();

	}

	public interface Formula_And extends Formula {

		/** @return nonnull */
		public Collection<? extends Formula> getFormulas();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();

	}

	public interface Formula_Or extends Formula {

		/** @return nonnull */
		public Collection<? extends Formula> getFormulas();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();

	}

	public interface Formula_Exists extends Formula {

		/** @return non-empty sequence */
		public Collection<Var> getVariables();

		/** The matrix. */
		public Formula getFormula();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();
	}

	public interface Formula_External extends Formula {

		public Atom getContent();

		public Set<Var> variables();

	}

	public interface Atom extends Atomic {

		public Psoa getPsoa();

		public Set<Var> variables();

	}

	public interface Equal extends Atomic {

		/** @return nonnull */
		public Term getLeft();

		/** @return nonnull */
		public Term getRight();

		/** The set of all free variables in the formula. */
		public Set<Var> varaiables();

	}

	public interface Subclass extends Atomic {

		/** @return nonnull */
		public Term getSubclass();

		/** @return nonnull */
		public Term getSuperclass();

		/** The set of all free variables in the formula. */
		public Set<Var> variables();

	}

	public interface Psoa extends Expr {

		public Term getInstance();

		public Term getClassExpr();

		public Collection<? extends Tuple> getPositionalAtom();

		public Collection<? extends Slot> getSlottedAtom();

		public Set<Var> variables();

	}

	public interface Slot extends Construct {

		public Term getName(); // not really a name rather term

		public Term getValue();

		public Set<Var> variables();

	}

	public interface Tuple extends Construct {

		public Collection<? extends Term> getArguments();

		public Set<Var> variables();

	}

	public interface Term extends Construct {

		// public boolean isConst();

		public Const asConst();

		// public boolean isVar();

		public Var asVar();

		// public boolean isExpr();

		public Expr asExpr();

		// public boolean isExternal();

		public External asExternal();

		public Set<Var> variables();

	}

	public interface Const extends Term {

		public Const_Literal asConstLiteral();

		public Const_Constshort asConstshort();

		public Set<Var> variables();

	}

	public interface Const_Literal extends Const {

		// public String getContent(); //cant be null
		public String getLiteral(); // cant be null

		public Symspace getSymspace();

		// public String getType(); //could not be moved to Symspace as not
		// implemented //cant be null

		// public String getLang(); //may be null

		public Set<Var> variables();

	}

	public interface Const_Constshort extends Const {

		public String getConstshort();

		public Set<Var> variables();

	}

	public interface Var extends Term {

		public String getName();

		public Set<Var> variables();

	} // interface Var

	public interface Expr extends Term {

		public Term getInstance();

		public Term getClassExpr();

		public Collection<? extends Tuple> getPositionalAtom();

		public Collection<? extends Slot> getSlottedAtom();

		public Set<Var> variables();
	}

	public interface External extends Term {

		public Expr GetExternalExpr();

		public Set<Var> variables();

	}

	public interface Symspace extends Construct {

		public String getValue();

		public Set<Var> variables();

	}

	// createX methods:

	/**
	 * @param base
	 *            can be null
	 * @param prefixes
	 *            can be null or empty
	 * @param imports
	 *            can be null or empty
	 * @param group
	 *            can be null
	 */
	public Document createDocument(Base base, Iterable<Prefix> prefixes,
			Iterable<Import> imports, Group group);

	public Base createBase(String iri);

	public Prefix createPrefix(Name name, String iri);

	public Name createName(String name);

	public Import createImport(String iri, Profile profile);

	public Group createGroup(Iterable<GroupElement> elements);

	/**
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public Rule createRule(Iterable<Var> vars, Clause matrix);

	public Clause createClause(Implies implication);

	public Clause createClause(Atomic formula);

	/**
	 * @param heads
	 *            can be null or empty
	 * @param condition
	 *            nonnull
	 */
	public Implies createImplies(Iterable<Head> heads, Formula condition);

	/**
	 * Creates a rule head by applying an exitential quantifier over the given
	 * variables to the formula.
	 * 
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public Head createHead(Iterable<Var> vars, Atomic matrix);

	public Profile createProfile(String iri);

	/**
	 * Creates conjunction.
	 * 
	 * @param formulas
	 *            can be null or empty
	 */
	public Formula_And createFormula_And(Iterable<Formula> formulas);

	/**
	 * Creates disjunction.
	 * 
	 * @param formulas
	 *            can be null or empty
	 */
	public Formula_Or createFormula_Or(Iterable<Formula> formulas);

	/**
	 * Creates existentially quantified formula.
	 * 
	 * @param vars
	 *            nonnull and nonempty
	 * @param matrix
	 *            nonnull
	 */
	public Formula_Exists createFormula_Exists(Iterable<Var> vars,
			Formula matrix);

	public Formula_External createFormula_External(Atom atom);

	public Atom createAtom(Psoa term);

	public Equal createEqual(Term lhs, Term rhs);

	public Subclass createSubclass(Term lhs, Term rhs);

	/**
	 * @param object
	 *            can be null
	 * @param classTerm
	 *            can be null
	 * @param tuples
	 *            can be null or empty; element tuples can be null or empty
	 * @param slots
	 *            can be bull or empty
	 */
	public Psoa createPsoa(Term object, Term classTerm, Iterable<Tuple> tuples,
			Iterable<Slot> slots);

	/**
	 * @param terms
	 *            can be null or empty
	 * @return possibly null
	 */
	public Tuple createTuple(Iterable<Term> terms);

	public Slot createSlot(Term name, Term value);

	public Const_Literal createConst_Literal(String literal, Symspace symspace);

	public Const_Constshort createConst_Constshort(String name);

	/**
	 * @param name
	 *            can be null or "" for anonymous variables.
	 */
	public Var createVar(String name);

	public Expr createExpr(Term object, Term classTerm, Iterable<Tuple> tuples,
			Iterable<Slot> slots);

	public External createExternalExpr(Expr externalexpr);

	/** Temporary plug. What is CURIE? */
	public Symspace createSymspace(String value);

} // interface AbstractSyntax
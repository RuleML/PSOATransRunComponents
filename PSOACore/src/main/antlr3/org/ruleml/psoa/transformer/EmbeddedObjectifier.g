/**
 *  This grammar file is used to generate a transformer for embedded objectification.
 **/

tree grammar EmbeddedObjectifier;

options
{
    output = AST;
    ASTLabelType = CommonTree;
    tokenVocab = PSOAPS;
    rewrite = false;
    k = 1;
}

@header
{
    package org.ruleml.psoa.transformer;

    import java.util.Collections;
    import java.util.Set;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.LinkedHashMap;
    import java.util.Vector;

    import static org.ruleml.psoa.FreshNameGenerator.*;
    import static org.ruleml.psoa.utils.IOUtil.*;
    import org.ruleml.psoa.analyzer.*;
}

@members
{
    // :BEGIN: These five fields are copied from Objectifier.g

    private boolean m_isRuleBody = false;
    private boolean m_isQuery = false;
    private boolean m_isGroundFact = false;

    private Set<String> m_localConsts;
    private Set<String> m_clauseVars = new HashSet<String>();

    // :END:

    /*
    
    m_newPositiveExistsVarNodes stores AST nodes describing existential variables
    occurring in positive contexts (rule conclusions and facts) 
    while 
    m_newNegativeExistsVarNodes stores existential variables 
    in negative contexts (rule conditions and queries).
    
    */

    private Map<String, CommonTree> m_newPositiveExistsVarNodes = new LinkedHashMap<String, CommonTree>();
    private Map<String, CommonTree> m_newNegativeExistsVarNodes = new LinkedHashMap<String, CommonTree>();

    // :BEGIN: The next four methods are adapted from Objectifier.g

    public void setExcludedLocalConstNames(Set<String> excludedConstNames)
    {
        m_localConsts = excludedConstNames;
    }

    private CommonTree newVarNode()
    {
        // During clause-tree traversal, generate a fresh variable name using an increasing
        // set of already encountered clause variable names. If this name is encountered later, 
        // it will be modified in newForallVarsTree().
        String name = freshVarName(m_clauseVars);
        CommonTree node = (CommonTree)adaptor.create(VAR_ID, name);

        if (m_isRuleBody || m_isQuery)
            m_newNegativeExistsVarNodes.put(name, node);
        else
            m_newPositiveExistsVarNodes.put(name, node);

        return (CommonTree)adaptor.dupNode(node);
    }

    private void checkNewExistVars()
    {
        for (Map.Entry<String, CommonTree> entry : m_newPositiveExistsVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();

           // Rename the variable name if it was encountered in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));
           }
        }

        m_newPositiveExistsVarNodes.clear();

        for (Map.Entry<String, CommonTree> entry : m_newNegativeExistsVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();

           // Rename the variable name if it was encountered in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));
           }
        }

        m_newNegativeExistsVarNodes.clear();
    }

    private CommonTree newExistsVarsTree(Map<String, CommonTree> existVarNodes)
    {
        CommonTree root = (CommonTree) adaptor.nil();

        for (Map.Entry<String, CommonTree> entry : existVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();

           // Rename the variable name if it was encountered in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));
           }

           adaptor.addChild(root, node);
        }

        existVarNodes.clear();
        return root;
    }

    // :END:
}

document
    :   ^(DOCUMENT base? prefix* importDecl* group?)
    ;

base
    :   ^(BASE IRI_REF)
    ;

prefix
    :   ^(PREFIX NAMESPACE IRI_REF)
    ;

importDecl
    :   ^(IMPORT IRI_REF IRI_REF?)
    ;

group
    :   ^(GROUP group_element*)
    ;

group_element
    :   rule
    |   group
    ;

query
@init
{
   m_isQuery = true;
}
@after
{
   checkNewExistVars();
   m_clauseVars.clear();
   m_isQuery = false;
}
    :   formula
    ->  { m_newNegativeExistsVarNodes.isEmpty() }? formula
    ->  ^(EXISTS { newExistsVarsTree(m_newNegativeExistsVarNodes) } formula)
    ;

rule
@init
{
   // Reset variable generator before processing each (PSOA RuleML Presentation Syntax) RULE nonterminal
   // (http://wiki.ruleml.org/index.php/PSOA_RuleML#Monolithic_EBNF_for_PSOA_RuleML_Presentation_Syntax)
   resetVarGen();
}
@after
{
   checkNewExistVars();
   m_clauseVars.clear();
}
    :  ^(FORALL (VAR_ID {  m_clauseVars.add($VAR_ID.text); })+ clause) // Add Forall variable(s) to m_clauseVars for ground heuristic below
    |   clause
    ;

/*
 * ANTLR rule for transforming clause trees
 *
 * A clause is either an IMPLICATION (i.e., a PSOA rule) subtree containing a head and a formula, 
 * or a standalone head (i.e., a PSOA fact) subtree:
 *
 * In IMPLICATION subtrees, oidless embedded atoms are assigned existential variable OIDs.
 * These variables are gathered in m_newPositiveExistsVarNodes if they occur in a positive
 * context and in m_newNegativeExistsVarNodes if they occur in a negative one.
 *
 * In standalone head subtrees, the m_clauseVars.isEmpty() heuristic determines if the head is ground;
 * the set m_clauseVars is populated with variable names listed in the possibly enclosing Forall clause;
 * m_clauseVars is populated in the FORALL alternative of rule before clause is conjunctively invoked:
 *
 * If m_clauseVars is empty, then presumably the standalone head subtree has no variables, making it ground.
 *
 * All oidless embedded atoms found in the standalone head subtree are objectified with Skolem constant OIDs.
 */

clause
    :   ^(IMPLICATION
          { m_isGroundFact = false; }  // An IMPLICATION is not a PSOA ground fact
          head
          { m_isRuleBody = true; }  // Now in the IMPLICATION's formula (rule body)
          formula
          { m_isRuleBody = false; }  // No longer in the IMPLICATION's formula
         ) 
        -> { (m_newPositiveExistsVarNodes.isEmpty())
           && m_newNegativeExistsVarNodes.isEmpty() }?  // No embedded OIDs in either conclusion (head) or condition (body)
           ^(IMPLICATION head formula)
        -> { m_newPositiveExistsVarNodes.isEmpty() }?   // Embedded OIDs in condition, none in conclusion
           ^(IMPLICATION head ^(EXISTS { newExistsVarsTree(m_newNegativeExistsVarNodes) } formula))
        -> { m_newNegativeExistsVarNodes.isEmpty() }?   // Embedded OIDs in conclusion, none in condition
           ^(IMPLICATION ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head) formula)
        -> ^(IMPLICATION  // Embedded OIDs in both conclusion and condition
             ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head)
             ^(EXISTS { newExistsVarsTree(m_newNegativeExistsVarNodes) } formula))
    |   { m_isGroundFact = m_clauseVars.isEmpty(); }  // The fact (standalone head subtree) is ground
        head
        -> { !m_isGroundFact && !m_newPositiveExistsVarNodes.isEmpty() }?  // Not a ground fact, and there are generated OID variables
           ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head)
        -> head // Otherwise, the fact is ground, or there were no generated OID variables
    ;

head
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS VAR_ID+ head)
    ;

formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS VAR_ID+ formula)
    |   FALSITY
    |   naf_formula
    |   atomic
    |   external
    ;

naf_formula
    :   ^(NAF formula)
    ;

atomic
    :   atom
    |   equal
    |   subclass
    ;

atom
    :   psoa
    ;

equal
    :   ^(EQUAL term term)
    ;

subclass
    :   ^(SUBCLASS term term)
    ;

term
    :   constant
    |   VAR_ID
    {
        m_clauseVars.add($VAR_ID.text);  // Add variable name to avoid later collisions with generated variables (e.g., for OIDs)
    }
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;

/*
 * ANTLR rule for transforming psoa trees
 *
 * psoa is where all oidless embedded atoms are objectified.
 *
 * psoa uses the previously determined boolean m_isGroundFact
 * to determine whether to emit a Skolem constant OID (_1, _2, ...) 
 * or an existential variable OID.
 *
 * The method newVarNode() does the work of both building the AST node
 * representing the OID and putting it into one of the m_newPositiveExistsVarNodes
 * or m_newNegativeExistsVarNodes Maps, depending on the context of the
 * surrounding atom.
 */

psoa
    :   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
    |   ^(OIDLESSEMBATOM ^(INSTANCE term) tuple* slot*)
    ->  { m_isGroundFact }? // The oidless embedded atom occurs in a ground fact
    	^(PSOA ^(SHORTCONST LOCAL[freshConstName(m_localConsts)]) ^(INSTANCE term) tuple* slot*) // Generate a Skolem constant OID 
    ->  // Here we need to gather introduced variables into an Exists clause:
        ^(PSOA { newVarNode() } ^(INSTANCE term) tuple* slot*) // Otherwise, generate an existential variable OID, put it in the appropriate map
    ;

tuple
    :   ^(TUPLE DEPSIGN term*)
    ;

slot
    :   ^(SLOT DEPSIGN term term)
    ;

constant
    :   ^(LITERAL IRI)
    |   ^(SHORTCONST constshort)
    |   TOP
    ;

constshort
    :   IRI
    |   LITERAL
    |   NUMBER
    |   LOCAL
    ;

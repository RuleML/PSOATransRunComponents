/**
 * This grammar file is the template for all transformers. The grammar rules corresponds
 * to PSOA's EBNF grammar.
 *
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
    private boolean m_isRuleBody = false;
    private boolean m_isQuery = false;
    private boolean m_isGroundFactOrConclusion = false;
    private int m_ruleNum = 0;

    private Set<String> m_localConsts;
    private Set<String> m_clauseVars = new HashSet<String>();

    private Map<String, CommonTree> m_newPositiveExistsVarNodes = new LinkedHashMap<String, CommonTree>();
    private Map<String, CommonTree> m_newNegativeExistsVarNodes = new LinkedHashMap<String, CommonTree>();

    private KBInfoCollector m_KBInfo = null;

    public void setExcludedLocalConstNames(Set<String> excludedConstNames)
    {
        m_localConsts = excludedConstNames;
    }

    public void setKBInfo(KBInfoCollector info)
    {
        m_KBInfo = info;
    }

    private CommonTree newVarNode()
    {
        // Generate a fresh variable name using an incomplete set of
        // clause variables. The name may be modified later in newForallVarsTree().
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

           // Rename the variable name if it has been used in the clause
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

           // Rename the variable name if it has been used in the clause
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

           // Rename the variable name if it has been used in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));
           }

           adaptor.addChild(root, node);
        }

        existVarNodes.clear();
        return root;
    }

    private boolean isGroundFactOrConclusion()
    {
        Vector nonGroundRuleHeads = m_KBInfo.getNonGroundRuleHeads();
        return Collections.binarySearch(nonGroundRuleHeads, m_ruleNum) < 0;
    }
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
   // Reset variable generator before processing each rule
   resetVarGen();
}
@after
{
   checkNewExistVars();
   m_ruleNum++;
   m_clauseVars.clear();
}
    :  ^(FORALL (VAR_ID {  m_clauseVars.add($VAR_ID.text); })+ clause)
    |   clause
    ;

clause
    :   ^(IMPLICATION
          { m_isGroundFactOrConclusion = isGroundFactOrConclusion(); }
          head
          { m_isRuleBody = true; }
          formula
          { m_isRuleBody = false; })
        -> { (m_newPositiveExistsVarNodes.isEmpty() || m_isGroundFactOrConclusion)
           && m_newNegativeExistsVarNodes.isEmpty() }?
           ^(IMPLICATION head formula)
        -> { m_newPositiveExistsVarNodes.isEmpty() || m_isGroundFactOrConclusion }?
           ^(IMPLICATION head ^(EXISTS { newExistsVarsTree(m_newNegativeExistsVarNodes) } formula))
        -> { m_newNegativeExistsVarNodes.isEmpty() }?
           ^(IMPLICATION ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head) formula)
        -> ^(IMPLICATION
             ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head)
             ^(EXISTS { newExistsVarsTree(m_newNegativeExistsVarNodes) } formula))
    |   { m_isGroundFactOrConclusion = isGroundFactOrConclusion(); }
        head
        -> { !m_isGroundFactOrConclusion && !m_newPositiveExistsVarNodes.isEmpty() }?
           ^(EXISTS { newExistsVarsTree(m_newPositiveExistsVarNodes) } head)
        -> head
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
        m_clauseVars.add($VAR_ID.text);
    }
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;

psoa
    :   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
    |   ^(OIDLESSEMBATOM ^(INSTANCE term) tuple* slot*)
    ->  { m_isRuleBody || m_isQuery || !m_isGroundFactOrConclusion }?
        // here we need to gather introduced variables into an Exists clause.
        ^(PSOA { newVarNode() } ^(INSTANCE term) tuple* slot*)
    ->  ^(PSOA ^(SHORTCONST LOCAL[freshConstName(m_localConsts)]) ^(INSTANCE term) tuple* slot*)
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

/**
 *  This grammar file is used to generate a transformer for differentiated objectification.
 **/

tree grammar Objectifier;

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
	
	import java.util.Set;
	import java.util.HashSet;
    import java.util.Map;
    import java.util.HashMap;
	import org.ruleml.psoa.analyzer.*;
	
	import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    private boolean m_isRuleBody = false, m_isGroundFact = false, m_dynamic = false, m_diff;
    private Set<String> m_localConsts;
    private Set<String> m_clauseVars = new HashSet<String>();
    private Map<String, CommonTree> m_newVarNodes = new HashMap<String, CommonTree>();
    private KBInfoCollector m_KBInfo = null;
    
    public void setDynamic(boolean b, KBInfoCollector info)
    {
        m_dynamic = b;
        m_KBInfo = info;
    }
    
    public void setDifferentiated(boolean d)
    {
        m_diff = d;
    }
    
    public void setExcludedLocalConstNames(Set<String> excludedConstNames)
    {
        m_localConsts = excludedConstNames;
    }
    
    private CommonTree newVarNode()
    {
	    // Generate a fresh variable name using an incomplete set of 
	    // clause variables. The name may be modified later in newVarsTree().
        String name = freshVarName(m_clauseVars);
        CommonTree node = (CommonTree)adaptor.create(VAR_ID, name);
        
		m_newVarNodes.put(name, node);
		return (CommonTree)adaptor.dupNode(node);
    }
	
  	private CommonTree newVarsTree()
  	{
        CommonTree root = (CommonTree)adaptor.nil();
  	    
  	    for (Map.Entry<String, CommonTree> entry : m_newVarNodes.entrySet())
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
        
        m_newVarNodes.clear();
        return root;
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
   // Reset variable generator before processing each query
   resetVarGen();
}
@after
{
   m_clauseVars.clear();
   m_newVarNodes.clear();
}
    :   formula
    ;
    
rule
@init
{
   // Reset variable generator before processing each rule
   resetVarGen();
}
@after
{
   m_clauseVars.clear();
}
    :  ^(FORALL (VAR_ID {  m_clauseVars.add($VAR_ID.text); })+ clause)
      // Add new variables to ForAll wrapper
      -> { !m_newVarNodes.isEmpty() && m_diff }? ^(FORALL VAR_ID+ { newVarsTree() } clause)
      -> ^(FORALL VAR_ID+ clause)
	  |  clause
	  // Add new variables to ForAll wrapper
	  -> { !m_newVarNodes.isEmpty() && m_diff }? ^(FORALL { newVarsTree() } clause)
	  -> clause
    ;

clause
    :   ^(IMPLICATION head { m_isRuleBody = true; } formula { m_isRuleBody = false; })
    |   { m_isGroundFact = m_clauseVars.isEmpty(); } head { m_isGroundFact = false; }
    ;
    
head
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS VAR_ID+ head)
    ;

formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   FALSITY
    |   ^(EXISTS VAR_ID+ formula)
    |   atomic
    |   external
    ;

atomic
    :   atom
    |   equal
    |   subclass
    ;

atom
    :   psoa[true]
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
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomic]
@init
{
   CommonTree varNode;
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuple* slot*)
    // keep oidful psoa term unchanged
    -> { oid != null }? ^(PSOA $oid ^(INSTANCE $type) tuple* slot*)
    // dynamic objectification for an atom using a relational predicate 
    -> { !isAtomic || 
         (   m_dynamic 
          && !m_KBInfo.hasHeadOnlyVariables()
          && m_KBInfo.isPurelyRelational($type.tree)) }?
          ^(PSOA ^(INSTANCE $type) tuple* slot*)
    // differentiated static objectification for psoa terms in rule conditions
    -> { m_isRuleBody && m_diff }?
          ^(PSOA { newVarNode() } ^(INSTANCE $type) tuple* slot*)
    // differentiated static objectification for psoa terms used as ground facts
    -> { m_isGroundFact && m_diff }?
          ^(PSOA ^(SHORTCONST LOCAL[freshConstName(m_localConsts)]) ^(INSTANCE $type) tuple* slot*)
    // differentiated static objectification for psoa terms used in 
    // rule conclusions or queries or undifferentiated static objectification
    ->
        ^(EXISTS { (varNode = newVarNode()) }
            ^(PSOA { varNode.dupNode() } ^(INSTANCE $type) tuple* slot*)
        )
    ;

tuple
    :   ^(TUPLE DEPSIGN term+)
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
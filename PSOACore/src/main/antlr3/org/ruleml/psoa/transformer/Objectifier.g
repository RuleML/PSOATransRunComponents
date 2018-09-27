/**
 *  This grammar file is used to generate a transformer for objectification.
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
    import java.util.LinkedHashMap;
	import org.ruleml.psoa.analyzer.*;
	
	import static org.ruleml.psoa.FreshNameGenerator.*;
	import static org.ruleml.psoa.utils.IOUtil.*;
}

@members
{
    private boolean m_isRuleBody = false, m_isQuery = false, m_isGroundFact = false, m_dynamic = false, m_diff;
    private Set<String> m_localConsts;
    private Set<String> m_clauseVars = new HashSet<String>();
    private Map<String, CommonTree> m_newForallVarNodes = new LinkedHashMap<String, CommonTree>(),
    								m_newExistVarNodes = new LinkedHashMap<String, CommonTree>();
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
	    // clause variables. The name may be modified later in newForallVarsTree().
        String name = freshVarName(m_clauseVars);
        CommonTree node = (CommonTree)adaptor.create(VAR_ID, name);
        
        if (m_isRuleBody || m_isQuery)
        	m_newForallVarNodes.put(name, node);
        else
        	m_newExistVarNodes.put(name, node);
		return (CommonTree)adaptor.dupNode(node);
    }
	
  	private CommonTree newForallVarsTree()
  	{
        CommonTree root = (CommonTree)adaptor.nil();
  	    
  	    for (Map.Entry<String, CommonTree> entry : m_newForallVarNodes.entrySet())
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
        
        m_newForallVarNodes.clear();
        return root;
  	}
  	
  	private void checkNewExistVars()
  	{
  	    for (Map.Entry<String, CommonTree> entry : m_newExistVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();
           
           // Rename the variable name if it has been used in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));  
           }
        }
        
        m_newExistVarNodes.clear();
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
    ->  { m_newForallVarNodes.isEmpty() }? formula
    ->  ^(EXISTS { newForallVarsTree() } formula)
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
   m_clauseVars.clear();
}
    :  ^(FORALL (VAR_ID {  m_clauseVars.add($VAR_ID.text); })+ clause)
      // Add new variables to ForAll wrapper
      -> { !m_newForallVarNodes.isEmpty() && m_diff }? ^(FORALL VAR_ID+ { newForallVarsTree() } clause)
      -> ^(FORALL VAR_ID+ clause)
	  |  clause
	  // Add new variables to ForAll wrapper
	  -> { !m_newForallVarNodes.isEmpty() && m_diff }? ^(FORALL { newForallVarsTree() } clause)
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
   boolean hasSlot = false;
}
    :   ^(PSOA oid=term?
            ^(INSTANCE type=term) tuple* (slot { hasSlot = true; })*
            	{
            		// If predicate variable of atom can be bound to a relational predicate, static/dynamic may be incomplete
            		if (m_dynamic &&   // Static/dynamic setting
            			(oid == null || $oid.tree.getType() == VAR_ID) &&   // OID absent or OID variable
            			!hasSlot &&    // No slot 
            			// Tuple testing is more complicated, hence omitted, leading to more "false positive" warnings
            			$type.tree.getType() == VAR_ID)    // Predicate variable
            		{
            			printErrln("Warning: Predicate variables may lead to incompleteness under " +
            					   "static/dynamic objectification (completeness obtained under --staticOnly (-s) option)");
            		}
            	}
         )
    // keep oidful psoa term unchanged
    -> { oid != null }? ^(PSOA $oid ^(INSTANCE $type) tuple* slot*)
    // dynamic objectification for an atom using a relational predicate 
    -> { !isAtomic || 
         (   m_dynamic 
          && !m_KBInfo.hasHeadOnlyVariables()
	  && $type.tree.getType() != VAR_ID
          && m_KBInfo.isRelational($type.tree)) }?
          ^(PSOA ^(INSTANCE $type) tuple* slot*)
    // differentiated static objectification for psoa terms in rule conditions or queries
    // (for queries, existential quantifiers are lifted to the top-level)
    -> { (m_isRuleBody && m_diff) || m_isQuery }?
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

tree grammar DifferentiateObjectifier;

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
	import org.ruleml.psoa.analyzer.*;
	
	import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    private boolean m_isRuleBody = false, m_isGroundFact = false, m_dynamic = false;
    private KBInfoCollector m_KBInfo = null;
    
    public void setDynamic(boolean b, KBInfoCollector info)
    {
        m_dynamic = b;
        m_KBInfo = info;
    }
    
    private String newVarName()
    {
        Set<String> vars;
        String name;
        
		if ($query.size() > 0)
		{
		    vars = $query::queryVars;
		    name = freshVarName(vars);
		    vars.add(name);
		}
		else
		{
		    vars = $rule::vars;
		    name = freshVarName(vars);
		    vars.add(name);
		    if (m_isRuleBody)
		        $rule::newVarsTree.addChild((CommonTree)adaptor.create(VAR_ID, name));
		}
		
		return name;
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
scope
{
  Set<String> queryVars;
}
@init
{
   $query::queryVars = new HashSet<String>();
}
@after
{
   $query::queryVars.clear();
   $query::queryVars = null;
}
    :   formula
    ;
    
rule
scope
{
  Set<String> vars;
  CommonTree newVarsTree;
}
@init
{
  $rule::vars = new HashSet<String>();
  $rule::newVarsTree = (CommonTree)adaptor.nil();
  // Reset the counter for generating variable names before processing each rule 
  resetVarCounter();
}
    :  ^(FORALL (VAR_ID { $rule::vars.add($VAR_ID.text); })+ clause)
      -> ^(FORALL VAR_ID+ { $rule::newVarsTree } clause)
	  |   clause
	  -> { $rule::newVarsTree.getChildCount() > 0 }?
	     ^(FORALL { $rule::newVarsTree } clause)
	  ->  clause
    ;

clause
    :   ^(IMPLICATION head { m_isRuleBody = true; } formula { m_isRuleBody = false; })
    |   { m_isGroundFact = $rule::vars.isEmpty(); } head { m_isGroundFact = false; }
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
      if ($query.size() > 0)
        $query::queryVars.add($VAR_ID.text);
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
  String varName;
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuple* slot*)
    -> { oid != null }? ^(PSOA $oid ^(INSTANCE $type) tuple* slot*)
    -> { !isAtomic || 
         (   m_dynamic 
          && !m_KBInfo.hasHeadOnlyVariables()
          && m_KBInfo.isPurelyRelational($type.tree)) }?
          ^(PSOA ^(INSTANCE $type) tuple* slot*)
    -> { m_isRuleBody }?
          ^(PSOA VAR_ID[newVarName()] ^(INSTANCE $type) tuple* slot*)
    -> { m_isGroundFact }?
          ^(PSOA ^(SHORTCONST LOCAL[freshConstName()]) ^(INSTANCE $type) tuple* slot*)
    ->  // Rule head or query
        ^(EXISTS VAR_ID[varName = newVarName()]
            ^(PSOA VAR_ID[varName] ^(INSTANCE $type) tuple* slot*)
        )
    ;

tuple
    :   ^(TUPLE term+)
    ;
    
slot
    :   ^(SLOT term term)
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
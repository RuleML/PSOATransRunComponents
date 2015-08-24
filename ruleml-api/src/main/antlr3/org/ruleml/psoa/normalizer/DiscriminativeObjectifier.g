tree grammar DiscriminativeObjectifier;

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
	package org.ruleml.psoa.normalizer;
	
	import java.util.Set;
	import java.util.HashSet;
}

@members
{
    private int m_localConstID = 0, m_varCtr;
    private boolean m_isRuleHead = false, m_isRuleBody = false;
    
    private String newVarName()
    {
      if ($query.size() > 0)
        return newVarName($query::queryVars);
        
      String name = newVarName($rule::vars);
      
      if (m_isRuleBody)
        $rule::newVarsTree.addChild((CommonTree)adaptor.create(VAR_ID, name));
      
      return name;
    }
    
    private String newVarName(Set<String> usedNames)
    {
      String name;
      
      do
      {
        name = String.valueOf(++m_varCtr);
      } while (!usedNames.add(name));
      
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
    :   ^(PREFIX ID IRI_REF)
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
  m_varCtr = 0;
}
    :  ^(FORALL (VAR_ID { $rule::vars.add($VAR_ID.text); })+ clause)
    -> ^(FORALL VAR_ID+ { $rule::newVarsTree } clause)
	  |   clause
	  -> { $rule::newVarsTree.getChildCount() > 0 }?
	     ^(FORALL { $rule::newVarsTree } clause)
	  ->  clause
    ;

clause
    :   ^(IMPLICATION
         { m_isRuleHead = true; } head { m_isRuleHead = false; }
         { m_isRuleBody = true; } formula { m_isRuleBody = false; })
    |   head
    ;
    
head
    :   atomic
    |   ^(EXISTS (VAR_ID { $rule::vars.add($VAR_ID.text); } )+ atomic)
    ;

formula
    :   ^(AND formula+)
    |   ^(OR formula+)
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
    -> { !isAtomic }? ^(PSOA ^(INSTANCE $type) tuple* slot*)
    -> { m_isRuleBody }?
          ^(PSOA VAR_ID[newVarName()] ^(INSTANCE $type) tuple* slot*)
    -> { $query.size() > 0 || !$rule::vars.isEmpty() }?
        // Rule head or query
        ^(EXISTS VAR_ID[varName = newVarName()]
            ^(PSOA VAR_ID[varName] ^(INSTANCE $type) tuple* slot*)
        )
    -> ^(PSOA ^(SHORTCONST LOCAL[String.valueOf(++m_localConstID)]) ^(INSTANCE $type) tuple* slot*)
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
        {
            String lcName = $LOCAL.text;
            boolean hasNonDigitChar = false;
            for (int i = lcName.length() - 1; i >= 0; i--)
            { 
                if (!Character.isDigit(lcName.charAt(i)))
                {
                    hasNonDigitChar = true;
                    break;
                }
            }
            if (!hasNonDigitChar)
            {
                throw new RuntimeException("'_" + lcName + "' is disallowed: the name of local constants cannot have only digits.");
            }
        }
    ;
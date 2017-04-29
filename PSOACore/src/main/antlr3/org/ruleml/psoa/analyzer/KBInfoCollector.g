/**
 * This grammar file is used to generate a static analyzer that collects predicate 
 * information from KB.  
 **/
 
tree grammar KBInfoCollector;

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
	package org.ruleml.psoa.analyzer;
	
	import java.util.Set;
	import java.util.HashSet;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.Collection;
}

@members
{
    // private KBInfo m_KBInfo = new KBInfo();
        
    private Map<String, PredicateInfo> m_predicates = new HashMap<String, PredicateInfo>();
    private List<String> m_importedKBs = new ArrayList<String>();
    private boolean m_hasHeadOnlyVariables = false;
    
    public PredicateInfo getPredInfo(String predicate)
    {
        return getPredInfo(predicate, false);
    }
    
    private PredicateInfo getPredInfo(String predicate, boolean createNew)
    {
        PredicateInfo pi = m_predicates.get(predicate);
        
        if (pi == null && createNew)
        {
            pi = new PredicateInfo(predicate);
            m_predicates.put(predicate, pi);
        }
        
        return pi;
    }
    
    public boolean isPurelyRelational(CommonTree tree)
    {
        return isPurelyRelational(tree.toStringTree());
    }
    
    public boolean isPurelyRelational(String pred)
    {
        PredicateInfo pi = m_predicates.get(pred);
        
        return pi == null || pi.isPurelyRelational();
    }
    
    public void addPsoaTermInfo(String pred, Collection<Integer> positionalArities, boolean hasOID, boolean hasSlot)
    {
        PredicateInfo pi = getPredInfo(pred, true);
        
        if (positionalArities.isEmpty())
        {
            if (!hasSlot)
                pi.addPositionalArity(0);
        }
        else
            pi.addPositionalArities(positionalArities);
        
        if (hasOID)
            pi.setHasOID(hasOID);
            
        if (hasSlot)
            pi.setHasSlot(hasSlot);
    }
    
    public void addPredInSubclassFormula(String pred)
    {
        PredicateInfo pi = getPredInfo(pred, true);
        pi.setHasOID(true);
    }
    
    public boolean hasHeadOnlyVariables()
    {
        return m_hasHeadOnlyVariables;
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
    :   formula
    ;
    
rule
scope
{
    Set<String> headOnlyVars;
    Set<String> existVars;
    boolean isRuleBody;
}
@init
{
    $rule::headOnlyVars = new HashSet<String>();
    $rule::existVars = new HashSet<String>();
    $rule::isRuleBody = false;
}
@after
{
    if (!$rule::headOnlyVars.isEmpty())
    {
       $rule::headOnlyVars.clear();
       m_hasHeadOnlyVariables = true;
    }
    $rule::existVars.clear();
}
    :  ^(FORALL VAR_ID+ clause)
    |   clause -> clause
    ;

clause
    :   ^(IMPLICATION head { $rule::isRuleBody = true; } formula { $rule::isRuleBody = false; })
    |   head
    ;
    
head
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS (VAR_ID { $rule::existVars.add($VAR_ID.text); })+ head)
    ;
    
formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS VAR_ID+ formula)
    |   FALSITY
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
    :   ^(SUBCLASS sub=term sup=term)
    {
        addPredInSubclassFormula($sub.tree.toStringTree());
        addPredInSubclassFormula($sup.tree.toStringTree());
    }
    ;
    
term
    :   constant
    |   VAR_ID
    {
       String varName = $VAR_ID.text;
       if (!$rule::isRuleBody)
       {
       	  if (!$rule::existVars.contains(varName))
       	     $rule::headOnlyVars.add(varName);
       }
       else
          $rule::headOnlyVars.remove(varName);
    }
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomicFormula]
@init
{
    boolean hasSlot = false;
    ArrayList<Integer> arities = new ArrayList<Integer>();
}
    :   ^(PSOA oid=term? ^(INSTANCE pred=term)
            (t=tuple { arities.add($t.length); })* (slot { hasSlot = true; })*)
    {
        if (isAtomicFormula)
            addPsoaTermInfo($pred.tree.toStringTree(), arities, oid != null, hasSlot);
    }
    ;

tuple returns [int length]
@init
{
    $length = 0;
}
    :   ^(TUPLE (term { $length++; })+)
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
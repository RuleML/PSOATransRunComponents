tree grammar ConjunctiveHeadSplitter;

options 
{
  output = AST;
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	rewrite = true;
	k = 1;
}

@header
{
	package org.ruleml.psoa.normalizer;
	
	import java.util.Set;
	import java.util.HashSet;
	import java.util.List;
	import java.util.ArrayList;
	import java.util.Iterator;
}

@members
{
    private CommonTree splitImplication(CommonTree head, CommonTree body)
    {
        CommonTree root;
        RewriteRuleSubtreeStream stream_body;
        List headConjuncts, conjunctVarsList;
        
        root = (CommonTree)adaptor.nil();
        stream_body = new RewriteRuleSubtreeStream(adaptor, "rule body", body);
        headConjuncts = head.getChildren();
        conjunctVarsList = $rule::conjunctVarsList;
        
        assert headConjuncts.size() == conjunctVarsList.size();
        
        Iterator<Set<String>> conjunctVarsListItr = conjunctVarsList.iterator();
        
        for (Object conjunct : headConjuncts)
        {
            Set<String> conjunctVars = conjunctVarsListItr.next();
            Object newImply = adaptor.create(IMPLICATION, ":-");
            
            adaptor.addChild(newImply, conjunct);
            adaptor.addChild(newImply, stream_body.nextTree());
            
            if (conjunctVars.isEmpty())
            {
              adaptor.addChild(root, newImply);
            }
            else
            {
	            Object forall = adaptor.create(FORALL, "FORALL");
	            
	            for (String var : conjunctVars)
	            {
	                adaptor.addChild(forall, adaptor.create(VAR_ID, var));
	            }
	            
	            adaptor.addChild(forall, newImply); 
	            adaptor.addChild(root, forall);
	          }
        }
        
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
    :   formula
    ;
    
rule
scope
{
    boolean hasConjunctiveHead;
    List<Set<String>> conjunctVarsList;
}
@init
{ 
    $rule::conjunctVarsList = new ArrayList<Set<String>>();
}
@after
{
    // Clean up conjunctVarsList
    List<Set<String>> l = $rule::conjunctVarsList;
    for (Set<String> conjunctVars : l)
    {
        conjunctVars.clear();
    }
    l.clear();
}
    :  ^(FORALL VAR_ID+ clause)
    -> { $rule::hasConjunctiveHead }? clause
    -> ^(FORALL VAR_ID+ clause)
	|   clause // The AST of conjunction has between rewritten while parsing clause 
    ;

clause
scope
{
    boolean inRuleHead;
    Set<String> conjunctVars;
}
@init
{
    $clause::conjunctVars = new HashSet<String>();
    $clause::inRuleHead = true;
    $rule::hasConjunctiveHead = false;
}
    :   ^(IMPLICATION head { $clause::inRuleHead = false; } formula)
    -> { $rule::hasConjunctiveHead }?
       { splitImplication($head.tree, $formula.tree) }
    ->  ^(IMPLICATION head formula)
    |   atomic
    |   ^(AND { $rule::hasConjunctiveHead = true; }
            (
                atomic
                {
                    $rule::conjunctVarsList.add($clause::conjunctVars);
                    $clause::conjunctVars = new HashSet<String>();
                }
            )+
         )
    ->  atomic+
    ;
    
head
    :   atomic
    |   ^(AND { $rule::hasConjunctiveHead = true; }
            (
	            atomic
	            {
	                $rule::conjunctVarsList.add($clause::conjunctVars);
	                $clause::conjunctVars = new HashSet<String>();
	            }
            )+
         )
    |   ^(EXISTS VAR_ID+ atomic)
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
            if ($rule::hasConjunctiveHead && $clause::inRuleHead)
                $clause::conjunctVars.add($VAR_ID.text);
        }
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
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
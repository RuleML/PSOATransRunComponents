/**
 * This grammar file is used to generate a transformer for splitting rules 
 * with conjunctive conclusions (rule heads).
 **/

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
	package org.ruleml.psoa.transformer;
	
	import java.util.Set;
	import java.util.HashSet;
	import java.util.Map;
    import java.util.HashMap;
	import java.util.List;
	import java.util.ArrayList;
}

@members
{
    private CommonTree splitRules(CommonTree head, CommonTree body)
    {
        CommonTree root = (CommonTree)adaptor.nil();
        Map<Object, Set<String>> headConjuctVars = $clause::headConjuctVars;
        
        int i = 0;
        
        for (Object conjunct : head.getChildren())
        {
            i++;
            Set<String> conjunctVars = headConjuctVars.get(conjunct);
            Object newImply = adaptor.create(IMPLICATION, ":-");
            
            adaptor.addChild(newImply, conjunct);
            adaptor.addChild(newImply, i == 1? body : adaptor.dupTree(body));
            
            conjunctVars.addAll($clause::bodyVars);
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
    boolean hasConjuctiveHead;
}
@init
{
    $rule::hasConjuctiveHead = false;
}
    // The AST has between rewritten while parsing clause
    :  ^(FORALL VAR_ID+ clause) 
    -> { $rule::hasConjuctiveHead }? clause
    -> ^(FORALL VAR_ID+ clause)
	|   clause 
    ;

clause
scope
{
    Map<Object, Set<String>> headConjuctVars;
    Set<String> conjunctFreeVars;
    Set<String> bodyVars;
}
@init
{
    $clause::headConjuctVars = new HashMap<Object, Set<String>>();
    $clause::conjunctFreeVars = new HashSet<String>();
    $clause::bodyVars = new HashSet<String>();
}
@after
{
    $clause::conjunctFreeVars = null;
    $clause::bodyVars = null;
    for (Map.Entry<Object, Set<String>> entry : $clause::headConjuctVars.entrySet())
    {
        entry.getValue().clear();
    }
    $clause::headConjuctVars = null;
}
    :   ^(IMPLICATION head[false] formula)
    -> { $rule::hasConjuctiveHead }? { splitRules($head.tree, $formula.tree) }
    -> ^(IMPLICATION head formula)
    |   head[false]
    ;
    
head[boolean inExists]
scope
{
    Set<String> existVars;
}
@init
{
    $head::existVars = new HashSet<String>();
}
@after
{
    $head::existVars = null;
}
    :   atomic
    |   ^(AND
            (h=head[inExists]
                {
                    if (!inExists)
                    {
                        $clause::headConjuctVars.put($h.tree, $clause::conjunctFreeVars);
                        $clause::conjunctFreeVars = new HashSet<String>();
                    }
                }
            )+
          )
       {
            if (!inExists)
                $rule::hasConjuctiveHead = true;
       }
    // Keep conjunction inside a existential quantification 
    -> { inExists }? ^(AND head+)
    -> head+
    |   ^(EXISTS (VAR_ID { $head::existVars.add($VAR_ID.text); })+ head[true])
        { $head::existVars.clear(); }
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
            if ($head.size() > 0)
            {
                boolean isExistentialVar = false;
			    for (int i = 0; i < $head.size(); i++)
			    {
			        Set<String> formulaExistVars = $head[i]::existVars;
                    if (!formulaExistVars.isEmpty())
                    {
	                    if(formulaExistVars.contains($VAR_ID.text))
	                    {
	                        isExistentialVar = true;
	                        break;
	                    }
                    }
			    }
			    
                if(!isExistentialVar)
                    $clause::conjunctFreeVars.add($VAR_ID.text);
            }
            else
            {
                $clause::bodyVars.add($VAR_ID.text);
            }
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
tree grammar Unnester;

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
    import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    private CommonTree getConjunctionTree(List conjuncts)
    {
        CommonTree and = (CommonTree)adaptor.create(AND, "AND");
        for (Object conjunct : conjuncts)
            adaptor.addChild(and, conjunct);
        
        return and;
    }
    
    private String newVarName()
    {
        String name;
        if ($rule.size() > 0)
        {   
            name = freshVarName($rule::vars);
            adaptor.addChild($rule::newVarsTree, adaptor.create(VAR_ID, name));
        }
        else
        {
            // name = freshVarName(query::vars);
            name = freshVarName();
            adaptor.addChild($query::newVarsTree, adaptor.create(VAR_ID, name));
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
    CommonTree newVarsTree;
}
@init
{
    $query::newVarsTree = (CommonTree)adaptor.nil();
}
    :   formula
    -> { $query::newVarsTree.getChildCount() > 0 }? ^(EXISTS { $query::newVarsTree } formula)
    -> formula
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
}
    :  ^(FORALL (VAR_ID { $rule::vars.add($VAR_ID.text); })+ clause)
    -> ^(FORALL VAR_ID+ { $rule::newVarsTree } clause)
    |   clause
    -> { $rule::newVarsTree.getChildCount() > 0 }? ^(FORALL { $rule::newVarsTree } clause)
    -> clause
    ;

clause
    :   ^(IMPLICATION head formula)
    |   head
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
    |   atomic
    |   external
    ;

atomic
scope
{
    List atoms;
}
@init
{
    $atomic::atoms = new ArrayList<CommonTree>();
}
@after
{
    $atomic::atoms.clear();
}
    : (
          t1=atom   { $atomic::atoms.add($t1.tree); } 
        | t2=equal  { $atomic::atoms.add($t2.tree); }
        | t3=subclass  { $atomic::atoms.add($t3.tree); }
      )
	  -> { $atomic::atoms.size() == 1 }?  { $atomic::atoms.get(0) }
	  -> { getConjunctionTree($atomic::atoms) }
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
    ->  { $VAR_ID.text.isEmpty() }? VAR_ID[newVarName()]
    ->  VAR_ID
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isTopLevel]
@after
{
    if (oid != null && !isTopLevel)
    {
        CommonTree trim = (CommonTree)adaptor.create(PSOA, "PSOA"),
                   typeTree = (CommonTree)adaptor.create(INSTANCE, "#");
                   
        adaptor.addChild(typeTree, $type.tree);
        adaptor.addChild(trim, adaptor.dupTree($oid.tree));
        adaptor.addChild(trim, typeTree);
        
        if ($tuples != null)
        {
            for (Object tuple : $tuples)
            {
                adaptor.addChild(trim, tuple);
            }
        }
        
        if ($slots != null)
        {
            for (Object slot : $slots)
            {
                adaptor.addChild(trim, slot);
            }
        }
        
        $atomic::atoms.add(trim);
    }
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuples+=tuple* slots+=slot*)
    -> { oid == null || isTopLevel }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> $oid
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
            if (!(hasNonDigitChar || lcName.isEmpty()))
            {
                throw new RuntimeException("'_" + lcName + "' is disallowed: the name of local constants cannot have only digits.");
            }
        }
    ->  { $LOCAL.text.isEmpty() }? LOCAL[freshConstName()]
    ->  LOCAL
    ;
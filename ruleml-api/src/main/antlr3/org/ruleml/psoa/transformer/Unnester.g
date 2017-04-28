/**
 *  This grammar file is used to generate a transformer for unnesting.
 *  Each grammar rule will become a Java method for reading and transforming 
 *  a subtree corresponding to a PSOA non-terminal.
 *
 *  Since one non-terminal can be used in different contexts while sharing
 *  the same transformation method, e.g. a psoa term can be used both
 *  on the top-level and embedded inside an atom, we use input parameters
 *  of certain rules to provide necessary context information and the transformation 
 *  is performed differently according to these parameters.
 *
 */

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
    package org.ruleml.psoa.transformer;
    
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
    // Keep the psoa term unchanged if it is oidless or on the top level 
    -> { oid == null || isTopLevel }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    // Leave behind the OID otherwise
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
    ->  { $LOCAL.text.isEmpty() }? LOCAL[freshConstName()]
    ->  LOCAL
    
/*      {
            String lcName = $LOCAL.text;
            if (lcName.isEmpty())
            {
                throw new RuntimeException("'_' must be renamed before unnesting");
            }
        } */
    ;
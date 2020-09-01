/**
 * This grammar file is used to generate a Java class for concatenating multiple KBs 
 * into one KB.
 **/

tree grammar Concatenater;

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
}


// documents is the root non-terminal representing multiple PSOA KBs
documents
scope
{
    CommonTree docTree;
}
@init
{
    // root node of new document tree
    $documents::docTree = (CommonTree)adaptor.create(DOCUMENT, "DOCUMENT");
}
    : document+
    // DOCUMENT and GROUP are stripped from document and recreated on the top-level
	-> ^({ $documents::docTree } ^(GROUP document+))
    ;

document
    :   ^(DOCUMENT base? 
        (prefix { adaptor.addChild($documents::docTree, $prefix.tree); })* 
        importDecl* group?)
    -> group?
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
    :   ^(GROUP group_element*) -> group_element*
    ;

group_element
    :   rule
    |   group
    ;
    
rule
    :  ^(FORALL VAR_ID+ clause)
    |   clause -> clause
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
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(OIDLESSEMBATOM ^(INSTANCE term) tuple* slot*)
    |   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
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
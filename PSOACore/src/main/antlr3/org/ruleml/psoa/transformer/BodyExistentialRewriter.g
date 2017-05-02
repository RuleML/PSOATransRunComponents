tree grammar BodyExistentialRewriter;

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
}

@members
{
  private boolean _isQuery = false;
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
    :   { _isQuery = true; } formula { _isQuery = false; }
    ;
    
rule
scope
{
  CommonTree existVarsTree;
}
@init
{
   $rule::existVarsTree = (CommonTree)adaptor.nil();
}
    :  ^(FORALL VAR_ID+ clause)
    -> ^(FORALL VAR_ID+ { $rule::existVarsTree } clause)
    |   clause
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
    |   FALSITY
    |   ^(EXISTS 
           (VAR_ID
              {
                if (!_isQuery)
                  adaptor.addChild($rule::existVarsTree, $VAR_ID.tree);
              }
           )+
          formula)
    ->  { _isQuery }? ^(EXISTS VAR_ID+ formula)
    ->  formula
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
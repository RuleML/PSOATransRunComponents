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
	package org.ruleml.psoa.normalizer;
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
    |   atomic
    |   ^(AND atomic+)
    ;
    
head
    :   atomic
    |   ^(AND atomic+)
    |   ^(EXISTS VAR_ID+ atomic)
    ;
    
formula
    :   ^(AND formula+)
    |   ^(OR formula+)
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
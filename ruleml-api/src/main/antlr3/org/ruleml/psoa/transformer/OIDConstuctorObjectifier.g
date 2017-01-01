tree grammar OIDConstuctorObjectifier;

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
	
	import java.util.ListIterator;
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
    :   ^(FORALL VAR_ID+ clause)
	|   clause
    ;

clause
    :
        ^(IMPLICATION head formula)
    |   atomic
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
            if ($VAR_ID.text.equals("obj"))
                throw new RuntimeException("?obj is a reserved variable and can not be used.");
        }
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomic]
scope
{
  boolean createOIDFunc;
  CommonTree oidFuncTree;
}
@init
{
  int numSlots = 0;
  ListIterator itr = null;
}    
    :   ^(PSOA oid=term? { $psoa::createOIDFunc = (isAtomic && oid == null); } 
            ^(INSTANCE type=term) { $psoa::createOIDFunc = false; }
            tuples+=tuple* (slot { numSlots++; })*)
    -> { isAtomic && oid == null && $tuples.size() == 1 && numSlots == 0 }?
        ^(PSOA
          ^(PSOA ^(INSTANCE ^(SHORTCONST {$psoa::oidFuncTree})) { (CommonTree)(itr = $tuples.listIterator()).next() })
          ^(INSTANCE $type))
    ->  ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
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
    {
      if ($psoa.size() > 0 && $psoa::createOIDFunc)
      {
        $psoa::oidFuncTree = (CommonTree)adaptor.create(IRI, $IRI.text.concat("-oid"));
      }
    }
    |   LITERAL
    |   NUMBER
    |   LOCAL
    {
      if ($psoa.size() > 0 && $psoa::createOIDFunc)
      {
        $psoa::oidFuncTree = (CommonTree)adaptor.create(LOCAL, $LOCAL.text.concat("-oid"));;
      }
    }
    ;
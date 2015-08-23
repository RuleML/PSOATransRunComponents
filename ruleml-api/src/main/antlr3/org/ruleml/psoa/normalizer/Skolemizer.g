tree grammar Skolemizer;

options 
{
  output = AST;
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	k = 1;
}

@header
{
	package org.ruleml.psoa.normalizer;
  import java.util.HashMap;
  import java.util.Map;
}

@members
{
  private final String s_skolem = "skolem";
  private int m_skolemCtr = 0;
  
  private CommonTree createSkolemTermTree()
  {
    CommonTree skolemTermTree = (CommonTree)adaptor.create(PSOA, "PSOA"),
               typeTree = (CommonTree)adaptor.create(INSTANCE, "INSTANCE"),
               skolemShortConstTree = (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST"),
               skolemFunc = (CommonTree)adaptor.create(LOCAL, s_skolem + (++m_skolemCtr)),
               tupleTree = (CommonTree)adaptor.create(TUPLE, "TUPLE");
    
    adaptor.addChild(typeTree, skolemShortConstTree);
    adaptor.addChild(skolemShortConstTree, skolemFunc);
    adaptor.addChild(skolemTermTree, typeTree);
    
    for (String s : $rule::forAllVarList)
    {
      CommonTree var = (CommonTree)adaptor.create(VAR_ID, s);
      adaptor.addChild(tupleTree, var);
    }
    
    adaptor.addChild(skolemTermTree, tupleTree);
    
    if ($rule::forAllVarList.isEmpty())
      return skolemShortConstTree; // Ground rule
    else
      return skolemTermTree;
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
  ArrayList<String> forAllVarList;
  Map<String, CommonTree> existVarMap;
}
@init
{
  $rule::forAllVarList = new ArrayList<String>();
  $rule::existVarMap = new HashMap<String, CommonTree>();
}
@after
{
  $rule::forAllVarList.clear();
  $rule::existVarMap.clear();
  $rule::forAllVarList = null;
  $rule::existVarMap = null;
}
    :  ^(FORALL (VAR_ID { $rule::forAllVarList.add($VAR_ID.text); })+ clause)
    |   clause
    ;

clause
    :   ^(IMPLICATION head formula)
    |   head
    ;
    
head
    :   atomic
    |   ^(AND atomic+)
    |   ^(EXISTS 
            (VAR_ID 
              { $rule::existVarMap.put($VAR_ID.text, createSkolemTermTree()); }
            )+
            atomic
         )
    ->  atomic
    ;
    
formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS VAR_ID+ formula)
    {
        System.err.println("Body existentials should be removed before Skolemization.");
    }
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
@init
{
  CommonTree streamTree;
}
    :   constant
    |   VAR_ID
    ->  { (streamTree = $rule::existVarMap.get($VAR_ID.text)) != null }? { (CommonTree)adaptor.dupTree(streamTree) } 
    ->  VAR_ID
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
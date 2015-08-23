tree grammar ExternalFlattener;

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
  import java.util.Map;
  import java.util.LinkedHashMap;
}

@members
{
  private static class ExternalRewriteInfo
  {
     public RewriteRuleSubtreeStream streamVar;
     public CommonTree externalTree; 
  }
  
	private String getNewVarName()
	{    
	  Set<String> forallVars = $rule::forallVars,
	              existVars = $rule::existsVars;
	  int i = 1;
	  String var;
	  
	  do
	  {
	    var = String.valueOf(i++);
	  } while (existVars.contains(var) || !forallVars.add(var));
	  
	  return var;
	}
	
	private CommonTree getVarNodeForExternal(CommonTree external)
	{
	  String extStr = external.toStringTree();
	  Map<String, ExternalRewriteInfo> externalInfoMap = $formula.size() > 0? $formula::externalInfoMap : $rule::externalInfoMap;
	  ExternalRewriteInfo extInfo = externalInfoMap.get(extStr);
	  CommonTree varNode;
	  
	  if (extInfo == null)
	  {
      varNode = (CommonTree)adaptor.create(VAR_ID, getNewVarName());
       
	    extInfo = new ExternalRewriteInfo();
	    extInfo.streamVar = new RewriteRuleSubtreeStream(adaptor, "NewVar", varNode);
	    extInfo.externalTree = external;
	    externalInfoMap.put(extStr, extInfo);
	  }
	   
    return (CommonTree) extInfo.streamVar.nextTree();
	}
	
	private CommonTree getExtEquals()
	{
    Map<String, ExternalRewriteInfo> externalInfoMap = $formula.size() > 0? $formula::externalInfoMap : $rule::externalInfoMap;
	  
	  assert !externalInfoMap.isEmpty();
	  
	  CommonTree root = (CommonTree)adaptor.nil(),
	             rootNewVars = (CommonTree)adaptor.nil();
	  
    for (ExternalRewriteInfo rewriteInfo : externalInfoMap.values())
	  {
      CommonTree equal = (CommonTree)adaptor.create(EQUAL, "EQUAL");
      
      adaptor.addChild(equal, rewriteInfo.streamVar.nextTree());
      adaptor.addChild(equal, rewriteInfo.externalTree);
      adaptor.addChild(root, equal);
      adaptor.addChild(rootNewVars, rewriteInfo.streamVar.nextTree());
	  }
	  
	  $rule::newVarsTree = rootNewVars;
	  return root;
	}
	
//	private CommonTree newVarsTree()
//	{
//    Map<String, ExternalRewriteInfo> externalInfoMap = $rule::externalInfoMap;
//	  
//	  for (ExternalRewriteInfo rewriteInfo : externalInfoMap.values())
//    {
//      adaptor.addChild(root, rewriteInfo.streamVar.nextTree());
//    }
//    
//    return root;
//	}
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
   Set<String> forallVars;
   Set<String> existsVars;
   Map<String, ExternalRewriteInfo> externalInfoMap;
   CommonTree newVarsTree;
}
@init
{
   Set<String> forallVars, existsVars;
   Map<String, ExternalRewriteInfo> externalInfoMap;
   
   $rule::forallVars = (forallVars = new HashSet<String>());
   $rule::existsVars = (existsVars = new HashSet<String>());
   // Use LinkedHashMap to preserve order
   $rule::externalInfoMap = (externalInfoMap = new LinkedHashMap<String, ExternalRewriteInfo>());
}
@after
{
    forallVars.clear();
    existsVars.clear();
    externalInfoMap.clear();
}
    :   ^(FORALL (VAR_ID { forallVars.add($VAR_ID.text); })+ clause)
    -> { externalInfoMap.isEmpty() }? ^(FORALL VAR_ID+ clause)
    -> ^(FORALL VAR_ID+ { $rule::newVarsTree } clause)
    |   clause
    -> { externalInfoMap.isEmpty() }? clause
    -> ^(FORALL { $rule::newVarsTree } clause)
    ;

clause
@init
{
  Map<String, ExternalRewriteInfo> externalInfoMap = $rule::externalInfoMap;
}
    :  ^(IMPLICATION head formula)
    -> { externalInfoMap.isEmpty() }? ^(IMPLICATION head formula)
    -> ^(IMPLICATION head ^(AND formula { getExtEquals() }))
    |  atomic
    -> { externalInfoMap.isEmpty() }? atomic
    -> { externalInfoMap.size() == 1 }? ^(IMPLICATION atomic { getExtEquals() } )
    -> ^(IMPLICATION atomic ^(AND { getExtEquals() } ))
    |  ^(AND atomic+)
    -> { externalInfoMap.isEmpty() }? ^(AND atomic+)
    -> { externalInfoMap.size() == 1 }? ^(IMPLICATION ^(AND atomic+) { getExtEquals() } )
    -> ^(IMPLICATION ^(AND atomic+) ^(AND { getExtEquals() } ))
    ;
    
head
    :   atomic
    |   ^(AND atomic+)
    |   ^(EXISTS 
          (VAR_ID { $rule::existsVars.add($VAR_ID.text); } )+ atomic)
    ;
    
formula
scope
{
  Map<String, ExternalRewriteInfo> externalInfoMap;
}
@init
{
  $formula::externalInfoMap = new LinkedHashMap<String, ExternalRewriteInfo>();
}
@after
{
  $formula::externalInfoMap = null;
}
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS VAR_ID+ formula)
    |   atomic
    -> { $formula::externalInfoMap.isEmpty() }? atomic
    -> ^(AND { getExtEquals() } atomic)
    |   external
    -> { $formula::externalInfoMap.isEmpty() }? external
    -> ^(AND { getExtEquals() } external)
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
    :   ^(EQUAL term[true] term[true])
    ;

subclass
    :   ^(SUBCLASS term[false] term[false])
    ;
    
term[boolean isTopLevelEqualTerm]
@init
{
    String newVar = null;
}
    :   constant
    |   VAR_ID
    |   psoa
    |   external
    -> { isTopLevelEqualTerm }? external
    -> { getVarNodeForExternal($external.tree) } // Replace external function application with a new variable
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(PSOA term[false]? ^(INSTANCE term[false]) tuple* slot*)
    ;

tuple
    :   ^(TUPLE term[false]+)
    ;
    
slot
    :   ^(SLOT term[false] term[false])
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
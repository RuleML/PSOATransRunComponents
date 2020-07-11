/**
 * This grammar file is used to generate a transformer for flattening nested external 
 * function applications.
 **/

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
  package org.ruleml.psoa.transformer;
	
  import java.util.Set;
  import java.util.HashSet;
  import java.util.Map;
  import java.util.LinkedHashMap;
  
  import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    // New / old variables in the current KB clause or query formula
    private Map<String, CommonTree> m_newVarNodes = new LinkedHashMap<String, CommonTree>();
    private Set<String> m_clauseVars = new HashSet<String>();
    
    // Equalities generated from externals in the current formula.
    private List<CommonTree> m_extEqs = new ArrayList<CommonTree>();
	
	private CommonTree getVarNodeForExternal(CommonTree external)
	{
	   // Generate a fresh variable name using an incomplete set of 
	   // clause variables. The name may be modified later in newVarsTree(). 
	   String var = freshVarName(m_clauseVars);
	   CommonTree varNode = (CommonTree)adaptor.create(VAR_ID, var);
	   m_newVarNodes.put(var, varNode);
	   
	   CommonTree equal = (CommonTree)adaptor.create(EQUAL, "EQUAL");
	   adaptor.addChild(equal, adaptor.dupNode(varNode));
	   adaptor.addChild(equal, external);
	   m_extEqs.add(equal);
	   
	   return (CommonTree)adaptor.dupNode(varNode);
	}
	
	private CommonTree getExtEquals()
	{	    
		CommonTree root = (CommonTree)adaptor.nil();
		
		for (CommonTree equal : m_extEqs)
		{
			adaptor.addChild(root, equal);
		}
		
		// Clear the list of equalities once done
		m_extEqs.clear();
		return root;
	}
	
  	private CommonTree newVarsTree()
  	{
        CommonTree root = (CommonTree)adaptor.nil();
  	    
  	    for (Map.Entry<String, CommonTree> entry : m_newVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();
           
           // Rename the variable name if it has been used in the clause
           if (m_clauseVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_clauseVars));  
           }
           adaptor.addChild(root, node);
        }
        
        m_newVarNodes.clear();
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
@after
{
    m_clauseVars.clear();
}
    :   formula
    ->  { m_newVarNodes.isEmpty() }? formula
    ->  ^(EXISTS { newVarsTree() } formula)
    ;
    
rule
@init
{
    resetVarGen();
}
@after
{
    m_clauseVars.clear();
}
    :   ^(FORALL (VAR_ID { m_clauseVars.add($VAR_ID.text); })+ clause)
    -> { m_newVarNodes.isEmpty() }? ^(FORALL VAR_ID+ clause)
    // Add new variables to a non-ground clause
    -> ^(FORALL VAR_ID+ { newVarsTree() } clause)
    |   clause
    -> { m_newVarNodes.isEmpty() }? clause
    // Add new variables to a ground clause, generating new FORALL wrapper
    -> ^(FORALL { newVarsTree() } clause)
    ;

// Handle external calls in rule conclusion
clause
    :  ^(IMPLICATION head formula)
    -> { $head.numExts == 0 }? ^(IMPLICATION head formula)
    -> ^(IMPLICATION head ^(AND formula { $head.eqTree }))
    |  head
    -> { $head.numExts == 0 }? head
    -> { $head.numExts == 1 }? ^(IMPLICATION head { $head.eqTree })
    -> ^(IMPLICATION head ^(AND { $head.eqTree }))
    ;

head returns [CommonTree eqTree, int numExts]
@after
{
   $numExts = m_extEqs.size();
   $eqTree = m_extEqs.isEmpty()? null : getExtEquals();
}
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS
          (VAR_ID { m_clauseVars.add($VAR_ID.text); } )+ head)
    ;
    
formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   FALSITY
    |   ^(EXISTS (VAR_ID { m_clauseVars.add($VAR_ID.text); } )+ formula)
    |   naf_formula
    |   atomic
    -> { m_extEqs.isEmpty() }? atomic
    // Create a conjunction if there exists a nested external function application
    -> ^(AND { getExtEquals() } atomic)
    |   external
    -> { m_extEqs.isEmpty() }? external
    // Create a conjunction if there exists a nested external function application
    -> ^(AND { getExtEquals() } external)
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
    :   ^(EQUAL term[false] term[true])
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
    |   VAR_ID { m_clauseVars.add($VAR_ID.text); }
    |   psoa
    |   external
    // Keep external function application unchanged if it is on the top level of an equality
    -> { isTopLevelEqualTerm }? external
    // Replace external function application with a new variable
    -> { getVarNodeForExternal($external.tree) }
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(PSOA term[false]? ^(INSTANCE term[false]) tuple* slot*)
    ;

tuple
    :   ^(TUPLE DEPSIGN term[false]*)
    ;
    
slot
    :   ^(SLOT DEPSIGN term[false] term[false])
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
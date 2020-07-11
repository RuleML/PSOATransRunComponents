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
    import java.util.Map;
    import java.util.LinkedHashMap;
    import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    private boolean m_isPositive;
    private Map<String, CommonTree> m_newVarNodes = new LinkedHashMap<String, CommonTree>();
    private Set<String> m_clauseVars = new HashSet<String>();
    
    private CommonTree getConjunctionTree(List conjuncts)
    {
        CommonTree and = (CommonTree)adaptor.create(AND, "AND");
        for (Object conjunct : conjuncts)
            adaptor.addChild(and, conjunct);
        
        return and;
    }
    
    private CommonTree newVarNode()
    {
        String name = freshVarName(m_clauseVars);
        CommonTree node = (CommonTree)adaptor.create(VAR_ID, name);
        
		m_newVarNodes.put(name, node);
		return (CommonTree)adaptor.dupNode(node);
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
    -> { m_newVarNodes.isEmpty() }? formula
    -> ^(EXISTS { newVarsTree() } formula)
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
    ->  { m_newVarNodes.isEmpty() }? ^(FORALL VAR_ID+ clause)
    ->  ^(FORALL VAR_ID+ { newVarsTree() } clause)
    |   clause
    ->  { m_newVarNodes.isEmpty() }? clause
    ->  ^(FORALL { newVarsTree() } clause)
    ;

clause
    :   ^(IMPLICATION head formula)
    |   head
    ;
    
head
@init
{
   m_isPositive = true;
}
@after
{
   m_isPositive = false;
}
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
@init
{
   boolean isAnonymousVar = false;
}
    :   constant
    |   VAR_ID
    {
       String name = $VAR_ID.text;
       isAnonymousVar = name.isEmpty();
       if (isAnonymousVar)
       {
          if (m_isPositive)
            throw new PSOARuntimeException("Anonymous variables can only occur in rule conditions or queries.");
       }
       else
       {
          m_clauseVars.add(name);
       }
    }
    ->  { isAnonymousVar }? { newVarNode() }
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
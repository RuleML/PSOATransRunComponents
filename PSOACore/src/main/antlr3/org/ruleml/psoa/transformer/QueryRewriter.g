/**
 * The grammar file is used to generate a transformer for query rewriting in static/dynamic 
 * objectification.
 **/

tree grammar QueryRewriter;

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
    import java.util.SortedSet;
    import java.util.Map;
    import java.util.LinkedHashMap;
	import org.ruleml.psoa.analyzer.*;
	
	import static org.ruleml.psoa.FreshNameGenerator.*;
	import static org.ruleml.psoa.utils.IOUtil.*;
}

@members
{
    // New / old variables in the current KB clause or query formula
    private Map<String, CommonTree> m_newVarNodes = new LinkedHashMap<String, CommonTree>();
    private Set<String> m_queryVars = null;
    private boolean m_isQuery = false;
    
    private KBInfoCollector m_KBInfo;
    private static String s_oidFuncName = "oidcons";
    
    public QueryRewriter(TreeNodeStream input, KBInfoCollector info)
    {
        this(input);
        m_KBInfo = info;
    }
    
    private CommonTree membershipRewriteTree(CommonTree oid, CommonTree type, PredicateInfo pi)
    {
        ArrayList<CommonTree> disjuncts = new ArrayList<CommonTree>();

        if (pi == null)
            return (CommonTree)adaptor.create(FALSITY, "FALSITY");
        
        SortedSet<Integer> arities = pi.getPositionalArities();
        ArrayList<CommonTree> newVars = new ArrayList<CommonTree>();
        
        for (int i = arities.last(); i > 0; i--)
        {
        	newVars.add(newVarNode());
        }
        
        for (Integer i : arities)
        {
            CommonTree disjunct, predApp, predAppType, predAppTuple, equality,
                       oidFuncApp, oidFuncType, oidFuncConst, oidFuncTuple;
            
            predApp = (CommonTree)adaptor.create(PSOA, "PSOA");
            predAppType = (CommonTree) adaptor.create(INSTANCE, "#");
            predAppType.addChild((CommonTree)adaptor.dupTree(type));
            predApp.addChild(predAppType);
            
            predAppTuple = (CommonTree)adaptor.create(TUPLE, "TUPLE");
            predAppTuple.addChild((CommonTree) adaptor.create(DEPSIGN, "+"));
            if (i > 0)
            {
                predApp.addChild(predAppTuple);
            }
            
            equality = (CommonTree) adaptor.create(EQUAL, "EQUAL");
            
            oidFuncApp = (CommonTree) adaptor.create(PSOA, "PSOA");
            oidFuncType = (CommonTree) adaptor.create(INSTANCE, "#");
            oidFuncConst = (CommonTree) adaptor.create(SHORTCONST, "SHORTCONST");
            
            oidFuncType.addChild(oidFuncConst);
            oidFuncConst.addChild((CommonTree)adaptor.create(LOCAL, s_oidFuncName));
            oidFuncApp.addChild(oidFuncType);
            
            oidFuncTuple = (CommonTree)adaptor.create(TUPLE, "TUPLE");
            oidFuncTuple.addChild((CommonTree) adaptor.create(DEPSIGN, "+"));
            oidFuncTuple.addChild((CommonTree)adaptor.dupTree(type));
            oidFuncApp.addChild(oidFuncTuple);

            equality.addChild((CommonTree)adaptor.dupTree(oid));
            equality.addChild(oidFuncApp);
            
            for (int j = 0; j < i; j++)
            {
            	CommonTree varNode = newVars.get(j);
                predAppTuple.addChild((CommonTree)adaptor.dupTree(varNode));
                oidFuncTuple.addChild((CommonTree)adaptor.dupTree(varNode));
            }
            
            disjunct = (CommonTree)adaptor.create(AND, "AND");
            disjunct.addChild(predApp);
            disjunct.addChild(equality);
            disjuncts.add(disjunct);
        }
        
        newVars.clear();
        
        // The predicate is only used as a nullary predicate
        if (m_newVarNodes.isEmpty())
            return disjuncts.get(0);
        
        if (disjuncts.size() == 1)
        {
            return disjuncts.get(0);
        }
        else
        {
            CommonTree disjunction = (CommonTree)adaptor.create(OR, "OR");
            
            for (CommonTree disjunct : disjuncts)
            {
                disjunction.addChild(disjunct);
            }
            
            return disjunction;
        }
    }
    
    private CommonTree oidFuncArgTree(CommonTree type, CommonTree tuple)
    {
        CommonTree tree = (CommonTree) adaptor.dupTree(tuple);
        
        tree.insertChild(1, (CommonTree)adaptor.dupTree(type));
        return tree;
    }
    
    private CommonTree newVarNode() {
        String var = freshVarName(m_queryVars);
        CommonTree varNode = (CommonTree)adaptor.create(VAR_ID, var);
        m_newVarNodes.put(var, varNode);
        
        return varNode;
    }
	
  	private CommonTree newVarsTree()
  	{
        CommonTree root = (CommonTree)adaptor.nil();
  	    
  	    for (Map.Entry<String, CommonTree> entry : m_newVarNodes.entrySet())
        {
           String var = entry.getKey();
           CommonTree node = entry.getValue();
           
           // Rename the variable name if it has been used in the clause
           if (m_queryVars.contains(var))
           {
              node.getToken().setText(freshVarName(m_queryVars));  
           }
           adaptor.addChild(root, node);
        }
        
        m_newVarNodes.clear();
        return root;
  	}
  	
  	private boolean hasIndepTuple(List tuples) {
  		if (tuples == null)
  			return false;
  		
  		for (Object tuple : tuples) {
  			if (((CommonTree)tuple).getChild(0).getText().equals("-"))
  				return true;
  		}
  		
  		return false;
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
@init
{
   m_queryVars = new HashSet<String>();
}
@after
{
   m_queryVars.clear();
   m_queryVars = null;
}
    :   formula
    ->  { m_newVarNodes.isEmpty() }? formula
    ->  ^(EXISTS { newVarsTree() } formula)
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
    
term returns [boolean isVariable]
@init
{
    $isVariable = false;
}
    :   constant
    |   VAR_ID
    { 
        $isVariable = true;
        if (m_queryVars != null)
            m_queryVars.add($VAR_ID.text);
    }
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomicFormula]
@init
{
    int i = 0;
    PredicateInfo pi = null;
    CommonTree oidTree = null;
}
    :   ^(PSOA (oid=term { oidTree = $oid.tree; })?
            ^(INSTANCE type=term
            	{
            	  /*
            	    Unnecessary here, since the message will be printed by ANTLR-generated Objectifier class
            	    
            		if ((oid == null || $oid.tree.getType() == VAR_ID) && $type.tree.getType() == VAR_ID)      // Predicate variable
            		{
            			printErrln("Warning: Predicate variables may lead to incompleteness under " +
            					   "static/dynamic objectification (completeness obtained under --staticOnly (-s) option)");
            		}
                  */
            		pi = m_KBInfo.getPredInfo($type.tree.toStringTree());
            	}
             )
          tuples+=tuple* slots+=slot*)
    -> // Function applications
       { !$isAtomicFormula }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> {
            m_KBInfo.hasHeadOnlyVariables()     // KB has head-only variables
         || !(pi == null || pi.isRelational())  // Atoms with non-relational KB predicates
         || (oid == null && pi != null && $tuples != null
             && $tuples.size() == 1 && !hasIndepTuple($tuples) && $slots == null)     // Relational atom
         || $type.tree.getType() == TOP         // Top-typed atom
         || $type.tree.getType() == VAR_ID      // Predicate variable
       }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> {   pi == null ||         // Predicate does not exist in KB
          (oid != null && !oid.isVariable) ||    // Psoa term with a non-variable OID
          (pi.isRelational() && hasIndepTuple($tuples)) ||  // Relational predicate having independent tuple
          $slots != null         // Psoa term with slots
       }? FALSITY
    -> // Rewrite a pure membership query atom
       { $tuples == null }?
       { membershipRewriteTree($oid.tree, $type.tree, pi) }
    -> // Rewrite query atoms with tuples and OID variable
        ^(AND
            ^(AND ^(PSOA ^(INSTANCE { adaptor.dupTree($type.tree) } ) tuple)
                  ^(EQUAL { adaptor.dupTree(oidTree != null? oidTree : (oidTree = newVarNode())) }
                          ^(PSOA ^(INSTANCE ^(SHORTCONST LOCAL[s_oidFuncName])) 
             	                 { oidFuncArgTree($type.tree, (CommonTree)$tuples.get(i++)) }
             	           )
             	   )
             )*
        )
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
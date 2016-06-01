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
	package org.ruleml.psoa.normalizer;
	
	import java.util.Set;
    import java.util.HashSet;
    import java.util.SortedSet;
	import org.ruleml.psoa.analyzer.*;
}

@members
{
    private KBInfoCollector m_KBInfo;
    private int m_varCtr;
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
        
        CommonTree exists = (CommonTree) adaptor.create(EXISTS, "EXISTS");
        ArrayList<String> newVars = new ArrayList<String>();
        SortedSet<Integer> arities = pi.getPositionalArities();

        
        for (int i = arities.last(); i > 0; i--)
        {    
            newVars.add(newVarName($query::queryVars));
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
            oidFuncTuple.addChild((CommonTree)adaptor.dupTree(type));
            oidFuncApp.addChild(oidFuncTuple);

            equality.addChild((CommonTree) adaptor.dupTree(oid));
            equality.addChild(oidFuncApp);
            
            for (int j = 0; j < i; j++)
            {
                String varName = newVars.get(j);
                predAppTuple.addChild((CommonTree)adaptor.create(VAR_ID, varName));
                oidFuncTuple.addChild((CommonTree)adaptor.create(VAR_ID, varName));
            }
            
            disjunct = (CommonTree)adaptor.create(AND, "AND");
            disjunct.addChild(predApp);
            disjunct.addChild(equality);
            disjuncts.add(disjunct);
        }
        
        // The predicate is only used as a nullary predicate
        if (newVars.isEmpty())
            return disjuncts.get(0);
         
        for (String var : newVars)
        {
            exists.addChild((CommonTree)adaptor.create(VAR_ID, var));
        }
        
        if (disjuncts.size() == 1)
        {
            exists.addChild(disjuncts.get(0));
        }
        else
        {
            CommonTree disjunction = (CommonTree)adaptor.create(OR, "OR");
            
            for (CommonTree disjunct : disjuncts)
            {
                disjunction.addChild(disjunct);
            }
            
            exists.addChild(disjunction);
        }
        
        return exists;
    }
    
    private CommonTree oidFuncArgTree(CommonTree type, CommonTree tuple)
    {
        
        CommonTree tree = (CommonTree) adaptor.create(TUPLE, "TUPLE");
        
        tree.addChild((CommonTree)adaptor.dupTree(type));
        
        for (int i = 0; i < tuple.getChildCount(); i++)
        {
            tree.addChild((CommonTree)adaptor.dupTree(tuple.getChild(i)));
        }
        
        return tree;
    }
    
    private String newVarName(Set<String> usedNames)
    {
      String name;
      
      do
      {
        name = String.valueOf(++m_varCtr);
      } while (!usedNames.add(name));
      
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
scope
{
   Set<String> queryVars;
}
@init
{
   $query::queryVars = new HashSet<String>();
   m_varCtr = 0;
}
@after
{
   $query::queryVars.clear();
   $query::queryVars = null;
}
    :   formula
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
        if ($query.size() > 0)
            $query::queryVars.add($VAR_ID.text);
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
}
    :   ^(PSOA oid=term?
            ^(INSTANCE type=term { pi = m_KBInfo.getPredInfo($type.tree.toStringTree()); } ) tuples+=tuple* slots+=slot*)
    -> // Function applications
       { !$isAtomicFormula }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> {
            m_KBInfo.hasHeadOnlyVariables()  // KB has head-only variables
         || !(pi == null || pi.isPurelyRelational())  // Atoms with non-purely-relational predicates
         || (oid == null && $slots == null)  // Purely relational atom
         || $type.tree.getType() == TOP      // Top-typed queries
         || $type.tree.getType() == VAR_ID   // Predicate variable
       }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*) 
    -> {   pi == null ||         // Predicate does not exist in KB
          !$oid.isVariable ||    // Psoa term with OID constants
          $slots != null        // Psoa term with slots
       }? FALSITY
    -> // Rewrite a pure membership query atom
       { $tuples == null }?
       { membershipRewriteTree($oid.tree, $type.tree, pi) }
    -> // Rewrite query atoms with tuples and OID variable
        ^(AND (^(PSOA ^(INSTANCE $type) tuple)
              ^(EQUAL $oid ^(PSOA ^(INSTANCE ^(SHORTCONST LOCAL[s_oidFuncName])) { oidFuncArgTree($type.tree, (CommonTree)$tuples.get(i++)) })))*)
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
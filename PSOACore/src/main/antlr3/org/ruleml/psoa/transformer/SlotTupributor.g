/**
 * The grammar file is used to generate a transformer for performing slotribution/tupribution.
 **/
 
tree grammar SlotTupributor;

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
    private boolean m_isQuery = false, m_reproduceClass, m_omitMemtermInRuleBody;
    
    public boolean getReproduceClass()
    {
        return m_reproduceClass;
    }
    
    public void setReproduceClass(boolean reproduceClass)
    {
        m_reproduceClass = reproduceClass;
    }
    
    private CommonTree getSlotTupributorTree(CommonTree oid, CommonTree pred, List list_tuples, List list_slots)
    {
		CommonTree root = (CommonTree)adaptor.nil();
		CommonTree predTree, topPredTree, memberTree;
		boolean isPredTop = (pred.getToken().getType() != TOP);
		
		memberTree = (CommonTree)adaptor.create(PSOA, "PSOA");
		adaptor.addChild(memberTree, oid);
		predTree = (CommonTree)adaptor.create(INSTANCE, "#");
		adaptor.addChild(predTree, pred);
		adaptor.addChild(memberTree, predTree);
		
	    topPredTree = (CommonTree)adaptor.create(INSTANCE, "#");
	    adaptor.addChild(topPredTree, (CommonTree)adaptor.create(TOP, "TOP"));
  
        if (list_slots != null)
        {
            CommonTree slot, sloterm;
        
            for (int i = 0; i < list_slots.size(); i++)
            {
               sloterm = (CommonTree)adaptor.create(PSOA, "PSOA");
               slot = (CommonTree)list_slots.get(i);
               adaptor.addChild(sloterm, adaptor.dupTree(oid));
               if (slot.getChild(0).getText().equals("+") || isPredTop)
               {
               	  adaptor.addChild(sloterm, adaptor.dupTree(predTree));               	  
               } 
               else
               {
                  adaptor.addChild(sloterm, adaptor.dupTree(topPredTree));
               }
               adaptor.addChild(sloterm, slot);
               adaptor.addChild(root, sloterm);
            }
        }
		
		if (list_tuples != null)
		{
		    CommonTree tuple, tupleterm;
		    
		    for (int i = 0; i < list_tuples.size(); i++)
		    {
		       tuple = (CommonTree)list_tuples.get(i);
		       tupleterm = (CommonTree)adaptor.create(PSOA, "PSOA");
		       adaptor.addChild(tupleterm, adaptor.dupTree(oid));
               if (tuple.getChild(0).getText().equals("+") || isPredTop)
               {
               	  adaptor.addChild(tupleterm, adaptor.dupTree(predTree));               	  
               } 
               else
               {
                  adaptor.addChild(tupleterm, adaptor.dupTree(topPredTree));
               }
		       adaptor.addChild(tupleterm, tuple);
		       adaptor.addChild(root, tupleterm);
		    }
		}
    
        if (isPredTop)
        {
            adaptor.addChild(root, memberTree);
        }
		
		switch (root.getChildCount())
		{
		    case 0:
		        // The psoa formula is o#Top
		        return memberTree;
		    case 1:
		        return root;
		    default:
		        CommonTree rootAnd = (CommonTree)adaptor.create(AND, "AND");
		        return (CommonTree)adaptor.becomeRoot(rootAnd, root);
		}
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
    :   { m_isQuery = true; } formula { m_isQuery = false; }
    ;
    
rule
    :   ^(FORALL VAR_ID+ clause)
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
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomicFormula]
scope
{
    boolean isAtomic;
}
@init
{
    $psoa::isAtomic = isAtomicFormula;
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuples+=tuple* slots+=slot*)
    -> { !$psoa::isAtomic || oid == null }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> { getSlotTupributorTree($oid.tree, $type.tree, $tuples, $slots) }

//    -> ^(AND 
//          ^(PSOA $oid ^(INSTANCE $type))
//          ^(PSOA $oid ^(INSTANCE TOP) tuple)*
//          ^(PSOA $oid ^(INSTANCE TOP) slot)*
//        )
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
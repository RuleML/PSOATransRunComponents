/**
 * The grammar file is used to generate a transformer for performing describution.
 **/
 
tree grammar Descributor;

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
    private boolean m_isNegOccurence = false, m_omitMemtermInNegativeAtoms;
    
    public boolean getOmitMemtermInNegativeAtoms()
    {
        return m_omitMemtermInNegativeAtoms;
    }
    
    public void setOmitMemtermInNegativeAtoms(boolean omitMemtermInNegativeAtoms)
    {
        m_omitMemtermInNegativeAtoms = omitMemtermInNegativeAtoms;
    }
    
    private CommonTree getDescributorTree(CommonTree oid, CommonTree pred, 
    		List list_tuples, List list_slots)
    {
		CommonTree root = (CommonTree)adaptor.nil();
		CommonTree predTree, topPredTree, memtermTree;
		boolean isPredTop = (pred.getToken().getType() == TOP);
		boolean hasDependent = false;
		
		memtermTree = (CommonTree)adaptor.create(PSOA, "PSOA");
		adaptor.addChild(memtermTree, oid);
		predTree = (CommonTree)adaptor.create(INSTANCE, "#");
		adaptor.addChild(predTree, pred);
		adaptor.addChild(memtermTree, predTree);
		
	    topPredTree = (CommonTree)adaptor.create(INSTANCE, "#");
	    adaptor.addChild(topPredTree, (CommonTree)adaptor.create(TOP, "TOP"));
		
		if (list_tuples != null)
		{
		    CommonTree tuple, tuptermTree;
		    
		    for (int i = 0; i < list_tuples.size(); i++)
		    {
		       tuple = (CommonTree)list_tuples.get(i);
		       tuptermTree = (CommonTree)adaptor.create(PSOA, "PSOA");
		       adaptor.addChild(tuptermTree, adaptor.dupTree(oid));
               if (tuple.getChild(0).getText().equals("-"))
               {
               	   adaptor.addChild(tuptermTree, adaptor.dupTree(topPredTree));
               }
               else if (isPredTop)
               {
                   tuple.setChild(0, (Tree)adaptor.create(DEPSIGN, "-"));
                   adaptor.addChild(tuptermTree, adaptor.dupTree(topPredTree));
               }
               else
               {
                  hasDependent = true;
               	  adaptor.addChild(tuptermTree, adaptor.dupTree(predTree));           	  
               }
               
		       adaptor.addChild(tuptermTree, tuple);
		       adaptor.addChild(root, tuptermTree);
		    }
		}
  
        if (list_slots != null)
        {
            CommonTree slot, slotermTree;
        
            for (int i = 0; i < list_slots.size(); i++)
            {
               slotermTree = (CommonTree)adaptor.create(PSOA, "PSOA");
               slot = (CommonTree)list_slots.get(i);
               adaptor.addChild(slotermTree, adaptor.dupTree(oid));
               if (slot.getChild(0).getText().equals("-"))
               {
               	   adaptor.addChild(slotermTree, adaptor.dupTree(topPredTree));
               }
               else if (isPredTop)
               {
                   slot.setChild(0, (Tree)adaptor.create(DEPSIGN, "-"));
                   adaptor.addChild(slotermTree, adaptor.dupTree(topPredTree));
               }
               else
               {
                  hasDependent = true;
               	  adaptor.addChild(slotermTree, adaptor.dupTree(predTree));               	  
               }
               
               adaptor.addChild(slotermTree, slot);
               adaptor.addChild(root, slotermTree);
            }
        }
    
        if (!isPredTop &&
            !(getOmitMemtermInNegativeAtoms() && m_isNegOccurence && hasDependent))
        {
            adaptor.addChild(root, memtermTree);
        }
		
		switch (root.getChildCount())
		{
		    case 0:
		        // The psoa formula is o#Top
		        return memtermTree;
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
    :   formula
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
@init
{
	m_isNegOccurence = false;
}
@after
{
	m_isNegOccurence = true;
}
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS VAR_ID+ head)
    ;

formula
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   FALSITY
    |   ^(EXISTS VAR_ID+ formula)
    |   naf_formula
    |   atomic
    |   external
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
    :   ^(OIDLESSEMBATOM ^(INSTANCE term) tuple* slot*)
    |   ^(PSOA oid=term? ^(INSTANCE op=term) tuples+=tuple* slots+=slot*)
    -> { !isAtomicFormula || oid == null }? ^(PSOA $oid? ^(INSTANCE $op) tuple* slot*)
	-> { getDescributorTree($oid.tree, $op.tree, $tuples, $slots) }
	
//    -> ^(AND 
//          ^(PSOA $oid ^(INSTANCE $pred))
//          ^(PSOA $oid ^(INSTANCE TOP) tuple)*
//          ^(PSOA $oid ^(INSTANCE TOP) slot)*
//        )
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
    ;
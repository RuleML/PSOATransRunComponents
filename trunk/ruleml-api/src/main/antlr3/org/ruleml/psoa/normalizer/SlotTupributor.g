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
	package org.ruleml.psoa.normalizer;
}

@members
{
    private boolean m_isQuery = false;
    
    private CommonTree getSlotTupributorTree(CommonTree oid, CommonTree type, List list_tuples, List list_slots)
    {
		CommonTree root = (CommonTree)adaptor.nil();
		CommonTree typeTree, memberTree, topTypeTree;
		
		memberTree = (CommonTree)adaptor.create(PSOA, "PSOA");
		adaptor.addChild(memberTree, oid);
		typeTree = (CommonTree)adaptor.create(INSTANCE, "INSTANCE");
		adaptor.addChild(typeTree, type);
		adaptor.addChild(memberTree, typeTree);
		
		topTypeTree = (CommonTree)adaptor.create(INSTANCE, "#");
		adaptor.addChild(topTypeTree, (CommonTree)adaptor.create(TOP, "TOP"));
    
    if (list_slots != null)
    {
        CommonTree slot;
        
        for (int i = 0; i < list_slots.size(); i++)
        {
           slot = (CommonTree)adaptor.create(PSOA, "PSOA");
           adaptor.addChild(slot, adaptor.dupTree(oid));
           adaptor.addChild(slot, adaptor.dupTree(topTypeTree));
           adaptor.addChild(slot, list_slots.get(i));
           adaptor.addChild(root, slot);
        }
    }
		
		if (list_tuples != null)
		{
		    CommonTree tuple;
		    
		    for (int i = 0; i < list_tuples.size(); i++)
		    {
		       tuple = (CommonTree)adaptor.create(PSOA, "PSOA");
		       adaptor.addChild(tuple, adaptor.dupTree(oid));
		       adaptor.addChild(tuple, adaptor.dupTree(topTypeTree));
		       adaptor.addChild(tuple, list_tuples.get(i));
		       adaptor.addChild(root, tuple);
		    }
		}
    
    if (type.getToken().getType() != TOP)
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
    :   { m_isQuery = true; } formula { m_isQuery = false; }
    ;
    
rule
    :   ^(FORALL VAR_ID+ clause)
	|   clause
    ;

clause
    :   ^(IMPLICATION head formula)
    |   atomic
    ;
    
head
    :   atomic
    |   ^(EXISTS VAR_ID+ atomic)
    ;

formula
    :   ^(AND formula+)
    |   ^(OR formula+)
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
    {
       if ($psoa::isAtomic && oid == null)
           throw new RuntimeException("Psoa formulas must be objectified before slotribution and tupribution");
    }
    -> { !$psoa::isAtomic }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
    -> { getSlotTupributorTree($oid.tree, $type.tree, $tuples, $slots) }

//    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuple* slot*)    
//    {
//       if ($psoa::isAtomic && oid == null)
//           throw new RuntimeException("Psoa formulas must be objectified before slotribution and tupribution");
//    }
//    -> { !$psoa::isAtomic }? ^(PSOA $oid? ^(INSTANCE $type) tuple* slot*)
//    -> ^(AND 
//          ^(PSOA $oid ^(INSTANCE $type))
//          ^(PSOA $oid ^(INSTANCE TOP) tuple)*
//          ^(PSOA $oid ^(INSTANCE TOP) slot)*
//        )
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
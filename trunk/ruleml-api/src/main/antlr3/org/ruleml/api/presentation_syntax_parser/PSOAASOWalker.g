tree grammar PSOAASOWalker;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = PSOARuleMLPS;
	k = 1;
} 

@header
{
	package org.ruleml.api.presentation_syntax_parser;
	import org.ruleml.api.*;
    import org.ruleml.api.AbstractSyntax.*;
}

@members
{
    private AbstractSyntax factory;
    
    public PSOAASOWalker(TreeNodeStream input, AbstractSyntax factory) {
        this(input, new RecognizerSharedState());
        this.factory = factory;
    }
}

document returns [Document result]
@init 
{
    List<Prefix> prefixes = new ArrayList<Prefix>();
    List<Import> imports = new ArrayList<Import>();
}
    : ^(DOCUMENT b=base?
        (prf=prefix { prefixes.add(prf); } )*
        (imp=importDecl { imports.add(imp); } )*
        g=group?)
        {
            $result = factory.createDocument(b, prefixes, imports, g);
        }
    ;

base returns [Base result]
    :   ^(BASE IRI_REF)
		{
		    $result = factory.createBase($IRI_REF.text);
		}
    ;

prefix returns [Prefix result] 
    :   ^(PREFIX ID IRI_REF)
		{
		    System.out.println("prefix");
		    $result = null;
		}
    ;

importDecl returns [Import result] 
    :   ^(IMPORT IRI_REF IRI_REF?)
		{
		    System.out.println("import");
		    $result = null;
		}
    ;

group returns [Group result]
    :   ^(GROUP group_element*)
		{
		    System.out.println("group");
		    $result = null;
		}
    ;

group_element returns [GroupElement result]
    :   rule { $result = $rule.result; }
    |   group { $result = $group.result; }
    ;


rule returns [Rule result]
    :   ^(FORALL ^(VAR_LIST VAR_ID+) clause)
		{
		    System.out.println("rule");
		    $result = null;
		}
	|   clause
    ;

clause returns [Clause result]
    :   ^(IMPLICATION head formula)
    |   atomic
    ;
    
head returns [Head result]
    :   atomic
    |   ^(EXISTS ^(VAR_LIST VAR_ID+) atomic)
    ;

formula returns [Formula result]
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS ^(VAR_LIST VAR_ID+) formula)
    |   atomic
    |   external
    ;

atomic returns [Atomic result]
    :   atom { $result = $atom.result; }
    |   equal { $result = $equal.result; }
    |   subclass { $result = $subclass.result; }
    ;

atom returns [Atom result]
    :   psoa
    ;

equal returns [Equal result]
    :   ^(EQUAL term term) { $result = null; }
    ;

subclass returns [Subclass result]
    :   ^(SUBCLASS term term)
    ;
    
term returns [Term result]
    :   constant
    |   VAR_ID 
    |   psoa { $result = $psoa.result; }
    |   external
    ;

external returns [Psoa result]
    :   ^(EXTERNAL psoa)
    ;
    
psoa returns [Psoa result]
    :   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
    ;

tuple returns [Tuple result]
    :   ^(TUPLE term+)
    ;
    
slot returns [Slot result]
    :   ^(SLOT term term)
    ;

constant returns [Const result]
    :   ^(LITERAL IRI)
    |   ^(SHORTCONST constshort)
    |   TOP
    ;
    
constshort returns [Const_Constshort result]
    :   IRI
    |   LITERAL
    |   NUMBER
    |   LOCAL
    ;
tree grammar BasicRuleMLPresentationASTGrammar;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = RuleMLPresentationSyntax;
	k = 1;
} 

@header
{
	package org.ruleml.api.presentation_syntax_parser;

	import static cs6795.group2.PSOATranslatorUtil.*;
    import java.io.*;
}

@members
{
    private PrintStream m_outStream = System.out;
    
    public void setOutStream(OutputStream out)
    {
        m_outStream = new PrintStream(out);
    }
    
    private void writeln(StringBuilder b)
    {
        m_outStream.println(b);
    }
    
    private void writeln()
    {
        m_outStream.println();
    }
}

document
    : ^(DOCUMENT base? prefix* importDecl* group?)
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
    :   r=rule { writeln(fofFactSentence(r)); }
    |   group
    ;

queries
    :   (r=rule { writeln(fofConjSentence(r)); } )+
    ;

rule returns [StringBuilder result]
    :   ^(FORALL ^(VAR_LIST VAR_ID+) clause) {  }
	|   c=clause { $result = c; }
    ;

clause returns [StringBuilder result]
    :   ^(IMPLICATION h=head f=formula) { ruleStr(h, f); }
    |   atomicFormula = atomic { $result = atomicFormula; }
    ;
    
head returns [StringBuilder result]
@init {
    StringBuilder b = builder();
}
    :   atomicHead=atomic { $result = atomicHead; }
    |   ^(EXISTS
          ^(VAR_LIST (VAR_ID { collectTerm(b, getVarName($VAR_ID.text)); })+)
          f=atomic)
	    {
	        $result = existStr(b, f);
	    }
    ;

formula returns [StringBuilder result]
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS ^(VAR_LIST VAR_ID+) formula)
    |   f=atomic { $result = f; }
    |   external
    ;

atomic returns [StringBuilder result]
    :   at=atom { $result = at; }
    |   equal
    |   subclass
    ;

atom returns [StringBuilder result]
    :   p=psoa { $result = p; }
    ;

equal
    :   ^(EQUAL term term)
    ;

subclass
    :   ^(SUBCLASS term term)
    ;
    
term returns [String result]
    :   c=constant { $result = c; }
    |   VAR_ID { $result = getVarName($VAR_ID.text); }
    |   p=psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa returns [StringBuilder result]
@init {
	List<String> tuples = list();
	List<String> slots = list();
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) (t=tuple {tuples.add(t); })* (s=slot {slots.add(s); })*)
    {
		$result = psoaStr(oid, type, tuples, slots);
    }
    ;

tuple returns [String result]
@init {
    StringBuilder b = builder();
}
    :   ^(TUPLE (t=term { collectTerm(b, t); })+) {
      $result = b.toString(); 
    }
    ;
    
slot returns [String result]
@init {
  StringBuilder b = builder();
}
    :   ^(SLOT s=term t=term) {
      collectTerms(b, s, t);
      $result = b.toString(); 
    }
    ;

constant returns [String result]
    :   ^(LITERAL IRI)
    |   ^(SHORTCONST c=constshort) { $result = c; }
    ;
    
constshort returns [String result]
    :   IRI
    |   LITERAL
    |   NUMBER { $result = $NUMBER.text; }
    |   LOCAL { $result = getConstName($LOCAL.text); }
    ;
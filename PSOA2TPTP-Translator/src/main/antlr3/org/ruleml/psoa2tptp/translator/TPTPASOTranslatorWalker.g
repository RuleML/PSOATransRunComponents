tree grammar TPTPASOTranslatorWalker;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = PSOARuleMLPS;
	k = 1;
    superClass = TranslatorWalker;
} 

@header
{
    package org.ruleml.psoa2tptp.translator;

	import logic.is.power.tptp_parser.TptpParserOutput.*;
	import java.io.*;
	import java.util.Set;
	import java.util.HashSet;
    import org.ruleml.psoa2tptp.translator.ANTLRBasedTranslator.TranslatorWalker;
    import static org.ruleml.psoa2tptp.translator.TPTPASOGenerator.*;
}

@members
{
    private TPTPASOGenerator m_generator = new TPTPASOGenerator();
        
    public void parseDocument() throws RecognitionException {
        document();
    }
    
    public void parseQuery() throws RecognitionException {
        query();
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
    :   r=rule { m_outStream.print(m_generator.getAnnotatedAxiom(r)); }
    |   group
    ;

query
    :   r=rule { m_outStream.print(m_generator.getAnnotatedQuery(r)); }
    ;

rule returns [FofFormula formula]
    :   ^(FORALL ^(VAR_LIST VAR_ID+) clause) {  }
	|   f=clause { $formula = f; }
    ;

clause returns [FofFormula formula]
    :   ^(IMPLICATION h=head f=formula) { m_generator.getImplies(h, f); }
    |   atomicFormula=atomic { $formula = atomicFormula; }
    ;
    
head returns [FofFormula formula]
    :   f=atomic { $formula = f; }
    |   ^(EXISTS ^(VAR_LIST (VAR_ID)+) f=atomic)
    ;

formula returns [FofFormula formula]
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS ^(VAR_LIST VAR_ID+) formula)
    |   atomic
    |   external
    ;

atomic returns [FofFormula formula]
    :   f=atom { $formula = f; }
    |   equal
    |   subclass
    ;

atom returns [FofFormula formula]
    :   f=psoa { $formula = f; }
    ;

equal
    :   ^(EQUAL term term)
    ;

subclass
    :   ^(SUBCLASS term term)
    ;
    
term returns [Term t]
    :   c=constant { $t = c; }
    |   VAR_ID { $t = m_generator.getVariable($VAR_ID.text); }
    |   p=psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa returns [FofFormula formula]
@init {
	Set<List<Term>> tuples = new HashSet<List<Term>>(4);
	Set<List<Term>> slots = new HashSet<List<Term>>(4);
}
    :   ^(PSOA oid=term? ^(INSTANCE type=term) (t=tuple {tuples.add(t); })* (s=slot {slots.add(s); })*)
    {
		$formula = m_generator.getPSOAFormula(oid, type, tuples, slots);
    }
    ;

tuple returns [List<Term> terms]
@init {
    $terms = new ArrayList<Term>(4);
}
    :   ^(TUPLE (t=term { $terms.add(t); })+)
    ;
    
slot returns [List<Term> terms]
@init {
    $terms = new ArrayList<Term>(2);
}
    :   ^(SLOT p=term v=term)
	    {
	        terms.add(p);
	        terms.add(v);
	    }
    ;

constant returns [Term t]
    :   ^(LITERAL IRI)
    |   ^(SHORTCONST ct=constshort) { $t = ct; }
    |   TOP { $t = null; }
    ;
    
constshort returns [Term t]
    :   IRI
    |   LITERAL
    |   NUMBER { $t = m_generator.getConst($NUMBER.text); }
    |   LOCAL { $t = m_generator.getConst($LOCAL.text); }
    ;
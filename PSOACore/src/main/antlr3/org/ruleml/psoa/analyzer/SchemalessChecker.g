/**
 * This grammar file checks that all rule variables are declared in Forall clauses. If the --noUniClos option was specified
 * at the command line, an exception is thrown if any unquantified variables are found within the enclosing clause. 
 * If --noUniClos was not specified, a warning is printed to standard error instead.
 * 
 **/

tree grammar SchemalessChecker;

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
	package org.ruleml.psoa.analyzer;
		
	import java.util.Set;
	import java.util.HashSet;
	
	import org.ruleml.psoa.transformer.PSOARuntimeException;
	import static org.ruleml.psoa.utils.IOUtil.*;
}

@members
{
    // used to print a warning about lack of forall, if we need it.
    private boolean m_hasForall;
    
    // used in formula, which can also occur in query. query has no rule body,
    // but we are only concerned with free variables in rule. thus, we need 
    // context sensitivity.
    private boolean m_inRuleBody;
    private boolean m_noUniversalClosure;
    
    private Set<String> m_unboundVars = new HashSet<String>();
    private Set<String> m_quantifiedVars = new HashSet<String>();  
        
    private void addNewVar(Set<String> existVars, String v) {
        if (!m_quantifiedVars.contains(v)) {
           existVars.add(v);
           m_quantifiedVars.add(v);           
        }
    }
    
    public void setNoUniversalClosure(boolean noUniversalClosure) {
        m_noUniversalClosure = noUniversalClosure;
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
@init 
{
    m_hasForall = false;
    
    m_quantifiedVars.clear();
    m_unboundVars.clear();
}
@after 
{
	if (!m_unboundVars.isEmpty()) {
	   if (m_noUniversalClosure) {
	      throw new PSOARuntimeException("Variables not explicitly quantified: " + m_unboundVars);
	   } else {
	      printErrln("Warning: Variables not explicitly quantified: " + m_unboundVars);
	   }
	}
}
    :  ^(FORALL { m_hasForall = true; } (VAR_ID { m_quantifiedVars.add($VAR_ID.text); })+ clause)
    |   clause
    ;

clause
    :   ^(IMPLICATION head { m_inRuleBody = true; } formula { m_inRuleBody = false; })
    {
        if (!m_hasForall && !m_unboundVars.isEmpty()) {
            printErrln("Warning: \"Forall\" wrapper is missing from the conclusion: " + $head.text);
        }
    }
    |   head
    ;
    
head
scope 
{
    Set<String> existVars;
}
@init 
{
    $head::existVars = new HashSet<String>();
}
@after 
{
    for (String v : $head::existVars) {
    	m_quantifiedVars.remove(v);    	
    }
}
    :   atomic
    |   ^(AND head+)
    |   ^(EXISTS (VAR_ID { addNewVar($head::existVars, $VAR_ID.text); })+ head)
    ;
    
formula
scope {
    Set<String> existVars;
}
@init {
    $formula::existVars = new HashSet<String>();
}
@after {
    for (String v : $formula::existVars) {
    	m_quantifiedVars.remove(v);    	
    }
}
    :   ^(AND formula+)
    |   ^(OR formula+)
    |   ^(EXISTS (VAR_ID { if (m_inRuleBody) addNewVar($formula::existVars, $VAR_ID.text); })+ formula)
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
    :   psoa
    ;

equal
    :   ^(EQUAL term term)
    ;

subclass
    :   ^(SUBCLASS term term)
    ;
    
term
    :   constant
    |   VAR_ID { if (!m_quantifiedVars.contains($VAR_ID.text)) m_unboundVars.add($VAR_ID.text); }
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(PSOA term? ^(INSTANCE term) tuple* slot*)
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
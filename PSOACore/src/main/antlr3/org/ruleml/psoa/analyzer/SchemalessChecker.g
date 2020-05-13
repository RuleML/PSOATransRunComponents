/**
 * This grammar file is used to perform schemaless checking. If the --fAllWrap option was specified
 * at the command line, an exception is thrown if any unquantified variables are found within the enclosing clause. 
 * If --fAllWrap was not specified, a warning is printed to standard error instead.
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
    private boolean m_isRuleBody;
    private boolean m_noUniversalClosure;
    
    private Set<String> m_freeVars = new HashSet<String>();
    private Set<String> m_quantifiedVars = new HashSet<String>();  
        
    private void recordExistentialVar(Set<String> existVars, String v) {
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
    :   ^(DOCUMENT 
    {
        if ($DOCUMENT.text.equals("Document")) 
        {
           printErrln("Warning: \"Document\" is deprecated. Use \"RuleML\" instead.");
        }
    }
    base? prefix* importDecl* group?)
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
    :   ^(GROUP 
    {
        if ($GROUP.text.equals("Group"))
        {
           printErrln("Warning: \"Group\" is deprecated. Use \"Assert\" instead.");
        }
    }
    group_element*)
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
    m_freeVars.clear();
}
@after 
{
	if (!m_freeVars.isEmpty()) {
	   if (m_noUniversalClosure) {
	      throw new PSOARuntimeException("Variable(s) not explicitly quantified: ?" + String.join(", ?", m_freeVars) + " in the clause: \n" + $rule.text);
	   } 
	   else {
	      printErrln("Warning: Variable(s) not explicitly quantified: ?" + String.join(", ?", m_freeVars) + " in the clause: \n" + $rule.text + "\n");
	   }
	}
}
    :  ^(FORALL { m_hasForall = true; } (VAR_ID { m_quantifiedVars.add($VAR_ID.text); })+ clause)
    |   clause
    {
        if (!m_hasForall && !m_freeVars.isEmpty()) {
            printErrln("Warning: \"Forall\" wrapper is missing from the clause: \n" + $clause.text);
        }
    }
    ;

clause
    :   ^(IMPLICATION head { m_isRuleBody = true; } formula { m_isRuleBody = false; })
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
    |   ^(EXISTS (VAR_ID { recordExistentialVar($head::existVars, $VAR_ID.text); })+ head)
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
    |   ^(EXISTS (VAR_ID { if (m_isRuleBody) recordExistentialVar($formula::existVars, $VAR_ID.text); })+ formula)
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
    |   VAR_ID { if (!$VAR_ID.text.isEmpty() && !m_quantifiedVars.contains($VAR_ID.text)) m_freeVars.add($VAR_ID.text); }
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
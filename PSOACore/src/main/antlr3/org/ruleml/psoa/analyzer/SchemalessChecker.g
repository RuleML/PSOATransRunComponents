/**
 * This grammar file is used to perform schemaless checking. 
 *
 * Forall wrapping: If the --fAllWrap option was specified at the command line, an 
 * error is raised if any unquantified variables are found within the enclosing clause. 
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
    private boolean m_forallWrap;
    
    private Set<String> m_freeVars = new HashSet<String>();
    private Set<String> m_quantifiedVars = new HashSet<String>();
    private Set<String> m_nonNafVars = new HashSet<String>();
    private Set<String> m_headVars = new HashSet<String>();
    
    private int m_nafLevels = 0;
        
    private void recordExistentialVar(Set<String> existVars, String v) {
        if (!m_quantifiedVars.contains(v)) {
           existVars.add(v);
           m_quantifiedVars.add(v);           
        }
    }
    
    public void setForallWrap(boolean forallWrap) {
        m_forallWrap = forallWrap;
    }
}

document
    :   ^(DOCUMENT
    //  Deprecation Warning: If "Document" was used instead of "RuleML", warn the user at load time to use "RuleML" instead, as support for "Document" will eventually be removed. 
    {
        if ($DOCUMENT.text.equals("Document")) 
        {
           printErrln("Warning: \"Document\" is deprecated and will be removed in a future release. Use \"RuleML\" instead.");
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
    //  Deprecation Warning: If "Group" was used instead of "Assert", warn the user at load time to use "Assert" instead, as support for "Group" will eventually be removed. 
    {
        if ($GROUP.text.equals("Group"))
        {
           printErrln("Warning: \"Group\" is deprecated and will be removed in a future release. Use \"Assert\" instead.");
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
    
    m_nonNafVars.clear();
    m_quantifiedVars.clear();
    m_freeVars.clear();
    m_headVars.clear();
}
@after 
{
    if (!m_freeVars.isEmpty()) {
       if (m_forallWrap) {
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
    |   naf_formula
    |   atomic
    |   external
    ;
    
naf_formula
scope
{
   Set<String> nafVars;
}
@init 
{
    ++m_nafLevels;
    $naf_formula::nafVars = new HashSet<String>();
}
@after
{
    --m_nafLevels;
    
    HashSet<String> headNafVars = new HashSet(m_headVars);
    headNafVars.retainAll($naf_formula::nafVars);
    
    if (!m_nonNafVars.containsAll(headNafVars)) {
       headNafVars.removeAll(m_nonNafVars);
       printErrln("Warning: Conclusion variable(s): ?" + String.join(", ?", headNafVars) + "\n" + 
                  "do(es) not occur in a conjunct preceding the Naf, which should be instantiated " + 
                  "to prevent floundering: \n" + $naf_formula.text + "\n");
    }
    
    $naf_formula::nafVars.removeAll(m_nonNafVars);
    $naf_formula::nafVars.removeAll(m_headVars);
    
    if (!$naf_formula::nafVars.isEmpty()) {
       
       printErrln("Warning: Variable(s): ?" + String.join(", ?", $naf_formula::nafVars) + "\n" +
                  "do(es) not occur in a conjunct preceding the Naf, which should be instantiated " +
                  "to prevent floundering: \n" + $naf_formula.text + "\n");       
    }
}
    :   ^(NAF formula)
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
    |   VAR_ID 
        { if (!$VAR_ID.text.isEmpty() && !m_quantifiedVars.contains($VAR_ID.text)) 
             m_freeVars.add($VAR_ID.text);
          
          if (m_nafLevels > 0)
             $naf_formula::nafVars.add($VAR_ID.text);
          else if (m_isRuleBody) // never deem variables in rule conclusions as non-NAF variables.
             m_nonNafVars.add($VAR_ID.text);
          else
             m_headVars.add($VAR_ID.text);
        }
    |   psoa
    |   external
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
    :   ^(OIDLESSEMBATOM ^(INSTANCE term) tuple* slot*)
    |   ^(PSOA term? ^(INSTANCE term) tuple* (slots+=slot)*)        
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

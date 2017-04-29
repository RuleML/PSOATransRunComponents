/**
 * This grammar file is used to generate a transformer for renaming the local constants 
 * in a KB.
 **/
 
tree grammar Renamer;

options 
{
    output = AST;
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	k = 1;
}

@header
{
	package org.ruleml.psoa.transformer;
	
	import java.util.Set;
	import java.util.HashSet;
	import java.util.Map;
	import java.util.HashMap;
    import static org.ruleml.psoa.FreshNameGenerator.*;
}

@members
{
    private Map<String, String> m_nameMap = new HashMap<String, String>();
    private Set<String> m_excluded = new HashSet<String>();
    
    public void setExcluded(Set<String> excluded)
    {
    	m_excluded = excluded;
    }
    
    private String getNewName(String name)
    {
        String newName = m_nameMap.get(name);
        if (newName == null)
        {
            newName = freshConstName(m_excluded);
            if (!name.isEmpty())
            {
                m_nameMap.put(name, newName);
            }
        }
        
        return newName;
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
    |   LOCAL    ->  LOCAL[getNewName($LOCAL.text)]
    ;
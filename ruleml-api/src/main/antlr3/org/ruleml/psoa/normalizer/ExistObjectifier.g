tree grammar ExistObjectifier;

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
    private boolean m_isRule = false, m_isQuery = false;
    private int m_localConstID = 0, m_varID = 0;
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
    :   rule { m_varID = 0; }
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
    :   { m_isRule = true; }
        ^(IMPLICATION head formula)
        { m_isRule = false; }
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
        {
            if ($VAR_ID.text.equals("obj"))
                throw new RuntimeException("?obj is a reserved variable and can not be used.");
        }
    |   psoa[false]
    |   external
    ;

external
    :   ^(EXTERNAL psoa[false])
    ;
    
psoa[boolean isAtomic]
    :   ^(PSOA oid=term? ^(INSTANCE type=term) tuple* slot*)
    -> { oid != null }? ^(PSOA $oid ^(INSTANCE $type) tuple* slot*)
    -> { !isAtomic }? ^(PSOA ^(INSTANCE $type) tuple* slot*)
    -> { (m_isRule || m_isQuery) && (++m_varID > 0) }?
        ^(EXISTS VAR_ID["obj"+ String.valueOf(m_varID)]
            ^(PSOA VAR_ID["obj" + String.valueOf(m_varID)] ^(INSTANCE $type) tuple* slot*)
        )
    -> ^(PSOA ^(SHORTCONST LOCAL[String.valueOf(++m_localConstID)]) ^(INSTANCE $type) tuple* slot*)
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
        {
            String lcName = $LOCAL.text;
            boolean hasNonDigitChar = false;
            for (int i = lcName.length() - 1; i >= 0; i--)
            { 
                if (!Character.isDigit(lcName.charAt(i)))
                {
                    hasNonDigitChar = true;
                    break;
                }
            }
            if (!hasNonDigitChar)
            {
                throw new RuntimeException("'_" + lcName + "' is disallowed: the name of local constants cannot have only digits.");
            }
        }
    ;
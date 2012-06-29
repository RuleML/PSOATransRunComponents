/* This is a grammar file for PSOA RuleML. Use ANTLR 2.7.x to generate 
 * the parser code.
 */

grammar PSOARuleMLPS;

options
{
    output = AST;
    k = 1;
    ASTLabelType = CommonTree;
    // backtrack=true;
    // Potential performance problem! 
    // See http://www.antlr.org/wiki/display/ANTLR3/How+to+remove+global+backtracking+from+your+grammar
}

tokens 
{
    VAR_LIST;
    PSOA;
    TUPLE;
    SLOT;
    LITERAL;
    SHORTCONST;
    IRI;  
    NUMBER;
    LOCAL;
}

@header
{
	package org.ruleml.api.presentation_syntax_parser;
    import org.ruleml.api.*;
    import org.ruleml.api.AbstractSyntax.*;
}

@lexer::header {
    package org.ruleml.api.presentation_syntax_parser;
    import org.ruleml.api.*;
    import org.ruleml.api.AbstractSyntax.*;
}

@lexer::members {
}

@members
{
    
    private CommonTree getTupleTree(List list_terms, int length)
    {
        CommonTree root = (CommonTree)adaptor.nil();
        for (int i = 0; i < length; i++)
            adaptor.addChild(root, list_terms.get(i));
        return root;
    }
    
    private String getStrValue(String str)
    {
        return str.substring(1, str.length() - 1);
    }
}

top_level_item : document? EOF;

queries
    :   rule+;

document
    :   DOCUMENT LPAR base? prefix* importDecl* group? RPAR
        -> ^(DOCUMENT base? prefix* importDecl* group?)
    ;

base
    :   BASE LPAR IRI_REF RPAR -> ^(BASE IRI_REF)
    ;

prefix
    :   PREFIX LPAR ID IRI_REF RPAR -> ^(PREFIX ID IRI_REF)
    ;

importDecl
    :   IMPORT LPAR kb=IRI_REF (pf=IRI_REF)? RPAR -> ^(IMPORT $kb $pf?)
    ;

group
    :   GROUP LPAR group_element* RPAR -> ^(GROUP group_element*)
    ;

group_element
    :   rule
    |   group
    ;

rule
    :   FORALL VAR_ID+ LPAR clause RPAR
        -> ^(FORALL ^(VAR_LIST VAR_ID+) clause)
    |   clause
    ;

clause
@init { boolean isRule = false; }
@after
    {
        if (isRule)
        {
            if (!$f1.isValidHead)
                throw new RuntimeException("Unacceptable head formula:" + $f1.text);
        }
        else if (!$f1.isAtomic)
        {
            throw new RuntimeException("Unacceptable clause:" + $clause.text);
        }
    }
    :   f1=formula ( IMPLICATION f2=formula { isRule = true; } )?
    -> {isRule}? ^(IMPLICATION $f1 $f2)
    ->           $f1
    ;

formula returns [boolean isValidHead, boolean isAtomic]
@init { $isValidHead = true; $isAtomic = false; }
    :   AND LPAR (f=formula { if(!$f.isValidHead) $isValidHead = false; } )+ RPAR -> ^(AND formula*)
    |   OR LPAR formula+ RPAR { $isValidHead = false; } -> ^(OR formula*)
    |   EXISTS VAR_ID+ LPAR f=formula RPAR { $isValidHead = $f.isAtomic; }
        -> ^(EXISTS ^(VAR_LIST VAR_ID+) $f)
    |   atomic { $isAtomic = true; } -> atomic
    |   (external_term { $isValidHead = false; } -> external_term)
        (psoa_rest { $isAtomic = true; } -> ^(PSOA $formula psoa_rest))?
    ;
    
atomic
@after
{
    if ($tree.getChildCount() == 1 && $non_ex_term.isSimple)
        throw new RuntimeException("Simple term cannot be an atomic formula:" + $non_ex_term.text);
}
    :   non_ex_term=internal_term ((EQUAL | SUBCLASS)^ term)?
    ;

term
    :   internal_term -> internal_term
    |   external_term -> external_term
    ;

simple_term
    :   constant
    |   VAR_ID
    ;

external_term
    :   EXTERNAL LPAR simple_term LPAR term* RPAR RPAR
    -> ^(EXTERNAL ^(PSOA ^(INSTANCE simple_term) ^(TUPLE term*)))
    ;

internal_term returns [boolean isSimple]
@init { $isSimple = true; }
    :   (simple_term -> simple_term)
        (LPAR tuples_and_slots? RPAR { $isSimple = false; }
         -> ^(PSOA ^(INSTANCE $internal_term) tuples_and_slots?))?
        (psoa_rest { $isSimple = false; } -> ^(PSOA $internal_term psoa_rest))*
    ;

psoa_rest
    :   INSTANCE simple_term (LPAR tuples_and_slots? RPAR)?
    -> ^(INSTANCE simple_term) tuples_and_slots?
    ;

tuples_and_slots
    :   tuple+ slot* -> tuple+ slot*
    |   terms+=term+ { boolean hasSlot = false; }
        (SLOT_ARROW first_slot_value=term { hasSlot = true; } slot* )? // Syntactic sugar for psoa terms which has only one tuple
    -> {!hasSlot}? ^(TUPLE {getTupleTree($terms, $terms.size()) } ) // single tuple
    -> {$terms.size() == 1}?
        ^(SLOT {$terms.get(0)} $first_slot_value) slot* // slot only
    ->  ^(TUPLE {getTupleTree($terms, $terms.size() - 1)}) ^(SLOT {$terms.get($terms.size() - 1)} $first_slot_value) slot* // tuples and slots
    ;

tuple
    :   LSQBR term+ RSQBR -> ^(TUPLE term+)
    ;

slot
    :
        name=term SLOT_ARROW value=term -> ^(SLOT $name $value)
    ;

/*
**  The rule of constant strings can be rewrited to
**  Const ::= '"' UNICODESTRING '"' (^^ SYMSPACE))? | '"' UNICODESTRING '"@' langtag)? | CURIE | NumericLiteral | '_' NCName | IRI_REF
**  Symbol const_string is introduced to capture the first branch.
*/

constant
    :   const_string -> const_string
    |   CURIE   -> ^(SHORTCONST IRI[$CURIE.text]) 
    |   NUMBER  -> ^(SHORTCONST NUMBER[$NUMBER.text])
    |   ID /* _NCNAME */ {
            if (!$ID.text.startsWith("_"))
                throw new RuntimeException("Incorrect constant format:" + $ID.text);
        }
        -> ^(SHORTCONST LOCAL[$ID.text.substring(1)])
    |   IRI_REF -> ^(SHORTCONST IRI[$IRI_REF.text])
    |   TOP
    ;


//  Complete and abbreviated string constant
const_string
@init { boolean isAbbrivated = true; } 
    : STRING ((SYMSPACE_OPER symspace=(IRI_REF | CURIE) { isAbbrivated = false; } ) | '@')?    
    -> {isAbbrivated}? ^(SHORTCONST LITERAL[getStrValue($STRING.text)])
    -> LITERAL[getStrValue($STRING.text)] IRI[$symspace.text]
    //|   STRING '@' ID /* langtag */ -> ^(SHORTCONST LITERAL[$STRING.text])
    ;

//--------------------- LEXER: -----------------------
// Comments and whitespace:
WHITESPACE  :  (' '|'\t'|'\r'|'\n')+ { $channel = HIDDEN; } ;
MULTI_LINE_COMMENT :  '<!--' (options {greedy=false;} : .* ) '-->' { $channel=HIDDEN; } ;

// Keywords:
DOCUMENT : 'Document' ;
BASE : 'Base' ;
IMPORT : 'Import' ;
PREFIX : 'Prefix' ;
GROUP : 'Group' ;
FORALL : 'Forall' ;
EXISTS : 'Exists' ;
AND : 'And' ;
OR : 'Or' ;
EXTERNAL : 'External';
TOP : 'Top';

//  Constants:
NUMBER: DIGIT+ ('.' DIGIT*)?;
CURIE : ID? ':' ID?;

STRING: '"' (~('"' | '\\' | EOL) | ECHAR)* '"';

//  Identifiers:
IRI_REF : '<' IRI_START_CHAR (IRI_CHAR)+ '>' ;
VAR_ID : '?' ID_CHAR*;
ID : ID_START_CHAR ID_CHAR* ;

//   Operators:
IMPLICATION : ':-';
EQUAL : '=';
SUBCLASS : '##';
INSTANCE : '#';
SLOT_ARROW : '->';
SYMSPACE_OPER : '^^';

//   Punctuation:
LPAR : '(' ;
RPAR : ')' ;
LESS : '<' ;
GREATER : '>' ;
LSQBR : '[' ;
RSQBR : ']' ;

//  Basics
fragment ALPHA : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT : '0'..'9' ;
fragment IRI_CHAR : ALPHA | DIGIT | '+' | '-' | '.' | '@' | ':' | '_' | '~' | '%' | '!' |  '$' | '&' | '\'' | '(' | ')' | '*' | ',' | ';' | '=' | '?' | '#' | '/';

// Currently, these are only URI characters. The set has to be extended
// to include all IRI characters.
fragment IRI_START_CHAR : ALPHA ;

//  In the slots a hyphen may appear directly after a term, so we disallow it to be an ID_CHAR 
fragment ID_CHAR
    : ID_START_CHAR
    | DIGIT
    | '\u00B7'           // as in the SPARQL 1.1 grammar
    | '\u203F'..'\u2040' // as in the SPARQL 1.1 grammar
  //| '-'
  //| ':'
    | '.'
    ;

fragment ID_START_CHAR
    : ALPHA
    | '\u00C0'..'\u00D6'
    | '\u00D8'..'\u00F6'
    | '\u00F8'..'\u02FF'
    | '\u0370'..'\u037D'
    | '\u037F'..'\u1FFF'
    | '\u200C'..'\u200D'
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    | '_'
    ;

fragment ECHAR : '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'');

fragment EOL : '\n' | '\r';

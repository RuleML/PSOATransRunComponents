/*
 * Grammar for parsing PSOA RuleML presentation syntax.
 */

grammar PSOAPS;

options
{
    output = AST;
    k = 1;  // limit to one-step look ahead for efficient parsing
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
    FALSITY;
    DEPSIGN;
}

@header
{
    package org.ruleml.psoa.parser;
    
    import java.util.List;
    import java.util.ArrayList;
    import java.util.Set;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.HashMap;
}

@lexer::header {
    package org.ruleml.psoa.parser;
}

@members
{
	private List<String[]> m_imports = new ArrayList<String[]>();
	private Set<String> m_localConsts = new HashSet<String>();
    private Map<String, String> m_namespaceTable = new HashMap<String, String>();
    private static Set<String> s_numberTypeIRIs = new HashSet();
    private static String s_stringIRI = "http://www.w3.org/2001/XMLSchema#string";
    
    static
    {
       s_numberTypeIRIs.add("http://www.w3.org/2001/XMLSchema#integer");
       s_numberTypeIRIs.add("http://www.w3.org/2001/XMLSchema#double");
       s_numberTypeIRIs.add("http://www.w3.org/2001/XMLSchema#long");
    }
    
    private static boolean fastStringEquals(String s1, String s2)
    {
        return s1.hashCode() == s2.hashCode() && s1.equals(s2);
    }
    
    /** 
     * Get the full IRI form of an IRI prefix
    */
    protected String getNamespace(String prefix)
    {
    	String ns = m_namespaceTable.get(prefix);
    	if (ns == null)
    		throw new RuntimeException("Namespace prefix " + prefix + " used but not defined");
    		
    	return ns;
    }
        
    /** 
     * Set the full IRI of an IRI prefix
    */
	protected void setNamespace(String ns, String iri) {
		String oldIRI = m_namespaceTable.put(ns, iri);
		if (oldIRI != null && !oldIRI.equals(iri))
		{
			throw new RuntimeException("Redefinition of namespace prefix " + ns);
		}
	}
	
	/** 
     * Get full IRI from a namespace-prefixed IRI
    */
	protected String getFullIRI(String ns, String localName)
	{
		return getNamespace(ns) + localName;
	}
    
    public Map<String, String> getNamespaceTable()
    {
    	return m_namespaceTable;
    }

	public List<String[]> getImports()
	{
		return m_imports;
	}

	public Set<String> getLocalConsts()
	{
		return m_localConsts;
	}
	
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

top_level_item : document | query[null];

query[Map<String, String> nsTable]
@init
{
	if (nsTable != null)
		m_namespaceTable = nsTable;
}
    :   formula;

document
    :   DOCUMENT LPAR base? prefix* importDecl* group? RPAR
        -> ^(DOCUMENT base? prefix* importDecl* group?)
    ;

base
    :   BASE LPAR IRI_REF RPAR -> ^(BASE IRI_REF)
    ;

prefix
    :   PREFIX LPAR NAMESPACE IRI_REF RPAR
	    {
	    	setNamespace($NAMESPACE.text, $IRI_REF.text);
	    }
    -> ^(PREFIX NAMESPACE IRI_REF)
    ;

importDecl
    :   IMPORT LPAR kb=IRI_REF (pf=IRI_REF)? RPAR 
    	{
			String[] importDoc = new String[2];
			importDoc[0] = $kb.text;
			if (pf != null)
				importDoc[1] = $pf.text;
			m_imports.add(importDoc);
    	}
	-> ^(IMPORT $kb $pf?)
    ;

group
    :   GROUP LPAR group_element* RPAR -> ^(GROUP group_element*)
    ;

group_element
    :   rule
    |   group
    ;

rule
    :   FORALL variable+ LPAR clause RPAR
        -> ^(FORALL variable+ clause)
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
//        else if (!$f1.isAtomic)
//        {
//            throw new RuntimeException("Unacceptable clause:" + $clause.text);
//        }
    }
    :   (f1=formula -> formula) ( IMPLICATION f2=formula { isRule = true; } -> ^(IMPLICATION $clause $f2) )?
    //-> {isRule}? ^(IMPLICATION $f1 $f2)
    //->           $f1
    ;

formula returns [boolean isValidHead, boolean isAtomic]
@init
{
   $isValidHead = true; $isAtomic = false;
   boolean hasSubformulas = false;
}
    :   AND LPAR (f=formula { if(!$f.isValidHead) $isValidHead = false; } )+ RPAR -> ^(AND["AND"] formula*)
    |   OR LPAR (formula { hasSubformulas = true; })* RPAR { $isValidHead = false; }
        -> { hasSubformulas }? ^(OR["OR"] formula*)
        -> FALSITY
    |   EXISTS variable+ LPAR f=formula RPAR { } // $isValidHead = $f.isAtomic; }
        -> ^(EXISTS["EXISTS"] variable+ $f)
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
    |   variable
    ;

external_term
    :   EXTERNAL LPAR simple_term LPAR term* RPAR RPAR
    -> ^(EXTERNAL ^(PSOA ^(INSTANCE simple_term) ^(TUPLE DEPSIGN["+"] term*)))
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
    -> {!hasSlot}? ^(TUPLE DEPSIGN["+"] {getTupleTree($terms, $terms.size()) } ) // single tuple
    -> {$terms.size() == 1}?
        ^(SLOT DEPSIGN[$SLOT_ARROW.text.substring(0, 1)] {$terms.get(0)} $first_slot_value) slot* // slot only
    ->  ^(TUPLE DEPSIGN["+"] {getTupleTree($terms, $terms.size() - 1)}) ^(SLOT DEPSIGN[$SLOT_ARROW.text.substring(0, 1)] {$terms.get($terms.size() - 1)} $first_slot_value) slot* // tuples and slots
    ;

tuple
    :   DEPSIGN LSQBR term+ RSQBR -> ^(TUPLE DEPSIGN term+)
    |   LSQBR term+ RSQBR -> ^(TUPLE DEPSIGN["+"] term+)
    ;

slot
    :
        name=term SLOT_ARROW value=term -> ^(SLOT DEPSIGN[$SLOT_ARROW.text.substring(0, 1)] $name $value)
    ;

/*
**  The rule of constant strings can be rewritten to
**  Const ::= '"' UNICODESTRING '"' (^^ SYMSPACE))? | '"' UNICODESTRING '"@' langtag)? | CURIE | NumericLiteral | '_' NCName | IRI_REF
**  Symbol const_string is introduced to capture the first branch.
*/

constant
    :	iri   -> ^(SHORTCONST iri)
    |   const_string -> const_string
    |   NUMBER  -> ^(SHORTCONST NUMBER)
    |   PN_LOCAL /* _NCNAME */ {
            if (!$PN_LOCAL.text.startsWith("_"))
                throw new RuntimeException("Incorrect constant format:" + $PN_LOCAL.text);
            m_localConsts.add($PN_LOCAL.text.substring(1));
        }
        -> ^(SHORTCONST LOCAL[$PN_LOCAL.text.substring(1)])
    |   TOP
    ;


//  Complete and abbreviated string constant
const_string
@init
{
    boolean isAbbrivated = true;
}
    : STRING ((SYMSPACE_OPER symspace=iri { isAbbrivated = false; } ) | '@')?
    -> {isAbbrivated}? ^(SHORTCONST LITERAL[getStrValue($STRING.text)])
    -> { s_numberTypeIRIs.contains($symspace.fullIRI)}? ^(SHORTCONST NUMBER[getStrValue($STRING.text)])
    -> { fastStringEquals($symspace.fullIRI, s_stringIRI) }? ^(SHORTCONST LITERAL[getStrValue($STRING.text)])
    -> ^(LITERAL[getStrValue($STRING.text)] IRI[$symspace.text])
    //|   STRING '@' ID /* langtag */ -> ^(SHORTCONST LITERAL[$STRING.text])
    ;

variable
    :   VAR_ID -> VAR_ID[$VAR_ID.text.substring(1)]
    ;

answer
    : 'yes'
    | 'no'
    | (term EQUAL term)+  
    ;

iri returns [String fullIRI]
	: IRI_REF -> IRI[$fullIRI = $IRI_REF.text]
	| curie { $fullIRI = $curie.fullIRI; }
	;
	
curie returns [String fullIRI]
	: NAMESPACE localName=PN_LOCAL
	-> { localName == null }? IRI[$fullIRI = getFullIRI($NAMESPACE.text, "")]
	-> IRI[$fullIRI = getFullIRI($NAMESPACE.text, $localName.text)]
	;

//--------------------- LEXER: -----------------------
// Comments and whitespace:
WHITESPACE  :  (' '|'\t'|'\r'|'\n')+ { $channel = HIDDEN; } ;
COMMENT : '%' (options {greedy=false;} : .* ) '\n' { $channel = HIDDEN; } ;
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

//   Operators:
IMPLICATION : ':-';
EQUAL : '=';
SUBCLASS : '##';
INSTANCE : '#';
SLOT_ARROW : '->' | '+>';
SYMSPACE_OPER : '^^';

//   Punctuation:
LPAR : '(' ;
RPAR : ')' ;
LESS : '<' ;
GREATER : '>' ;
DEPSIGN: '+' | '-';
LSQBR : '[' ;
RSQBR : ']' ;


//  Constants:
NUMBER: ('+' | '-')? DIGIT+ ('.' DIGIT*)?;
STRING: '"' (options {greedy=false;} : ~('"' | '\\' | EOL) | ECHAR)* '"';

//  Identifiers:
// IRI_REF : '<' IRI_START_CHAR (IRI_CHAR)+ '>' { setText(getFullIRI(iri)); };

IRI_REF
    : ('<' (options{greedy=false;}: IRI_REF_CHARACTERS)* '>') => '<' (options{greedy=false;}: IRI_REF_CHARACTERS*) '>'
    { String s = getText(); setText(s.substring(1, s.length() - 1)); }
    ;

fragment IRI_REF_CHARACTERS
    :  ~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '`' | '\\' | '\u0000'..'\u0020')
    ;


NAMESPACE : PN_PREFIX? ':';
fragment PN_PREFIX : PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?;
PN_LOCAL
	: (PN_CHARS_U | DIGIT | PLX) (PN_CHARS | PLX)*;

fragment PN_CHARS
    : PN_CHARS_U
    | { input.LA(2) != '>' }? => '-'
    | DIGIT
    | '\u00B7' 
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
    ;
fragment PN_CHARS_U : PN_CHARS_BASE | '_';
fragment PN_CHARS_BASE
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
    ;

fragment PLX : PERCENT | PN_LOCAL_ESC;
fragment PERCENT : '%' HEX HEX;
fragment HEX : DIGIT | 'A'..'F' | 'a'..'f';
fragment PN_LOCAL_ESC 
	:  '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' 
			| ';' | '=' | ':' | '/' | '?' | '#' | '@' | '%' );

fragment ALPHA : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT : '0'..'9' ;

VAR_ID : '?' PN_LOCAL?;

/*
VAR_ID : '?' ID_CHAR*;
ID : ID_START_CHAR ID_CHAR*;

//  Basics
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
    | { input.LA(2) != '>' }? => '-'
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
*/

fragment ECHAR : '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'');

fragment EOL : '\n' | '\r';

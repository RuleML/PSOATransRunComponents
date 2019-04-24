package org.ruleml.psoa.psoa2x.psoa2prolog;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.TreeNodeStream;

/**
 * This is the superclass for Prolog converters. It includes methods such as 
 * built-in conversions.
 * 
 * */
public abstract class AbstractPrologConverter extends PrologTermLangConverter {
	private static final Map<String, String> s_builtInMap = new HashMap<String, String>(16);
	
	// Table maps PSOA built-ins to XSB Prolog
	static
	{
		// Taken from W3C RIF-DTB library
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-equal", "\'=:=\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-not-equal", "\'=\\\\=\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-greater-than", "\'>\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-greater-than-or-equal", "\'>=\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-less-than", "\'<\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-less-than-or-equal", "\'=<\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#is-literal-double", "float");  // Shortcut or xs:double float numbers (single or double) are represented as doubles for XSB
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#is-literal-integer", "integer");
		
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-add", "\'+\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-subtract", "\'-\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-multiply", "\'*\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-divide", "\'/\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-integer-divide", "\'//'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-mod", "rem");
		
		// Taken from ISO Prolog library
		// Numeric Functions
		s_builtInMap.put("https://www.iso.org/standard/21413.html#add", "\'+\'");     // XSB's special character tokens
		s_builtInMap.put("https://www.iso.org/standard/21413.html#sub", "\'-\'");     // replaced with
		s_builtInMap.put("https://www.iso.org/standard/21413.html#mul", "\'*\'");     // short names
		s_builtInMap.put("https://www.iso.org/standard/21413.html#int-div", "\'//'"); // similar to
		s_builtInMap.put("https://www.iso.org/standard/21413.html#div", "\'/\'");     // ISO's other names.
		s_builtInMap.put("https://www.iso.org/standard/21413.html#rem", "rem");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#mod", "mod");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#abs", "abs");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#sign", "sign");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#float", "float");  // Coerce to shortcut float number (represented as double) from, e.g., integer
		s_builtInMap.put("https://www.iso.org/standard/21413.html#truncate", "truncate");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#round", "round");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#floor", "floor");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#ceiling", "ceiling");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#power", "\'**\'");  // Special chars for XSB
		s_builtInMap.put("https://www.iso.org/standard/21413.html#sin", "sin");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#cos", "cos");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#atan", "atan");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#sqrt", "sqrt");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#exp", "exp");
		s_builtInMap.put("https://www.iso.org/standard/21413.html#log", "log");		
			
		// Numeric Predicates
		s_builtInMap.put("https://www.iso.org/standard/21413.html#integer", "integer");  // Test if integer
		s_builtInMap.put("https://www.iso.org/standard/21413.html#float", "float");  // Test if shortcut or xs:double float number (represented as double) [Currently redundant because of above same-named coerce]
		s_builtInMap.put("https://www.iso.org/standard/21413.html#number", "number");  // Test if number
		s_builtInMap.put("https://www.iso.org/standard/21413.html#eq", "\'=:=\'");                // XSB's special character tokens ...
		s_builtInMap.put("https://www.iso.org/standard/21413.html#not_eq", "\'=\\\\=\'");           //
		s_builtInMap.put("https://www.iso.org/standard/21413.html#greater_than", "\'>\'");        //
		s_builtInMap.put("https://www.iso.org/standard/21413.html#greater_than_or_eq", "\'>=\'"); //
		s_builtInMap.put("https://www.iso.org/standard/21413.html#less_than", "\'<\'");           //
		s_builtInMap.put("https://www.iso.org/standard/21413.html#less_than_or_eq", "\'=<\'");    // ... renamed like for functions above
		
		// Generic inequality
		s_builtInMap.put("https://www.iso.org/standard/21413.html#generic_not_eq", "\'\\\\=\'");

		// Taken from XSB Prolog standard module
		s_builtInMap.put("http://xsb.sourceforge.net/manual1/manual1.pdf#datime", "datime");              // XSB's datime and local_datime predicates
		s_builtInMap.put("http://xsb.sourceforge.net/manual1/manual1.pdf#local_datime", "local_datime");  // just prefixed with iri of XSB's manual1
		
	}

	public AbstractPrologConverter(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}
	
	@Override
	protected void convertIRIConst(String iri) {
		String builtIn = s_builtInMap.get(iri);
		if (builtIn == null)
		{
			super.convertIRIConst(iri);
		}
		else
		{
			append(builtIn);
		}
	}

	// Translate double-quoted strings to single-quoted Prolog symbols 
	@Override
	protected void convertStringConst(String str) {
		append("\'\"", str.replace("\'","\'\'"), "\"\'");
	}
	
	@Override
	protected String inverseTranslateUnquotedConst(String symbol)
	{
		// For the inverse translation of specific Prolog symbols used in 
		// answers returned by engines, e.g. date-related function names
		if(symbol.equals("datime"))
		{
			return "<http://xsb.sourceforge.net/manual1/manual1.pdf#datime>";
		}
		
		return symbol;
	}

	@Override
	protected Object inverseTranslateList(String symbol) {
		return symbol;
	}
}

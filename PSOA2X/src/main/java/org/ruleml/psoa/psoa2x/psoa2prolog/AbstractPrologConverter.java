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
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-not-equal", "\'=\\=\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-greater-than", "\'>\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-greater-than-or-equal", "\'>=\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-less-than", "\'<\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#numeric-less-than-or-equal", "\'=<\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-predicate#is-literal-integer", "\'integer\'");
		
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-add", "\'+\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-subtract", "\'-\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-multiply", "\'*\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-divide", "\'/\'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-integer-divide", "\'//'");
		s_builtInMap.put("http://www.w3.org/2007/rif-builtin-function#numeric-mod", "rem");
		
		// Taken from ISO Prolog library
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#rem", "rem");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#mod", "mod");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#abs", "abs");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#sign", "sign");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#float", "float");//*
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#truncate", "truncate");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#round", "round");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#ceiling", "ceiling");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#power", "\'**\'");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#sin", "sin");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#cos", "cos");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#atan", "atan");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#sqrt", "sqrt");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#exp", "exp");
		s_builtInMap.put("http://www.deransart.fr/prolog/bips.html#log", "log");
		
		
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
	
	@Override
	protected String inverseTranslateUnquotedConst(String symbol)
	{
		// For the inverse translation of specific Prolog symbols used in 
		// answers returned by engines, e.g. date-related function names
		
		return symbol;
	}
}

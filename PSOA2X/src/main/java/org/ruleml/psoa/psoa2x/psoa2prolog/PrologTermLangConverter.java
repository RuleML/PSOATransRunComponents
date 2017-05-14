package org.ruleml.psoa.psoa2x.psoa2prolog;

import java.util.regex.*;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.psoa2x.common.ANTLRBasedTranslator;
import org.ruleml.psoa.psoa2x.common.TranslatorException;

/**
 * This is the superclass of converters to languages using Prolog's syntax of terms, 
 * e.g. Prolog and TPTP. The class includes methods for term conversions.
 * 
 * */
public abstract class PrologTermLangConverter extends ANTLRBasedTranslator.Converter {
	public PrologTermLangConverter(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}
	
	protected void convertLocalConst(String name) {
		append("\'_", name, "\'");
	}
	
	protected void convertIRIConst(String iri) {
        // TODO: Handle special datatypes, e.g. string, int, etc.
		append("\'<", iri, ">\'");
	}

	protected void convertStringConst(String str) {
		append("\'\"", str.replace("\'","\'\'"), "\"\'");
	}
	
	protected void convertGeneralConst(String literal, String datatype) {
		append("\'\"", literal, "\"^^", datatype, "\'");
	}
	
	protected void convertVar(String var) {
		append("Q", var);
	}
	
	/**
	 * Compiled regular expression for Prolog terms.
	 * 
	 * */
	private static Pattern s_termPattern = 
			Pattern.compile(
					 "(?<quotedConst>\\'([^\\']|(\\'\\'))+\\'(?!\\'))" +
					"|(?<number>[+-]?[\\d*\\.]?\\d+)" +
					"|(?<unquotedConst>[a-z]\\w*)" +
					"|(?<var>[A-Z]\\w+)" +  
					"|(?<string>\\\"([^\\\"]|(\\\\\\\"))*\\\")"
			);
	
	/**
	 * Inverse translate a Prolog term into PSOA.
	 * 
	 * */
	@Override
	public String inverseTranslateTerm(String term)
	{
		StringBuilder sb = new StringBuilder(term.length());
		Matcher matcher = s_termPattern.matcher(term);
		int start = 0;
		while (matcher.find(start))
		{
			// Parse a Prolog term
			String symbol = matcher.group();
			
			if (matcher.group("quotedConst") != null)
			{
				sb.append(inverseTranslateQuotedConst(symbol));
			}
			else if (matcher.group("var") != null)
			{
				sb.append(inverseTranslateVariable(symbol));
			}
			else if (matcher.group("unquotedConst") != null)
			{
				sb.append(inverseTranslateUnquotedConst(symbol));
			}
			else if (matcher.group("number") != null)
			{
				sb.append(inverseTranslateNumber(symbol));
			}
			else if (matcher.group("string") != null)
			{
				sb.append(inverseTranslateString(symbol));
			}
			else
			{
				throw new TranslatorException("Unable to parse answer term: " + term);
			}
			
			start = matcher.end();
			try {
				// Parse connectors between terms
				char c = term.charAt(start);
				if (c == ',')
					sb.append(' ');
				else if (c == '(')
					sb.append(c);
				else if (c == ')')
				{
					do
					{
						sb.append(c);
						c = term.charAt(++start);
					} while (c == ')');
				}
				else
				{
					throw new TranslatorException("Unable to parse answer term: " + term);
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				break;
			}
		}
		
		if (start != term.length())
		{
			throw new TranslatorException("Unable to parse answer term: " + term);
		}
		
		return sb.toString();
	}

	protected String inverseTranslateQuotedConst(String symbol)
	{
		return symbol.substring(1, symbol.length() - 1).replace("\'\'", "'");
	}
	
	protected String inverseTranslateUnquotedConst(String symbol)
	{
		return symbol;
	}
	
	protected String inverseTranslateVariable(String symbol)
	{
		if (symbol.charAt(0) == 'Q')
		{
			return "?" + symbol.substring(1);
		}
		else
		{
			return symbol;
		}
	}
	
	protected String inverseTranslateNumber(String symbol) {
		return symbol;
	}

	protected String inverseTranslateString(String symbol) {
		return symbol;
	}
}

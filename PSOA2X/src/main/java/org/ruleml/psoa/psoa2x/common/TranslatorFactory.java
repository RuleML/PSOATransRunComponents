package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;
import org.ruleml.psoa.psoa2x.psoa2tptp.TPTPTranslator;

public class TranslatorFactory {
	
	public static Translator createTranslator(String targetLanguage)
	{
		if (targetLanguage.equalsIgnoreCase("prolog"))
			return new PrologTranslator();
		else if (targetLanguage.equalsIgnoreCase("tptp"))
			return new TPTPTranslator();
		
		throw new TranslatorException("Unknown target language: " + targetLanguage);
	}
}

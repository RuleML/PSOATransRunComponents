package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;
import org.ruleml.psoa.psoa2x.psoa2tptp.TPTPASOTranslator;

public class TranslatorFactory {
	
	public Translator createTranslator(String targetLanguage)
	{
		if (targetLanguage.equalsIgnoreCase("prolog"))
			return new PrologTranslator();
		else if (targetLanguage.equalsIgnoreCase("tptp"))
			return new TPTPASOTranslator();
		
		throw new TranslatorException("Unknown target language: " + targetLanguage);
	}
}

package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.psoa2x.psoa2prolog.PSOA2PrologConfig;
import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;
import org.ruleml.psoa.psoa2x.psoa2tptp.PSOA2TPTPConfig;
import org.ruleml.psoa.psoa2x.psoa2tptp.ASOTPTPTranslator;

public class TranslatorFactory {
	
	public Translator createTranslator(String targetLanguage)
	{
		if (targetLanguage.equalsIgnoreCase("prolog"))
			return new PrologTranslator(new PSOA2PrologConfig());
		else if (targetLanguage.equalsIgnoreCase("tptp"))
			return new ASOTPTPTranslator(new PSOA2TPTPConfig());
		
		throw new TranslatorException("Unknown target language: " + targetLanguage);
	}
}

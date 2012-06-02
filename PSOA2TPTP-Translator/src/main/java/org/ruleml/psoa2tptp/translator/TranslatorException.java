package org.ruleml.psoa2tptp.translator;

import org.antlr.runtime.RecognitionException;

public class TranslatorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8112315044970881572L;

	public TranslatorException(String msg)
	{
		super(msg);
	}
	
	public TranslatorException(RecognitionException rexp)
	{
		super("Paring Error");
	}
}

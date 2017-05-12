package org.ruleml.psoa.psoa2x.common;

import org.antlr.runtime.RecognitionException;
import org.ruleml.psoa.transformer.PSOARuntimeException;

/**
 * Translator exceptions
 * 
 */
public class TranslatorException extends PSOARuntimeException {
	private static final long serialVersionUID = 8112315044970881572L;

	public TranslatorException(String msg)
	{
		super(msg);
	}
	
	public TranslatorException(Throwable e)
	{
		super(e);
	}
	
	public TranslatorException(RecognitionException rexp)
	{
		super("Parsing Error");
	}
}

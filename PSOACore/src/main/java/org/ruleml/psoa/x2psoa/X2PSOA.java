package org.ruleml.psoa.x2psoa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Interface for translators from foreign langauges to PSOA
 * 
 * */
public abstract class X2PSOA
{
	public abstract void translate(InputStream input, OutputStream output) throws IOException;
	
	/**
	 * Get an InputStream including the translated PSOA/PS output of the original input.
	 * 
	 * @param input   the input stream to be translated into PSOA/PS
	 * 
	 * */
	public InputStream getTranslatedStream(InputStream input) throws IOException
	{
		InputStream stream;
		
		try (PipedOutputStream psoaOut = new PipedOutputStream()) {
			/*
			   The following assumes an maximum translated output size
			   of 10MB. If the output is larger than 10MB, the reading of 
			   the returned stream should be in a different thread from  
			   the thread where this method is called. Otherwise, the 
			   calling thread will be blocked.
			 */
			
			stream = new PipedInputStream(psoaOut, 10485760);
			translate(input, psoaOut);
		}
		
		return stream;
	}
}

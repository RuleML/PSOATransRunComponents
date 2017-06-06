package org.ruleml.psoa.x2psoa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Superclass for translators from other languages or syntaxes to PSOA presentation syntax
 * 
 * */
public abstract class X2PSOA
{
	/**
	 * Translate input into PSOA presentation syntax and write the output
	 * into an output stream.
	 * 
	 * @param input   the input stream to be translated into PSOA presentation syntax
	 * @param output   the output stream for translator output
	 * 
	 * */
	public abstract void translate(InputStream input, OutputStream output) throws IOException;
	
	/**
	 * Get an input stream for reading the PSOA presentation syntax translation 
	 * of the original input stream.
	 * 
	 * @param input   the input stream to be translated into PSOA presentation syntax
	 * 
	 * @return   a stream for reading the translator output
	 * 
	 * */
	public InputStream getTranslatedStream(InputStream input) throws IOException
	{
		InputStream stream;
		
		try (PipedOutputStream psoaOut = new PipedOutputStream()) {
			/*
			   The following assumes a maximum translator output size
			   of 10MB. An output size larger than that will block the 
			   current thread. In the future, the translate call can be 
			   put into a different thread to avoid this problem.
			 */
			
			stream = new PipedInputStream(psoaOut, 10485760);
			translate(input, psoaOut);
		}
		
		return stream;
	}
}

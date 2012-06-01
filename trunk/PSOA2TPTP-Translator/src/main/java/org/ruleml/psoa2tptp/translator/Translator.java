package org.ruleml.psoa2tptp.translator;

import java.io.*;

public abstract class Translator {
	abstract public void translateKB(String kb, OutputStream out) throws TranslatorException;
	abstract public void translateQuery(String query, OutputStream out) throws TranslatorException;
	
	public String translateKB(String kb) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream();
		translateKB(kb, output);
		return output.toString();
	}
	
	public String translateQuery(String query) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream();
		translateQuery(query, output);
		return output.toString();
	}
	
	public void translate(String kb, String query, OutputStream out) throws TranslatorException	{
		if (kb != null)
			translateKB(kb, out);
		if (query != null)
			translateQuery(query, out);
	}
	
	public String translate(String kb, String query) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream();
		translate(kb, query, output);
		return output.toString();
	}
	
	protected static PrintStream getPrintStream(OutputStream out) {
		if (out instanceof PrintStream)
			return (PrintStream)out;
		
		return new PrintStream(out);
	}
}
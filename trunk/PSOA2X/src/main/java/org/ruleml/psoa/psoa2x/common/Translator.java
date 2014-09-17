package org.ruleml.psoa.psoa2x.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Translator {
	Map<String, String> _queryVarMap = new HashMap<String, String>();
	
	abstract public void translateKB(String kb, OutputStream out) throws TranslatorException;
	abstract public void translateQuery(String query, OutputStream out) throws TranslatorException;
	abstract public void translateKB(InputStream kb, OutputStream out) throws TranslatorException;
	abstract public void translateQuery(InputStream query, OutputStream out) throws TranslatorException;
	
	public String translateKB(InputStream kb) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(512);
		translateKB(kb, output);
		return output.toString();
	}
	
	public String translateQuery(InputStream query) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream();
		translateQuery(query, output);
		return output.toString();
	}
	
	public String translateKB(String kb) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(512);
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
	
	public Map<String, String> getQueryVarMap()
	{
		return _queryVarMap;
	}
}
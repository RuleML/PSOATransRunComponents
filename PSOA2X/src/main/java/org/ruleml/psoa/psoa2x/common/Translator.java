package org.ruleml.psoa.psoa2x.common;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Translator {
	protected Map<String, String> m_queryVarMap = new HashMap<String, String>();
	protected static boolean debugMode = false;

	abstract public void translateKB(String kb, OutputStream out) throws TranslatorException;
	abstract public void translateQuery(String query, OutputStream out) throws TranslatorException;
	abstract public void translateKB(InputStream kb, OutputStream out) throws TranslatorException;
	abstract public void translateQuery(InputStream query, OutputStream out) throws TranslatorException;
	abstract public String inverseTranslateTerm(String term);
	
	/**
	 * Translate input KB stream
	 * 
	 * @param kb   input KB stream
	 * 
	 * */
	public String translateKB(InputStream kb) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(1024);
		translateKB(kb, output);
		return output.toString();
	}

	
	/**
	 * Translate input KB string
	 * 
	 * @param kb   input KB string
	 * 
	 * */
	public String translateKB(String kb) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(1024);
		translateKB(kb, output);
		return output.toString();
	}

	
	/**
	 * Translate input query stream
	 * 
	 * @param query   input query stream
	 * 
	 * */
	public String translateQuery(InputStream query) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(512);
		translateQuery(query, output);
		return output.toString();
	}

	
	/**
	 * Translate input query string
	 * 
	 * @param query   input query string
	 * 
	 * */
	public String translateQuery(String query) throws TranslatorException {
		OutputStream output = new ByteArrayOutputStream(512);
		m_queryVarMap.clear();
		translateQuery(query, output);
		return output.toString();
	}

	public Map<String, String> getQueryVarMap()
	{
		return m_queryVarMap;
	}
	
	/**
	 * Get the translated free variables in the most recent query
	 * 
	 * @return   translated free variables in the most recent query
	 * 
	 * */
	public List<String> getQueryVars() {
		return new ArrayList<String>(m_queryVarMap.keySet());
	}
}
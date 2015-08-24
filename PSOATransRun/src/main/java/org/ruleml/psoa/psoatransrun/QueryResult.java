package org.ruleml.psoa.psoatransrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ruleml.psoa.psoa2x.common.Translator;

public class QueryResult
{
	private boolean m_result;
	private Bindings m_bindings;
	
	public QueryResult(boolean result)
	{
		m_result = result;
		if (m_result)
			m_bindings = new Bindings();
	}
	
	public QueryResult(boolean result, Bindings bindings)
	{
		if (!result)
		{
			if (bindings != null)
				throw new IllegalArgumentException("Bindings must be null if the query result is false");
		}
		else if (bindings == null)
		{
			bindings = new Bindings();
		}
		
		m_result = result;
		m_bindings = bindings;
	}
	
	public QueryResult(Bindings bindings) {
		m_result = !bindings.isEmpty(); 
		if (m_result)
		{
			m_bindings = bindings;
		}
	}

	public void addBinding(Binding b)
	{
		m_bindings.add(b);
	}
	
	public boolean binaryResult()
	{
		return m_result;
	}
	
	public void inverseTranslate(Translator translator)
	{
		if (m_result)
		{
			m_bindings.inverseTranslate(translator);
		}
	}
	
	public Bindings getBindings()
	{
		return m_bindings;
	}
	
	public int numBindings()
	{
		return m_bindings == null? 0 : m_bindings.size();
	}
	
	public int numCommonBindings(QueryResult r)
	{
		if (!m_result || !r.m_result)
			return 0;
		else
			return m_bindings.numCommonBindings(r.m_bindings);
	}
	
	public static QueryResult parse(File f) throws FileNotFoundException
	{
		Scanner sc = new Scanner(f);
		try
		{
			String s = sc.nextLine();
			if (s.equalsIgnoreCase("yes"))
				return new QueryResult(true);
			else if (s.equalsIgnoreCase("no"))
				return new QueryResult(false);
			else
			{
				Bindings bindings = Bindings.parseBindings(sc);
				bindings.add(Binding.parse(s));
				return new QueryResult(bindings);
			}
		}
		finally
		{
			sc.close();
		}
	}
	
	@Override
	public String toString() {
		if (m_bindings == null)
			return "no";
		
		if (m_bindings.isEmpty())
			return m_result? "yes" : "no";
					
		return m_bindings.toString();
	}
}

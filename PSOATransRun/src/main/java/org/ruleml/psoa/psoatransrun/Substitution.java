package org.ruleml.psoa.psoatransrun;

import java.util.*;
import java.util.Map.Entry;

import org.ruleml.psoa.psoa2x.common.Translator;

/**
 * The class is used for operating on substitutions. Each instance of the class is 
 * a substitution that maps variables to terms, which is used to represent a query
 * answer in PSOATransRun.
 * 
 * */
public class Substitution {
	private Map<String, String> m_binding = new LinkedHashMap<String, String>();

	public void addPair(String var, String value) {
		m_binding.put(var, value);
	}
	
	public static Substitution parse(String result) {
		// TODO: Handle special cases, e.g. ',' or '=' in constants, syntactic errors
		Substitution b = new Substitution();
		String[] pairs = result.split("\\ \\?");
		boolean first = true;
		
		for (String pair : pairs)
		{
			int index = pair.indexOf('=');
			if (index < 0)
				continue;
			
			String s = pair.substring(0, index);
			if (first)
				first = false;
			else
				s = "?" + s;
			b.addPair(s, pair.substring(index + 1, pair.length()));
		}
		
		return b;
	}
	
	@Override
	public boolean equals(Object obj) {
		try
		{
			return ((Substitution)obj).m_binding.equals(m_binding);
		} catch (ClassCastException e)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return m_binding.hashCode();
	}
	
	@Override
	public String toString() {
		if (m_binding.isEmpty())
			return "Yes";
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : m_binding.entrySet())
		{
			sb.append(entry.getKey()).append("=");
			sb.append(entry.getValue()).append(",");
		}
		
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * Check whether this is an empty substitution, which denotes a "yes" answer
	 * 
	 * @return   whether the substitution is an empty set
	 * 
	 * */
	public boolean isEmpty()
	{
		return m_binding.isEmpty();
	}
	
	public void inverseTranslate(Translator t) {
		Map<String, String> transBinding = new LinkedHashMap<String, String>(m_binding.size() * 2);
		for (Entry<String, String> entry : m_binding.entrySet())
		{
			String var = t.inverseTranslateTerm(entry.getKey()),
				   value = t.inverseTranslateTerm(entry.getValue());
			transBinding.put(var, value);
		}
		m_binding.clear();
		m_binding = transBinding;
	}
}
package org.ruleml.psoa.psoatransrun.restful.models;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ruleml.psoa.psoatransrun.utils.RESTfulUtils;

@XmlRootElement
public class ExecutionRequest {
	
	/*
	 * expects: {kb: ..., query: ..., queryVars, ..., engine:...}
	 */
	private String m_kb, m_query, m_engine;
	private List<String> m_queryVars;
	
	@XmlElement(name = "kb")
	public String getKB() {
		return m_kb;
	}
	
	public void setKB(String kb) {
		m_kb = RESTfulUtils.decode(kb);
	}
	
	@XmlElement(name = "query")
	public String getQuery() {
		return m_query;
	}
	
	public void setQuery(String query)
	{
		m_query = RESTfulUtils.decode(query);
	}

	@XmlElement(name = "queryVars")
	public List<String> getQueryVars() {
		return m_queryVars;
	}
	
	public void setQueryVars(List<String> queryVars) {
		m_queryVars = queryVars;
	}
	
	@XmlElement(name = "engine")
	public String getEngine()
	{
		return m_engine;
	}
	
	public void setEngine(String engine)
	{
		m_engine = engine;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(256);
		
		if (m_kb != null)
			builder.append("kb:\n").append(m_kb).append("\n");
		if (m_query != null)
			builder.append("query:\n").append(m_query).append("\n");
		if (m_queryVars != null)
		{
			builder.append("queryVars:");
			for (String var : m_queryVars)
			{
				builder.append(var).append(',');
			}
			if (m_queryVars.isEmpty())
				builder.append("\n");
			else
				builder.setCharAt(builder.length() - 1, '\n');
		}
		if (m_engine != null)
			builder.append("engine:").append(m_engine).append("\n");
		
		return builder.toString();
	}
}

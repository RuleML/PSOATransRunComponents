package org.ruleml.psoa.psoatransrun.restful.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TranslationResult {
	/*
	 * Expects {kb: ..., query: ..., queryVars: []}
	 */
	
	private String m_KB, m_query;
	private List<String> m_queryVars;
	
	@XmlElement(name = "kb")
	public String getKB() {
		return m_KB;
	}
	
	public void setKB(String kb) {
		m_KB = kb;
	}
	
	@XmlElement(name = "query")
	public String getQuery() {
		return m_query;
	}
	
	public void setQuery(String query) {
		this.m_query = query;
	}
	
	@XmlElement(name = "queryVars")
	public List<String> getQueryVars() {
		return m_queryVars;
	}
	
	public void setQueryVars(List<String> queryVars) {
		m_queryVars = queryVars;
	}
	
//	public List<VarPair> getVarMap() {
//		return m_varMap;
//	}
//	
//	public void setVarMap(List<VarPair> varMap) {
//		this.m_varMap = varMap;
//	}
//	
//	public void setVarMap(Map<String, String> varMap) {
//		m_varMap = new ArrayList<VarPair>();
//		for (Map.Entry<String, String> entry : varMap.entrySet())
//		{
//			m_varMap.add(new VarPair(entry.getKey(), entry.getValue()));
//		}
//	}
//	
//	public static class VarPair
//	{
//		String psoaVar;
//		String transVar;
//		
//		public VarPair(String psoaVar, String transVar) {
//			this.psoaVar = psoaVar;
//			this.transVar = transVar;
//		}
//
//		public String getPsoaVar() {
//			return psoaVar;
//		}
//		
//		public void setPsoaVar(String psoaVar) {
//			this.psoaVar = psoaVar;
//		}
//		
//		public String getTransVar() {
//			return transVar;
//		}
//		
//		public void setTransVar(String transVar) {
//			this.transVar = transVar;
//		}
//	}
}

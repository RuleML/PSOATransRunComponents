package org.ruleml.psoa.psoatransrun.restful.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ruleml.psoa.psoatransrun.utils.RESTfulUtils;

@XmlRootElement
public class TranslationRequest {
	/*
	 * Expects {kb: ..., query: ..., target: ..., transType: ...}
	 */
//	public enum TranslatorType { Direct, TPTPASO };
	
	private String m_kb, m_query, m_targetLanguage;
//	private String m_transType;
	
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
	public void setQuery(String psoaQuery) {
		m_query = RESTfulUtils.decode(psoaQuery);
	}
//	public String getTransType() {
//		return m_transType;
//	}
//	public void setTransType(String transType) {
//		m_transType = transType;
//	}
	
	@XmlElement(name = "target")
	public String getTarget()
	{
		return m_targetLanguage;
	}
	public void setTarget(String target)
	{
		m_targetLanguage = target;
	}
	
	@Override
	public String toString() {
		return "Translation Request: kb: " + m_kb + ", query: " + m_query + ", target: " + m_targetLanguage;
	}
//	public TranslatorType transType() {
//	return Enum.valueOf(TranslatorType.class, m_transType);
//}
}

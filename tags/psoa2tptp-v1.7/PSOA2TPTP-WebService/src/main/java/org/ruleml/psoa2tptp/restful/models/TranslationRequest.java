package org.ruleml.psoa2tptp.restful.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TranslationRequest {
	/*
	 * Expects {document: ..., query: ..., transType: ...}
	 */
	public enum TranslatorType { Direct, TPTPASO };
	
	private String document;
	private String query;
	private String transType;

	public String getDocument() {
		return document;
	}
	public void setDocument(String psoaDocument) {
		this.document = psoaDocument;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String psoaQuery) {
		this.query = psoaQuery;
	}
	public String getTransType() {
		return transType;
	}	
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public TranslatorType transType() {
		return Enum.valueOf(TranslatorType.class, transType);
	}
}

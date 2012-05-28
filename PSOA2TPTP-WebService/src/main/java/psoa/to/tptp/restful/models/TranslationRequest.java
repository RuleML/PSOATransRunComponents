package psoa.to.tptp.restful.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TranslationRequest {

	/*
	 * Expects {document: ..., query: ...}
	 */
	private String document;
	private String query;

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
	
}

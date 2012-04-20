package psoa.to.tptp.restful.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TptpDocument {

	private List<String> sentences;
	
	public List<String> getSentences() {
		return sentences;
	}
	
	public void setSentences(List<String> sentences) {
		this.sentences = sentences;
	}
	
}

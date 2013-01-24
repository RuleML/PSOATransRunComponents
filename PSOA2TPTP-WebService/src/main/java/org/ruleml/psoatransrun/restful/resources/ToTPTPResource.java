package org.ruleml.psoa2tptp.restful.resources;

import java.net.URLDecoder;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ruleml.psoa2tptp.restful.models.*;
import org.ruleml.psoa2tptp.translator.*;

@Path("/translate")
public class ToTPTPResource {

	@Context UriInfo info;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Encoded
	public String translateSentences(TranslationRequest req) {
		String kb = decode(req.getDocument());
		String query = decode(req.getQuery());
		try {
			Translator translator = null;
			switch (req.transType()) {
				case Direct:
					translator = new DirectTranslator();
					break;
				case TPTPASO:
					translator = new TPTPASOTranslator();
					break;
			}
			
			if (kb.isEmpty()) kb = null;
			if (query.isEmpty()) query = null;
			return translator.translate(kb, query);
//			List<String> l = list();
//			if (kb != null && !kb.isEmpty())
//				l = deserialize(translator.translateKB(kb));
//			
//			if (query != null && !query.isEmpty())
//				l.add(translator.translateQuery(query));
		} catch (TranslatorException e) {
			e.printStackTrace();
			return null;
		}
//		for (String str : l) {
//			System.out.println(str);
//		}
		
//		TptpDocument doc = new TptpDocument();
//		doc.setSentences(l);
//		return doc;
	}
	
	
	@SuppressWarnings("deprecation")
	private static String decode(String s) {
		return URLDecoder.decode(s.replace("&gt;", ">"));
	}
}

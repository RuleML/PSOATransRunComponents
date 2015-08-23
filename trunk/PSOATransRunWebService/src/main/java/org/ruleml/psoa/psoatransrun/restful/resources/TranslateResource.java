package org.ruleml.psoa.psoatransrun.restful.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ruleml.psoa.psoatransrun.restful.models.*;
import org.ruleml.psoa.psoa2x.common.*;
import org.ruleml.psoa.psoa2x.psoa2tptp.*;

import static org.ruleml.psoa.psoatransrun.utils.RESTfulUtils.*;

@Path("/translate")
public class TranslateResource {

	@Context UriInfo info;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Encoded
	public TranslationResult translate(TranslationRequest req) {
		String kb = req.getKB(), query = req.getQuery();
		
		if (kb == null && query == null)
			return null;
		
		try {
			Translator translator = (new TranslatorFactory()).createTranslator(req.getTarget());
			TranslationResult result = new TranslationResult();
			
			if (kb != null)
				result.setKB(translator.translateKB(kb));
			
			if (query != null)
			{
				result.setQuery(translator.translateQuery(query));
				result.setQueryVars(translator.getQueryVars());
			}

			return result;
			
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
	
	
//	@SuppressWarnings("deprecation")
//	private static String decode(String s) {
//		return URLDecoder.decode(s.replace("&gt;", ">"));
//	}
}

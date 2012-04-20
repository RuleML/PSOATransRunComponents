package psoa.to.tptp.restful.resources;

import static psoa.to.tptp.restful.resources.Collections.list;
import static psoa.to.tptp.restful.resources.Util.convertDocument;
import static psoa.to.tptp.restful.resources.Util.convertQuery;
import static psoa.to.tptp.restful.resources.Util.deserialize;
import static psoa.to.tptp.restful.resources.Util.out;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.antlr.runtime.RecognitionException;

import psoa.to.tptp.restful.models.TptpDocument;
import psoa.to.tptp.restful.models.TranslationRequest;

@Path("/translate")
public class ToTPTPResource {

	@Context UriInfo info;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Encoded
	public TptpDocument translateSentences(TranslationRequest req) {
		List<String> l = null;
		OutputStream out = out();
		OutputStream out2 = out();
		String psoa = decode(req.getDocument());
		String query = decode(req.getQuery());
		try {
			convertDocument(psoa, out);
			convertQuery(query, out2);
		} catch (RecognitionException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		l = list(deserialize(out.toString()));
		l.add(out2.toString());
		TptpDocument doc = new TptpDocument();
		doc.setSentences(l);
		return doc;
	}
	
	
	@SuppressWarnings("deprecation")
	private static String decode(String s) {
		return URLDecoder.decode(s.replace("&gt;", ">"));
	}
	
}



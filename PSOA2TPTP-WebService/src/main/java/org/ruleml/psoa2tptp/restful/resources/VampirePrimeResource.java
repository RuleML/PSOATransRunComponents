package org.ruleml.psoa2tptp.restful.resources;

import static org.ruleml.psoa2tptp.restful.resources.ShellUtil.*;
import static org.ruleml.psoa2tptp.restful.resources.Util.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;

import org.ruleml.psoa2tptp.restful.models.TptpDocument;

@Path("/execute")
public class VampirePrimeResource {
	@Context UriInfo info;
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Encoded
	public String getVampirePrimeResults(String doc) {
		CommandLine cl = cl(VKERNEL);
		File tmp = tmpFile();
		PrintWriter w = writer(tmp);		
//		w.print(serialize(doc.getSentences()));
		w.print(doc);
		w.close();
		cl.addArgument(tmp.getAbsolutePath());
		OutputStream out = out();
		try {
			execute(cl, out);
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tmp.delete();
		return out.toString();
	}	
}

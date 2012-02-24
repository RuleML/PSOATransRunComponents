package psoa.to.tptp.restful.resources;

import static psoa.to.tptp.restful.resources.ShellUtil.VKERNELWRAPPER;
import static psoa.to.tptp.restful.resources.ShellUtil.cl;
import static psoa.to.tptp.restful.resources.ShellUtil.execute;
import static psoa.to.tptp.restful.resources.Util.out;
import static psoa.to.tptp.restful.resources.Util.serialize;

import java.io.IOException;
import java.io.OutputStream;

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

import psoa.to.tptp.restful.models.TptpDocument;

@Path("/execute")
public class VampirePrimeResource {
	@Context UriInfo info;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Encoded
	public String getVampirePrimeResults(TptpDocument doc) {
		CommandLine cl = cl(VKERNELWRAPPER);
		cl.addArgument(serialize(doc.getSentences()));
		OutputStream out = out();
		try {
			execute(cl, out);
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}	
}

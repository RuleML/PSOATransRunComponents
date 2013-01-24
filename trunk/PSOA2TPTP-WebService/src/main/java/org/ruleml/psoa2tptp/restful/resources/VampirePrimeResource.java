package org.ruleml.psoa2tptp.restful.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ruleml.psoatransrun.engine.ExecutionEngine;

@Path("/execute")
public class VampirePrimeResource
{	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Encoded
	public String getVampirePrimeResults(String tptpDoc)
	{
		ExecutionEngine engine = ExecutionEngine.VampirePrime;
		return engine.run(tptpDoc);
	}
}
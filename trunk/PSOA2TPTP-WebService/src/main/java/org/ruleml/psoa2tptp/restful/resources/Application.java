package org.ruleml.psoa2tptp.restful.resources;

import static org.ruleml.psoa2tptp.restful.resources.Collections.set;

import java.io.IOException;
import java.util.Set;

public class Application extends javax.ws.rs.core.Application {
	private Set<Object> singletons = set();
	private Set<Class<?>> empty = set();

	public Application() {
		// ADD YOUR RESTFUL RESOURCES HERE
		this.singletons.add(new ToTPTPResource());
		this.singletons.add(new VampirePrimeResource());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return this.empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return this.singletons;
	}
}

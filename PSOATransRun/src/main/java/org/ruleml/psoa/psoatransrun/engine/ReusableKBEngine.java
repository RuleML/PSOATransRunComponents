package org.ruleml.psoa.psoatransrun.engine;

import java.util.*;

import org.ruleml.psoa.psoatransrun.*;

/**
 * Engines which can reuse a KB for multiple queries.
 * 
 * */
public abstract class ReusableKBEngine extends ExecutionEngine {
	public abstract void loadKB(String kb);
	public abstract QueryResult executeQuery(String query, List<String> queryVars);
	
	@Override
	public QueryResult executeQuery(String kb, String query, List<String> queryVars)
	{
		loadKB(kb);
		return executeQuery(query, queryVars);
	}
}

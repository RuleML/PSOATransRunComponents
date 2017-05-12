package org.ruleml.psoa.psoatransrun.engine;

import java.util.*;

import org.ruleml.psoa.psoatransrun.*;

/**
 * Engines that can reuse one KB for multiple queries.
 * 
 */
public abstract class ReusableKBEngine extends ExecutionEngine {
	public abstract void loadKB(String kb);
	public abstract QueryResult executeQuery(String query,
			List<String> queryVars, boolean getAllAnswers);

	@Override
	public QueryResult executeQuery(String kb, String query,
			List<String> queryVars, boolean getAllAnswers) {
		loadKB(kb);
		return executeQuery(query, queryVars, getAllAnswers);
	}

	public QueryResult executeQuery(String query, List<String> queryVars) {
		return executeQuery(query, queryVars, true);
	}
}

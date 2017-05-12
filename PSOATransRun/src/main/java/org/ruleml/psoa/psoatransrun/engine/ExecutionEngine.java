package org.ruleml.psoa.psoatransrun.engine;

import java.util.List;

import org.ruleml.psoa.psoatransrun.QueryResult;

/**
 * Root class of all engines
 * 
 * */
public abstract class ExecutionEngine {
	public abstract QueryResult executeQuery(String kb, String query,
			List<String> queryVarMap, boolean getAllAnswers);

	public QueryResult executeQuery(String kb, String query,
			List<String> queryVarMap) {
		return executeQuery(kb, query, queryVarMap, true);
	}

	// Cleanup process, default to be empty
	public void shutdown() {

	}
}

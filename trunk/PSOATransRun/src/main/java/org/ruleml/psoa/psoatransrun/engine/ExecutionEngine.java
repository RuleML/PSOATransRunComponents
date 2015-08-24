package org.ruleml.psoa.psoatransrun.engine;

import java.util.List;

import org.ruleml.psoa.psoatransrun.QueryResult;

public abstract class ExecutionEngine
{
	public abstract QueryResult executeQuery(String kb, String query, List<String> queryVarMap);
	
	// Cleanup process, default to be empty
	public void dispose()
	{
		
	}
}

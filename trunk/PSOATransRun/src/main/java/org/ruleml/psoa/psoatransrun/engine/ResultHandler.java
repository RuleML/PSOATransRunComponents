package org.ruleml.psoa.psoatransrun.engine;

public abstract class ResultHandler
{
	public static final ResultHandler IdentityHandler = new ResultHandler()
	{
		@Override
		public String parse(String engineOutput)
		{
			return engineOutput;
		}
	};
	
	public abstract String parse(String engineOutput);
}
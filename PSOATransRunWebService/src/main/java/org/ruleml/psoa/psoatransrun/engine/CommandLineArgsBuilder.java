package org.ruleml.psoa.psoatransrun.engine;

public interface CommandLineArgsBuilder
{
	public String[] buildCommandLine(String[] resourcePaths, String inputFilePath);
}

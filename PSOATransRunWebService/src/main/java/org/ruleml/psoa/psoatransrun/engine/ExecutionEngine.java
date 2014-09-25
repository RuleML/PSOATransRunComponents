package org.ruleml.psoa.psoatransrun.engine;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;
import static org.ruleml.psoa.psoatransrun.utils.ShellUtil.execute;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.exec.CommandLine;

public class ExecutionEngine
{
	protected final String[] m_resourcePaths;
	protected final String m_binPath;
	protected final CommandLineArgsBuilder m_commBuilder;
	protected final long m_exeTimeout;
	protected final ResultHandler m_resultHandler;
	
	public ExecutionEngine(CommandLineArgsBuilder cBuilder, ResultHandler resultHandler, long exeTimeout, String binPath, String... resources)
	{
		ClassLoader loader = this.getClass().getClassLoader();
		File binFile = extractFromResource(loader, binPath);
		binFile.setExecutable(true, true);
		m_binPath = binFile.getPath();
		m_resourcePaths = new String[resources.length];
		
		for (int i = 0; i < resources.length; i++)
		{
			m_resourcePaths[i] = extractFromResource(loader, resources[i]).getPath();
		}
		
		m_commBuilder = cBuilder;
		m_exeTimeout = exeTimeout;
		m_resultHandler = resultHandler;
	}
	
	public String run(String input)
	{
		File req = tmpFile("request-", ".p");
		writeStringToFile(req, input);
		CommandLine cl = new CommandLine(m_binPath);
		cl.addArguments(m_commBuilder.buildCommandLine(m_resourcePaths, req.getPath()));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		execute(cl, out, m_exeTimeout);
		req.delete();
		return m_resultHandler.parse(out.toString());
	}
	
	public static ExecutionEngine VampirePrime = new ExecutionEngine(new CommandLineArgsBuilder() {
		
		@Override
		public String[] buildCommandLine(String[] resourcePaths,
				String inputFilePath)
		{
			return new String[] { "-t", "300", "-m",
								"300000", "--elim_def", "0", "--selection", "8",
								"--config", resourcePaths[0], "--max_number_of_answers", "100",
								"--inconsistencies_as_answers", "off",
								"--limited_abs_lit_chaining", "on", 
								"--proof", "on", inputFilePath };
		}
	}, ResultHandler.IdentityHandler, 5 * 60 * 1000, "vkernel", "answer_predicates.xml");
	
	// TODO Update for Eprover
//	public static ExecutionEngine EProver = new ExecutionEngine(new CommandLineArgsBuilder() {
//		
//		@Override
//		public String[] buildCommandLine(String[] resourcePaths,
//				String inputFilePath)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//	}, ResultHandler.IdentityHandler,  5 * 60 * 1000, "");
}

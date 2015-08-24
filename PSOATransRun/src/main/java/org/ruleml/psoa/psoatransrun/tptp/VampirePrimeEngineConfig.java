package org.ruleml.psoa.psoatransrun.tptp;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.extractFromResource;

import java.io.File;

public class VampirePrimeEngineConfig {
	public String binPath, answerPredicatePath;
	public long timeout = 300;
	
	public VampirePrimeEngineConfig()
	{
		this(300);
	}
	
	public VampirePrimeEngineConfig(long timeout)
	{
		ClassLoader loader = this.getClass().getClassLoader();
		File bin = extractFromResource(loader, "vampirePrime/vkernel");
		bin.setExecutable(true, true);
		
		this.binPath = bin.getAbsolutePath();
		this.answerPredicatePath = extractFromResource(loader, "vampirePrime/answer_predicates.xml").getAbsolutePath();
		this.timeout = timeout;
	}
	
	public VampirePrimeEngineConfig(String binPath, String answerPath, long timeout)
	{
		this.binPath = binPath;
		this.answerPredicatePath = answerPath;
		this.timeout = timeout;
	}
}

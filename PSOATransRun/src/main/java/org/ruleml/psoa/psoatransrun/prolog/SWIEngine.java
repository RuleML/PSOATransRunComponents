package org.ruleml.psoa.psoatransrun.prolog;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

import java.io.*;

import org.apache.commons.exec.OS;
import org.ruleml.psoa.psoatransrun.PSOATransRunException;
import org.ruleml.psoa.psoatransrun.engine.EngineConfig;

import com.declarativa.interprolog.SWISubprocessEngine;

/**
 * SWI Engine
 * 
 * */
public class SWIEngine extends PrologEngine {
	private String m_swiBinPath, m_swiFolder;
	private File m_transKBFile;

	/**
	 * SWI engine configuration
	 * 
	 * */
	public static class Config extends EngineConfig {
		public String swiFolderPath;
	}

	public SWIEngine() {
		this(new Config());
	}
	
	public SWIEngine(Config config) {
		this(config, false);
	}
	
	/**
	 * Initialize SWIEngine
	 * 
	 * @param config        the configuration
	 * @param delayStart    if true, start the engine at initialization time; otherwise, start the engine when KB is loaded
	 * */
	public SWIEngine(Config config, boolean delayStart) {
		System.out.println("Experimental SWI support");
		//TODO: Actually configure SWI Engine
		// Configure swi installation folder
		// For Linux the command providing the paths is 
		// swipl -dump-runtime-variables
		m_swiFolder = config.swiFolderPath;

		if (m_swiFolder == null || !(new File(m_swiFolder)).exists())
			m_swiFolder = System.getenv("PLBASE");
		
		if (m_swiFolder == null)
			throw new PSOATransRunException("Unable to locate SWI installation folder.");
		
		File f = new File(m_swiFolder);
		if (!(f.exists() && f.isDirectory()))
			throw new PSOATransRunException("SWI installation folder " + m_swiFolder + " does not exist");
		
		// Find the path of SWI binary
		if (OS.isFamilyUnix() || OS.isFamilyMac())
		{
			m_swiBinPath = "/usr/lib/swi-prolog/bin/amd64/";
		}

//			f = new File(f, "config");
//			File[] subdirs = f.listFiles();
//			if (subdirs == null || subdirs.length == 0)
//				throw new PSOATransRunException("Cannot find SWI binary: " + f.getAbsolutePath() + " does not exist or is empty.");
//			
//			File swiFile = null;
//			for (File dir : subdirs)
//			{
//				// /usr/lib/swi-prolog/bin/amd64 for me
//				//
//				File f1 = new File(dir, "bin/xsb");
//				if (f1.canExecute())
//					swiFile = f1;
//				
//				if (dir.getName().contains("x86"))
//					break;
//			}
//			
//			if (swiFile != null)
//				m_swiBinPath = swiFile.getAbsolutePath();
//			else
//				throw new PSOATransRunException("Cannot find executable xsb binary in " + f.getAbsolutePath());
//		}
//		else if (OS.isFamilyWindows())
//		{
//			f = new File(f, "config\\x86-pc-windows\\bin\\xsb");
//			m_swiBinPath = f.getAbsolutePath();
//		}
//		else
//		{
//			throw new PSOATransRunException("Unsupported operating system.");
//		}
//		
		// Start SWI engine
		if (!delayStart)
			m_engine = new SWISubprocessEngine(m_swiBinPath);
		
		// Set translated KB
		String transKBPath = config.transKBPath;
		try
		{
			if (transKBPath != null)
			{
				if (!transKBPath.endsWith(".pl") && !transKBPath.endsWith(".P"))
					throw new PSOATransRunException("Prolog translation output file name must end with .pl or .P: " + transKBPath);
				m_transKBFile = new File(transKBPath);
				m_transKBFile.createNewFile();
			}
			else
				m_transKBFile = tmpFile("tmp-", ".pl");
		}
		catch (IOException e)
		{
			throw new PSOATransRunException(e);
		}
		
	}

	@Override
	public void loadKB(String kb) {
		if (m_engine == null || m_engine.isShutingDown())
			m_engine = new SWISubprocessEngine(m_swiBinPath);
		
		try(PrintWriter writer = new PrintWriter(m_transKBFile))
		{
			writer.println(":- use_module(library(tabling)).");
			writer.println(":- table memterm/2.");
			writer.println(":- index memterm/2-2.");
			writer.println(":- table sloterm/3.");
			writer.println(":- index sloterm/3-2.");
			writer.println(":- table prdsloterm/4.");
			writer.println(":- index prdsloterm/4-2.");
			writer.println(":- index prdsloterm/4-3.");
			
			// Assume a maximum tuple length of 10 
			for (int i = 2; i < 11; i++)
			{
				writer.println(":- table tupterm/" + i + ".");
				writer.println(":- table prdtupterm/" + (i + 1) + ".");
				writer.println(":- index prdtupterm/" + (i + 1) + "-2.");
			}
			
			// Configure SWI
			//writer.println(":- set_prolog_flag(unknown,fail).");  // Return false for (sub)queries using unknown predicates
			//writer.println(":- import datime/1, local_datime/1 from standard.");  // Selectively import 2 predicates (works only for external calls inside KB rules)
			
			writer.print(kb);
		}
		
		catch (FileNotFoundException e)
		{
			throw new PSOATransRunException(e);
		}

		String path = m_transKBFile.getPath();
		path = path.substring(0, path.length() - 2).concat("xwam");			
		File xwamFile = new File(path);
		if (xwamFile.exists())
			xwamFile.delete();
		
		if (m_engine.consultAbsolute(m_transKBFile))
		{
			if (xwamFile.exists())
				xwamFile.deleteOnExit();
		}
		else
		{
			m_engine.interrupt();
			throw new PSOATransRunException("Failed to load KB");
		}
	}
}
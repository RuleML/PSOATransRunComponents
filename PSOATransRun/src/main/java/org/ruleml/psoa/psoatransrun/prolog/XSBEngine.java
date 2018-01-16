package org.ruleml.psoa.psoatransrun.prolog;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

import java.io.*;

import org.apache.commons.exec.OS;
import org.ruleml.psoa.psoatransrun.PSOATransRunException;
import org.ruleml.psoa.psoatransrun.engine.EngineConfig;

import com.declarativa.interprolog.XSBSubprocessEngine;

/**
 * XSB Engine
 * 
 * */
public class XSBEngine extends PrologEngine {
	private String m_xsbBinPath, m_xsbFolder;
	private File m_transKBFile;

	/**
	 * XSB engine configuration
	 * 
	 * */
	public static class Config extends EngineConfig {
		public String xsbFolderPath;
	}

	public XSBEngine() {
		this(new Config());
	}
	
	public XSBEngine(Config config) {
		this(config, false);
	}
	
	/**
	 * Initialize XSBEngine
	 * 
	 * @param config        the configuration
	 * @param delayStart    if true, start the engine at initialization time; otherwise, start the engine when KB is loaded
	 * */
	public XSBEngine(Config config, boolean delayStart) {
		
		// Configure xsb installation folder
		m_xsbFolder = config.xsbFolderPath;

		if (m_xsbFolder == null || !(new File(m_xsbFolder)).exists())
			m_xsbFolder = System.getenv("XSB_DIR");
		
		if (m_xsbFolder == null)
			throw new PSOATransRunException("Unable to locate XSB installation folder.");
		
		File f = new File(m_xsbFolder);
		if (!(f.exists() && f.isDirectory()))
			throw new PSOATransRunException("XSB installation folder " + m_xsbFolder + " does not exist");
		
		// Find the path of XSB binary
		if (OS.isFamilyUnix() || OS.isFamilyMac())
		{
			f = new File(f, "config");
			File[] subdirs = f.listFiles();
			if (subdirs == null || subdirs.length == 0)
				throw new PSOATransRunException("Cannot find XSB binary: " + f.getAbsolutePath() + " does not exist or is empty.");
			
			File xsbFile = null;
			for (File dir : subdirs)
			{
				File f1 = new File(dir, "bin/xsb");
				if (f1.canExecute())
					xsbFile = f1;
				
				if (dir.getName().contains("x86"))
					break;
			}
			
			if (xsbFile != null)
				m_xsbBinPath = xsbFile.getAbsolutePath();
			else
				throw new PSOATransRunException("Cannot find executable xsb binary in " + f.getAbsolutePath());
		}
		else if (OS.isFamilyWindows())
		{
			f = new File(f, "config\\x86-pc-windows\\bin\\xsb");
			m_xsbBinPath = f.getAbsolutePath();
		}
		else
		{
			throw new PSOATransRunException("Unsupported operating system.");
		}
		
		// Start XSB engine
		if (!delayStart)
			m_engine = new XSBSubprocessEngine(m_xsbBinPath);
		
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
			m_engine = new XSBSubprocessEngine(m_xsbBinPath);
		
		try(PrintWriter writer = new PrintWriter(m_transKBFile))
		{
			writer.println(":- table(memterm/2).");
			writer.println(":- index memterm/2-2.");
			writer.println(":- table(sloterm/3).");
			writer.println(":- index sloterm/3-2.");
			writer.println(":- table(prdsloterm/4).");
			writer.println(":- index prdsloterm/4-2.");
			writer.println(":- index prdsloterm/4-3.");
			
			// Assume a maximum tuple length of 10
			for (int i = 2; i < 12; i++)
			{
				writer.println(":- table(tupterm/" + i + ").");
				writer.println(":- table(prdtupterm/" + (i + 1) + ").");
				writer.println(":- index prdtupterm/" + (i + 1) + "-2.");
			}
			
			// Configure XSB
			writer.println(":- set_prolog_flag(unknown,fail).");  // Return false for (sub)queries using unknown predicates
			writer.println(":- import datime/1, local_datime/1 from standard.");  // Selectively import 2 predicates (works only for external calls inside KB rules)
			
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
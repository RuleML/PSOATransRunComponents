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
	
	// Maximum tuple length of psoa atoms for tupterm/prdtupterm
	protected static final int MAX_TUPLE_LEN_FOR_TABLING = 10;

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
		// Configure swi installation folder
		m_swiFolder = config.swiFolderPath;

		if (m_swiFolder == null || !(new File(m_swiFolder)).exists())
			// Search by default install locations
			if (OS.isFamilyUnix() || OS.isFamilyMac())
			{
			    // Look for the executable at /usr/bin (Linux),
				// /Applications/SWI-Prolog.app/Contents/MacOS/swipl or/usr/local/bin/swipl	(MacOS)
				
				String bin_path;
				
				if (OS.isFamilyMac())
					bin_path = "/Applications/SWI-Prolog.app/Contents/MacOS/swipl";
				else
					bin_path = "/usr/bin/swipl";
				File exec = new File(bin_path);
				if (!(exec.exists()))
				{
					if (m_swiFolder == null)
					{
						if (!OS.isFamilyMac())
							throw new PSOATransRunException(
								  "Cannot find SWI binary: Please install SWI Prolog " 
								+ "or specify a SWI Prolog directory.\n\n"
								+ "You can install SWI Prolog through your package manager\n\n"
								+ "If you are using debian/mint/ubuntu try:\n" + " sudo apt-get install swi-prolog\n\n"
								+ "If you are using OpenSUSE try:\n" + " sudo zypper in swipl");
						else
						{
							// Search for brew install
							File exec_brew = new File("/usr/local/bin/swipl");
							if (exec_brew.exists())
								m_swiFolder = exec_brew.getParentFile().getAbsolutePath();			
							else
								throw new PSOATransRunException("Cannot find SWI binary: Please install SWI Prolog" 
								+ "or specify a SWI Prolog directory.");
						}
					}
					else
						throw new PSOATransRunException("Cannot find SWI binary: " + m_swiFolder + " does not exist or is empty.");
				}
				else
					m_swiFolder = exec.getParentFile().getAbsolutePath();
			}
			
			else if (OS.isFamilyWindows())
			{
				// Search first at default installation location 
				File exec = new File("C:\\Program Files\\swipl");
				if (!(exec.exists()))
				{
					// Have a last look if the executable is located at C:\\Program Files (x86)\
					File exec_32 = new File("C:\\Program Files (x86)\\swipl");
					if (!(exec_32.exists() && exec_32.isDirectory()))
					{
						if (m_swiFolder == null)
							throw new PSOATransRunException("Cannot find SWI binary: Please install SWI Prolog or specify a SWI Prolog directory.");
						else
							throw new PSOATransRunException("Cannot find SWI binary: " + m_swiFolder + " does not exist or is empty.");
							
					}
					else
					{
						m_swiFolder = "C:\\Program Files (x86)\\swipl";
					}
								
				}
				else
					m_swiFolder = "C:\\Program Files\\swipl";					
			}
				
		if (m_swiFolder == null)
			throw new PSOATransRunException("Unable to locate SWI installation folder.");
		
		File f = new File(m_swiFolder);
		if (!(f.exists() && f.isDirectory()))
			throw new PSOATransRunException("SWI installation folder " + m_swiFolder + " does not exist");
		
		// Find the path of SWI binary
		if (OS.isFamilyUnix() || OS.isFamilyMac())
		{	
			if (m_swiFolder.equals("/usr/bin") || 
					m_swiFolder.equals("/Applications/SWI-Prolog.app/Contents/MacOS") || 
					m_swiFolder.equals("/usr/local/bin"))
				{
				m_swiBinPath = m_swiFolder;
				System.out.println("Using SWI Prolog executable found at: " + m_swiFolder) ;
				}
			else
			{
				f = new File(f, "bin");
				File[] subdirs = f.listFiles();
				if (subdirs == null || subdirs.length == 0)
					throw new PSOATransRunException("Cannot find SWI binary: " + f.getAbsolutePath() + " does not exist or is empty.");
				
				File swiFile = null;
				for (File dir : subdirs)
				{
					File f1 = new File(dir, "swipl");
					if (f1.canExecute())
						swiFile = f1;
				}
				
				if (swiFile != null)
				{
					m_swiBinPath = swiFile.getParentFile().getAbsolutePath();
				}
				else
					throw new PSOATransRunException("Cannot find executable swi binary in " + f.getAbsolutePath());
			}
		}
		else if (OS.isFamilyWindows())
		{
			f = new File(f, "bin");
			File f1 = new File(f, "swipl-win.exe");
			if (f1.canExecute())
			{
				m_swiBinPath = f.getAbsolutePath();
			}
			else
				throw new PSOATransRunException("Cannot find executable swi binary in " + f.getAbsolutePath());
		}

		else
		{
			throw new PSOATransRunException("Unsupported operating system.");
		}
		
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
			writer.println(":- discontiguous 'memterm tabled'/2.");
			//writer.println(":- index(memterm/2-2).");
			writer.println(":- table sloterm/3.");
			writer.println(":- discontiguous 'sloterm tabled'/3.");
			//writer.println(":- index(sloterm/3-2).");
			writer.println(":- table prdsloterm/4.");
			writer.println(":- discontiguous 'prdsloterm tabled'/4.");
			//writer.println(":- index(prdsloterm/4-2).");
			//writer.println(":- index(prdsloterm/4-3).");
			
			// Assume a maximum tuple length of MAX_TUPLE_LEN_FOR_TABLING 
			for (int i = 1; i < 2 + MAX_TUPLE_LEN_FOR_TABLING; i++)
			{
				writer.println(":- table tupterm/" + i + ".");
				writer.println(":- discontiguous 'tupterm tabled'/" + i + ".");
				writer.println(":- table prdtupterm/" + (i + 1) + ".");
				writer.println(":- discontiguous 'prdtupterm tabled'/" + (i + 1) + "-2.");
				//writer.println(":- index(prdtupterm/" + (i + 1) + "-2).");
			}
			
			// Configure SWI
			//writer.println(":- set_prolog_flag(unknown,fail).");  // Return false for (sub)queries using unknown predicates
			//writer.println(":- import datime/1, local_datime/1 from standard.");  // Selectively import 2 predicates (works only for external calls inside KB rules)
			writer.println(":- set_prolog_flag(iso,true)."); // Select ISO prolog compatibility
			
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

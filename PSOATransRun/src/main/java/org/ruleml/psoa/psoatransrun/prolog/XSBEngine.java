package org.ruleml.psoa.psoatransrun.prolog;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

import java.io.*;
import java.util.*;

import org.apache.commons.exec.OS;
import org.ruleml.psoa.psoatransrun.Binding;
import org.ruleml.psoa.psoatransrun.Bindings;
import org.ruleml.psoa.psoatransrun.QueryResult;
import org.ruleml.psoa.psoatransrun.engine.ReusableKBEngine;
import org.ruleml.psoa.psoatransrun.test.Watch;
import org.ruleml.psoa.psoatransrun.utils.PSOATransRunException;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.XSBSubprocessEngine;

public class XSBEngine extends ReusableKBEngine {
	private String m_xsbBinPath, m_xsbFolder;
	private File m_transKBFile;
	private XSBSubprocessEngine m_engine;
	
	public XSBEngine(XSBEngineConfig config) {
		m_xsbFolder = config.xsbFolderPath;

		if (m_xsbFolder == null || !(new File(m_xsbFolder)).exists())
			m_xsbFolder = System.getenv("XSB_DIR");
		
		if (m_xsbFolder == null)
			throw new PSOATransRunException("Unable to locate XSB installation folder.");
		
		File f = new File(m_xsbFolder);
		if (!(f.exists() && f.isDirectory()))
			throw new PSOATransRunException("XSB installation folder " + m_xsbFolder + " does not exist");
		
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
		
//		String cmd = "bash -c unset HOME && " + m_xsbBinPath;
//		String cmd = "bash -c export HOME=" + m_xsbFolder +" && " + m_xsbBinPath;
//		System.out.println("Cmd: " + cmd);
//		XSBSubprocessEngine engine = new XSBSubprocessEngine(cmd, true, false);
//		System.out.println(AbstractPrologEngine.class);
//		try
//		{
//			ServerSocket serverSocket = new ServerSocket(0);
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//		m_engine = new XSBSubprocessEngine(m_xsbBinPath, true);
		
		m_engine = new XSBSubprocessEngine(m_xsbBinPath);
//		System.out.println("Successfully starting XSB.");
		
		String transKBPath = config.transKBPath;
		try
		{
			if (transKBPath != null)
			{
				if (!transKBPath.endsWith(".pl") && !transKBPath.endsWith(".P"))
					throw new PSOATransRunException("Translation output file name must end with .pl or .P: " + transKBPath);
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
		PrintWriter writer;
		try
		{
			writer = new PrintWriter(m_transKBFile);
		}
		catch (FileNotFoundException e)
		{
			throw new PSOATransRunException(e);
		}
		
		writer.println(":- table(memterm/2).");
		writer.println(":- table(sloterm/3).");
		for (int i = 2; i < 11; i++)
		{
			writer.println(":- table(tupterm/" + i + ").");
		}
		writer.print(kb);
		writer.close();
		
//		System.out.println(m_transKBFile);
		
		if (m_engine.consultAbsolute(m_transKBFile))
		{
//			println("KB Loaded");
			
			String path = m_transKBFile.getPath();
			path = path.substring(0, path.length() - 2).concat("xwam");			
			File xwamFile = new File(path);
			if (xwamFile.exists())
				xwamFile.deleteOnExit();
		}
		else
		{
			m_engine.interrupt();
			throw new PSOATransRunException("Failed to load KB");
		}
	}

	Watch exeWatch = new Watch("Real execution time");
	@Override
	public QueryResult executeQuery(String query, List<String> queryVars) {
		TermModel result;
		QueryResult r;
		if (queryVars.isEmpty())
		{
			exeWatch.start();
			r = new QueryResult(m_engine.deterministicGoal(query));
			exeWatch.stop();
		}
		else
		{
			StringBuilder prologQueryBuilder = new StringBuilder("findall([");
			for (String queryVar : queryVars)
			{
				prologQueryBuilder.append(queryVar).append(",");
			}
			prologQueryBuilder.setCharAt(prologQueryBuilder.length() - 1, ']');
			prologQueryBuilder.append(",(").append(query).append("),AS),buildTermModel(AS,LM)");
			
			exeWatch.start();
			result = (TermModel)m_engine.deterministicGoal(prologQueryBuilder.toString(), "[LM]")[0];
			exeWatch.stop();
//			result = engine.deterministicGoal("findall([Q1],member(lo1,Q1),AS),buildTermModel(AS,LM)", "[LM]")[0];
			
			prologQueryBuilder.setLength(0);
			prologQueryBuilder = null;
			
			if (result.getChildCount() == 0)
				return new QueryResult(false);
			else
			{				
				Bindings bindings = new Bindings();
				for (TermModel binding : result.flatList())
				{
					Iterator<String> queryVarIter = queryVars.iterator();
					Binding b = new Binding();
					
					for (TermModel term : binding.flatList())
					{
						b.addPair(queryVarIter.next(), term.toString());
					}
					bindings.add(b);
				}
				
				r = new QueryResult(true, bindings);
			}
			
		}
		return r;
	}
	
//	public SolutionIterator executeQuery(String query, List<String> queryVars) {
//		
//	}
	
	public long getTime()
	{
		return exeWatch.totalMicroSeconds();
	}
	
	@Override
	public void dispose() {
		if (m_engine != null)
			m_engine.shutdown();
	}
}
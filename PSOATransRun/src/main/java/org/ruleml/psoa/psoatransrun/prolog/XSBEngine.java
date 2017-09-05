package org.ruleml.psoa.psoatransrun.prolog;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

import java.io.*;
import java.util.*;

import org.apache.commons.exec.OS;
import org.ruleml.psoa.psoatransrun.Substitution;
import org.ruleml.psoa.psoatransrun.SubstitutionSet;
import org.ruleml.psoa.psoatransrun.AnswerIterator;
import org.ruleml.psoa.psoatransrun.PSOATransRunException;
import org.ruleml.psoa.psoatransrun.QueryResult;
import org.ruleml.psoa.psoatransrun.engine.EngineConfig;
import org.ruleml.psoa.psoatransrun.engine.ReusableKBEngine;
import org.ruleml.psoa.psoatransrun.test.Watch;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.XSBSubprocessEngine;

/**
 * XSB Engine
 * 
 * */
public class XSBEngine extends ReusableKBEngine {
	private String m_xsbBinPath, m_xsbFolder;
	private File m_transKBFile;
	private XSBSubprocessEngine m_engine;

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
		m_engine = new XSBSubprocessEngine(m_xsbBinPath);
		
		// Set transated KB
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
	public String language() {
		return "prolog";
	}

	@Override
	public void loadKB(String kb) {
		if (m_engine.isShutingDown())
		{
			m_engine = new XSBSubprocessEngine(m_xsbBinPath);
		}
		
		try(PrintWriter writer = new PrintWriter(m_transKBFile))
		{
			writer.println(":- table(memterm/2).");
			writer.println(":- table(sloterm/3).");
			writer.println(":- table(prdsloterm/4).");
			
			// Assume a maximum tuple length of 10 
			for (int i = 2; i < 11; i++)
			{
				writer.println(":- table(tupterm/" + i + ").");
				writer.println(":- table(prdtupterm/" + (i + 1) + ").");
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
		
		if (m_engine.consultAbsolute(m_transKBFile))
		{
			// Works only for top level external queries: m_engine.deterministicGoal("import datime/1, local_datime/1 from standard");
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

	private Watch m_exeWatch = new Watch("Real execution time");
	
	@Override
	public QueryResult executeQuery(String query, List<String> queryVars, boolean getAllAnswers) {
		TermModel result;
		QueryResult r;
		
		if (queryVars.isEmpty())
		{
			m_exeWatch.start();
			// Remove trailing dot for InterProlog
			String queryToSend = query.substring(0, query.length() - 1); 
			r = new QueryResult(m_engine.deterministicGoal(queryToSend));
			m_exeWatch.stop();
		}
		else
		{
			StringBuilder prologQueryBuilder;
			
			if (getAllAnswers)
			{
				/*
				 * build a prolog query having the form
				 * findall([{Q1},...,{Qn}],({query}),AS),buildTermModel(AS,LM)
				 * 
				 * where {Q1}, ..., {Qn} is the list of returned variables and {query} is 
				 * the query string
				 *  
				 * */
				prologQueryBuilder = new StringBuilder("findall([");
				appendVars(prologQueryBuilder, queryVars, ",");
				prologQueryBuilder.append("],(").append(query);
				// Remove trailing dot for InterProlog
				prologQueryBuilder.setLength(prologQueryBuilder.length() - 1);
				prologQueryBuilder.append("),AS),buildTermModel(AS,LM)");
				
				m_exeWatch.start();
				result = (TermModel)m_engine.deterministicGoal(prologQueryBuilder.toString(), "[LM]")[0];
				m_exeWatch.stop();
				
				SubstitutionSet answers = new SubstitutionSet();
				
				for (TermModel bindings : result.flatList())
				{
					answers.add(createSubstitution(queryVars, bindings));
				}
				
				r = new QueryResult(answers);
			}
			else {
				/*
				 * build a prolog query having the form
				 * {query},buildTermModel([{Q1},...,{Qn}],LM)
				 * 
				 * where {Q1}, ..., {Qn} is the list of returned variables and {query} is 
				 * the query string
				 *  
				 * */
				prologQueryBuilder = new StringBuilder(query);
				// Remove trailing dot for InterProlog
				prologQueryBuilder.setLength(prologQueryBuilder.length() - 1);
				prologQueryBuilder.append(",buildTermModel([");
				appendVars(prologQueryBuilder, queryVars, ",");
				prologQueryBuilder.append("],LM)");

				m_exeWatch.start();
				SolutionIterator iter = m_engine.goal(prologQueryBuilder.toString(), "[LM]");
				m_exeWatch.stop();
				
				r = new QueryResult(new PrologAnswerIterator(iter, queryVars));
			}
			
			// cleanup the builder
			prologQueryBuilder.setLength(0);
			prologQueryBuilder = null;
		}
		
		return r;
	}
	
	private static Substitution createSubstitution(List<String> queryVars, TermModel bindings)
	{
		Iterator<String> queryVarIter = queryVars.iterator();
		Substitution answer = new Substitution();
		StringBuilder b = new StringBuilder(); 
		for (TermModel term : bindings.flatList())
		{
			termToString(b, term);
			answer.addPair(queryVarIter.next(), b.toString());
			b.setLength(0);
		}
		
		return answer;
	}
	
	private static StringBuilder appendVars(StringBuilder b, List<String> vars, String sep)
	{
		Iterator<String> iter = vars.iterator();
		if (iter.hasNext())
		{
			b.append(iter.next());
			while(iter.hasNext()) {
				b.append(sep).append(iter.next());
			}
		}
		
		return b;
	}
	
	private static void termToString(StringBuilder b, TermModel m)
	{
		if (m.isList())
		{
			termsToString(b, m.flatList(), "[", "]");
		}
		else if (m.node instanceof String)
		{
			String s = (String)m.node;
			boolean quote = !Character.isLowerCase(s.charAt(0));
			
			if (quote)
				b.append("'");
			
			for (int i = 0; i< s.length(); i++) {
				char c = s.charAt(i);
				if (c != '\'')
					b.append(c);
				else
					b.append("''");
			}
			
			if (quote)
				b.append("'");
			
			termsToString(b, m.getChildren(), "(", ")");
		}
		else
		{
			b.append(m.node.toString());
		}
	}
	
	private static void termsToString(StringBuilder b, TermModel[] terms, 
			String prefix, String suffix) {
		if (terms == null)
			return;
		
		b.append(prefix);
		for (TermModel t: terms)
		{
			termToString(b, t);
			b.append(",");
		}
		b.setLength(b.length() - 1);
		b.append(suffix);
	}
	
	private static class PrologAnswerIterator extends AnswerIterator {
		private SolutionIterator m_iter = null;
		private List<String> m_vars;
		
		public PrologAnswerIterator(SolutionIterator iter, List<String> vars) {
			m_iter = iter;
			m_vars = vars;
		}
		
		@Override
		public boolean hasNext() {
			return m_iter.hasNext();
		}

		@Override
		public Substitution next() {
			return createSubstitution(m_vars, (TermModel)m_iter.next()[0]);
		}

		@Override
		public void dispose() {
			m_iter.cancel();
		}
	}	
	
	public long getTime()
	{
		return m_exeWatch.totalMicroSeconds();
	}
	
	@Override
	public void shutdown() {
		if (m_engine != null)
			m_engine.shutdown();
	}
}
package org.ruleml.psoa.psoatransrun.test;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.ruleml.psoa.psoa2x.common.TranslatorException;
import org.ruleml.psoa.psoatransrun.PSOATransRun;
import org.ruleml.psoa.psoatransrun.PSOATransRunException;
import org.ruleml.psoa.psoatransrun.QueryResult;

import static org.ruleml.psoa.utils.IOUtil.*;

/**
 * Class for running one test case
 * 
 * */
public class TestCase {
	private int m_numAnsQueries,  // # of queries that have standard answers 
				m_numStandardAns, // Total # of standard answers for all queries 
				m_numSoundAns, // Total sound answers obtained for all queries 
				m_numEngineAns; // Total answers obtained for all queries that have standard answers
	private int m_numKBAxioms, m_numTransKBAxioms, m_numCorrectQueries;
	private long m_totalKBTransTime, m_totalQueryTransTime, m_totalQueryTime;
	private Map<String, String[]> m_incorrectQueries;
	private File m_dir, m_kb;
	private Map<File, QueryResult> m_queryAndAns;
	private PSOATransRun m_system = null;
	
	public TestCase(File testCaseDir, PSOATransRun system)
	{
		m_dir = testCaseDir;
		m_system = system;
		m_kb = null;
		m_queryAndAns = new LinkedHashMap<File, QueryResult>();
		m_incorrectQueries = new LinkedHashMap<String, String[]>();
		
		for (File f : m_dir.listFiles())
		{
			if (!f.isFile())
				continue;
			
			String fname =  f.getName();
			if (fname.endsWith("-KB.psoa"))
				m_kb = f;
			else if (fname.matches(".+-query.+\\.psoa"))
			{
				String path = f.getAbsolutePath();
				int ind = path.lastIndexOf("-query");
				String ansPath = path.substring(0, ind) + "-answer" + path.substring(ind + 6, path.length());
				File ans = new File(ansPath);
				if (ans.exists())
				{
					try
					{
						QueryResult result = QueryResult.parse(ans);
						m_queryAndAns.put(f, result);
						m_numAnsQueries++;
						m_numStandardAns += result.numAnswers();
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
						m_queryAndAns.put(f, null);
					}
				}
				else
				{
					System.err.println("No answer file for " + fname);
					m_queryAndAns.put(f, null);
				}
			}
		}
		
		if (m_kb == null)
			throw new PSOATransRunException("No KB in folder " + m_dir.getName());
	}
	
	private boolean runOnce()
	{
		try
		{
			boolean isTestCasePassed = true;

			// Load KB
			try (FileInputStream kbStream = new FileInputStream(m_kb)) {
				m_system.loadKB(kbStream);
			}
			
			// Execute queries
			for (Map.Entry<File, QueryResult> queryAns : m_queryAndAns.entrySet())
			{
				try {
	//				System.out.println("Execute query " + queryAns.getKey().getName());
					QueryResult stdResult = queryAns.getValue(),
								result = null;

					try (FileInputStream queryStream = new FileInputStream(queryAns.getKey()))
					{
						result = m_system.executeQuery(queryStream);
					}
					
					boolean isSound = false, isComplete = false;
					
					// Compare results and update stats
					if(stdResult.binaryResult())
					{
						if (result.binaryResult())
						{
							int numTotal = result.numAnswers(),
								numSound = result.numCommonAnswers(stdResult);
							m_numEngineAns += numTotal;
							m_numSoundAns += numSound;
							
							isSound = (numTotal == numSound);
							isComplete = (numTotal == numSound);
						}
					}
					else
					{
						isComplete = true;
						if (!result.binaryResult())
						{
	//						m_numSoundAns += 1;
							isSound = true;
						}
						else
							m_numEngineAns += result.numAnswers();
					}
					
					if (isSound && isComplete)
						m_numCorrectQueries++;
					else
					{
						String queryName = queryAns.getKey().getName(); 
						if (!m_incorrectQueries.containsKey(queryName))
						{
							isTestCasePassed = false;
							m_incorrectQueries.put(queryName, null);
//							m_incorrectQueries.put(queryName, new String[] { String.valueOf(result) });
							// TODO: Collect unsound/incomplete answers
						}
					}
				}
				catch (Exception e)
				{
					printErrln("Failed to run query " + queryAns.getKey().getName());
					throw e;
				}
			}
			
			return isTestCasePassed;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean run(int times)
	{		
		boolean isAnswerCorrect = true;
		
		for (int i = times; i > 0; i--)
		{
			isAnswerCorrect = runOnce() && isAnswerCorrect;
		}
		
		for (Entry<String, String[]> q : m_incorrectQueries.entrySet())
		{
			System.err.println("Incorrectly answered query: " + q.getKey());
//			System.err.println("  Wrong answer: " + q.getValue()[0]);
		}
		
		// TODO: Handle different numbers in different runs
		m_numEngineAns /= times;
		m_numSoundAns /= times;
		m_numCorrectQueries /= times;
		m_totalKBTransTime = m_system.kbTransTime() / times;
		m_totalQueryTransTime = m_system.queryTransTime() / times;
		m_totalQueryTime = m_system.executionTime() / times;
		return isAnswerCorrect;
	}
	
	public int numQueries()
	{
		return m_queryAndAns.size();
	}
	
	public int engineAnswers() {
		return m_numEngineAns;
	}

	public int correctEngineAnswers() {
		return m_numSoundAns;
	}

	public int standardAnswers() {
		return m_numStandardAns;
	}

	public long kbTranslateTime() {
		return m_totalKBTransTime;
	}
	
	public long queryTranslateTime() {
		return m_totalQueryTransTime;
	}
	
	public long queryTime() {
		return m_totalQueryTime;
	}
	
	public int numKBAxioms()
	{
		return m_numKBAxioms;
	}
	
	public int numTransKBAxioms()
	{
		return m_numTransKBAxioms;
	}

	public int numCorrectQueries() {
		return m_numCorrectQueries;
	}
	
	public String getDirName()
	{
		return m_dir.getName();
	}
	
	public void cleanup() {
		m_system.dispose();
	}
}

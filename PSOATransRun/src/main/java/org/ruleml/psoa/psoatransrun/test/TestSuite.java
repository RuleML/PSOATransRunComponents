package org.ruleml.psoa.psoatransrun.test;

import java.io.*;
import java.util.ArrayList;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

public class TestSuite {
	private ArrayList<TestCase> m_testCases;
	private int m_correctTestCases, m_totalQueries, m_correctQueries;
	private int m_engineAnswers, m_standardAnswers, m_correctEngineAnswers;
	private int m_translationFailures, m_executionFailures;
	private long m_kbTranslateTime, m_queryTranslateTime, m_queryTime;
	private static final int s_runsEachTestCase = 1;
	
	public TestSuite(String path, String targetLang)
	{
		File dir = new File(path);
		File[] testCases = dir.listFiles();
		
		if (testCases == null)
			System.out.println("No available test cases");
		
		m_testCases = new ArrayList<TestCase>(testCases.length);
		for (File testCaseDir : testCases)
		{
			if (testCaseDir.isDirectory())
				m_testCases.add(new TestCase(testCaseDir, targetLang));
		}
	}
	
	public void run()
	{
		int i = 0;
		for (TestCase tc : m_testCases)
		{
			m_totalQueries += tc.numQueries();
			
			try
			{
				println("Run test case ", (++i), ": ", tc.getDirName(), " (", tc.numQueries(), " queries)");
				if (tc.run(s_runsEachTestCase))
				{
					m_correctTestCases++;
				}
				
				m_engineAnswers += tc.engineAnswers();
				m_correctQueries += tc.numCorrectQueries();
				m_correctEngineAnswers += tc.correctEngineAnswers();
				m_standardAnswers += tc.standardAnswers();
				m_kbTranslateTime += tc.kbTranslateTime();
				m_queryTranslateTime += tc.queryTranslateTime();
				m_queryTime += tc.queryTime();
//				println("Correct queries: ", tc.numCorrectQueries());
//				println(m_correctEngineAnswers, "/", m_engineAnswers, ",");
			}
			catch (Exception e) {
				System.err.println("Failed to run test case " + tc.getDirName());
				e.printStackTrace();
			}
			finally
			{
				tc.cleanup();
			}
		}
	}
	
	public void outputSummary()
	{
		outputSummary(System.out);
	}
	
	public void outputSummary(OutputStream out)
	{
		if (out instanceof PrintStream)
			outputSummary(out);
		else
			outputSummary(new PrintStream(out));
	}
	
	public void outputSummary(PrintStream out)
	{
		out.print("Total test cases: ");
		out.println(m_testCases.size());
		out.print("Passed test cases: ");
		out.println(m_correctTestCases);
		out.print("Total queries: ");
		out.println(m_totalQueries);
		out.print("Correctly answered queries: ");
		out.println(m_correctQueries);
		out.print("Degree of soundness: ");
		out.format("%.2f", m_correctEngineAnswers / (double) m_engineAnswers).println();
		out.print("Degree of completeness: ");
		out.format("%.2f", m_correctEngineAnswers / (double) m_standardAnswers).println();
		out.print("Avg. KB translation time per test case (ms): ");
		out.format("%.2f", m_kbTranslateTime / (double) m_testCases.size() / 1000).println();
		out.print("Avg. Query translation time per test case (ms): ");
		out.format("%.2f", m_queryTranslateTime / (double) m_totalQueries / 1000).println();
		out.print("Avg. execution time per query (ms): ");
		out.format("%.2f", m_queryTime / (double) m_totalQueries / 1000).println();
	}
}

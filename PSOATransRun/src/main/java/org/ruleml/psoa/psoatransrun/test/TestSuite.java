package org.ruleml.psoa.psoatransrun.test;

import java.io.*;
import java.util.ArrayList;

import org.ruleml.psoa.psoatransrun.PSOATransRun;
import org.ruleml.psoa.psoatransrun.PSOATransRunException;

import static org.ruleml.psoa.utils.IOUtil.*;

public class TestSuite {
	private ArrayList<TestCase> m_testCases;
	private PrintStream m_outputStream = System.out;
	private boolean m_verbose; 
	private int m_correctTestCases, m_totalQueries, m_correctQueries;
	private int m_engineAnswers, m_standardAnswers, m_correctEngineAnswers;
	private long m_kbTranslateTime, m_queryTranslateTime, m_queryTime;
	private final int m_runsEachTestCase;
	
	public TestSuite(String path, PSOATransRun system, int runs, boolean verbose)
	{
		this(new File(path), system, runs, verbose);
	}
	
	public TestSuite(File dir, PSOATransRun system, int runs, boolean verbose)
	{
		File[] testCases = dir.listFiles();  // TODO: listFiles could be generalized to structured directories.
		
		if (testCases == null)
			throw new PSOATransRunException("The input path does not denote a directory.");
		
		m_runsEachTestCase = runs;
		m_verbose = verbose;
		m_testCases = new ArrayList<TestCase>(testCases.length);
		for (File testCaseDir : testCases)
		{
			try {
				if (testCaseDir.isDirectory())
				{
					m_testCases.add(new TestCase(testCaseDir, system));
				}
			}
			catch (Exception e)
			{
				printErrln("Failed to parse test case ", testCaseDir.getName());
				e.printStackTrace();
			}
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
				int numQueries = tc.numQueries();
				println("Run test case ", (++i), ": ", tc.getDirName(), " (", numQueries, numQueries > 1? " queries)" : " query)");
				if (tc.run(m_runsEachTestCase, m_verbose))
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
				
				if (m_verbose)
				{
					tc.outputSummary(m_outputStream, "\t");
				}
				
//				println("Correct queries: ", tc.numCorrectQueries());
//				println(m_correctEngineAnswers, "/", m_engineAnswers, ",");
			}
			catch (Exception e) {
				printErrln(e.getMessage());
//				e.printStackTrace();
				printErrln("Failed to run test case " + tc.getDirName());
			}
			finally
			{
				tc.cleanup();
			}
		}
	}
	
	public void setOutputStream(OutputStream out)
	{
		if (out instanceof PrintStream)
			m_outputStream = (PrintStream) out;
		else
			m_outputStream = new PrintStream(out);
	}
	
	public void outputSummary()
	{
		outputSummary(m_outputStream);
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
		out.format("%.2f", m_kbTranslateTime / (double) m_testCases.size()).println();
		out.print("Avg. query translation time per test case (ms): ");
		out.format("%.2f", m_queryTranslateTime / (double) m_totalQueries).println();
		out.print("Avg. execution time per query (ms): ");
		out.format("%.2f", m_queryTime / (double) m_totalQueries).println();
	}
}

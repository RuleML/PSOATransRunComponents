package org.ruleml.psoa.psoatransrun;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.println;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.ruleml.psoa.psoa2x.common.*;
import org.ruleml.psoa.psoa2x.psoa2prolog.*;
import org.ruleml.psoa.psoa2x.psoa2tptp.*;
import org.ruleml.psoa.psoatransrun.engine.*;
import org.ruleml.psoa.psoatransrun.prolog.*;
import org.ruleml.psoa.psoatransrun.tptp.*;
import org.ruleml.psoa.psoatransrun.test.Watch;
import org.ruleml.psoa.psoatransrun.utils.PSOATransRunException;
import org.ruleml.psoa.transformer.TransformerConfig;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;

/**
 * PSOATransRun system for question answering in PSOA RuleML
 * 
 * */
public class PSOATransRun {
	private Translator m_translator;
	private ExecutionEngine m_engine;
	private String m_transKB;
	private Watch m_translateKBWatch, m_translateQueryWatch, m_executionWatch;
	private boolean m_printTrans;
	

	public PSOATransRun(Translator t, ExecutionEngine e)
	{
		m_translator = t;
		m_engine = e;
		
		m_translateKBWatch = new Watch("KB Translation Watch");
		m_translateQueryWatch = new Watch("Query Translation Watch");
		m_executionWatch = new Watch("Execution Watch");
	}
	
	public void setPrintTrans(boolean printTrans)
	{
		m_printTrans = printTrans;
	}
	
	public static PSOATransRun getInstantiation(String targetLang)
	{
		if (targetLang.equalsIgnoreCase("prolog"))
		{
			return new PSOATransRun(new PrologTranslator(new PSOA2PrologConfig()), new XSBEngine(new XSBEngineConfig()));
		}
		else if (targetLang.equalsIgnoreCase("tptp"))
		{
			return new PSOATransRun(new TPTPASOTranslator(new PSOA2TPTPConfig()), new VampirePrimeEngine(new VampirePrimeEngineConfig()));
		}
		
		throw new PSOATransRunException("Unknown target language: " + targetLang);
	}
	
	public static PSOATransRun getInstantiation(String targetLang, TransformerConfig config)
	{
		if (targetLang.equalsIgnoreCase("prolog"))
		{
			return new PSOATransRun(new PrologTranslator((PSOA2PrologConfig)config), new XSBEngine(new XSBEngineConfig()));
		}
		else if (targetLang.equalsIgnoreCase("tptp"))
		{
			return new PSOATransRun(new TPTPASOTranslator((PSOA2TPTPConfig)config), new VampirePrimeEngine(new VampirePrimeEngineConfig()));
		}
		
		throw new PSOATransRunException("Unknown target language: " + targetLang);
	}
	
	public void loadKB(InputStream in)
	{
		loadKB(in, false);
	}
	
	/**
	 * Load an input PSOA KB into PSOATransRun. The input KB is translated 
	 * into the corresponding target language and prepared in the engine. 
	 * 
	 * @param in   input KB stream
	 * @param keepKB   whether to keep translated KB in the memory
	 * 
	 * */
	public void loadKB(InputStream in, boolean keepKB)
	{
		String transKB;

		m_translateKBWatch.start();
		transKB = m_translator.translateKB(in);
		m_translateKBWatch.stop();
		
		if (m_printTrans)
		{
			System.out.println("Translated KB:");
			System.out.println(transKB);
		}
		
		if (m_engine instanceof ReusableKBEngine)
		{
			((ReusableKBEngine) m_engine).loadKB(transKB);
			if (keepKB)
			{
				m_transKB = transKB;
			}
		}
		else
			m_transKB = transKB;
	}
	
	/**
	 * Get the translated KB
	 * 
	 * @return   translated KB
	 * 
	 * */
	public String getTransKB()
	{
		return m_transKB;
	}
	
	/**
	 * Execute PSOA query and return all answers
	 * 
	 * @param query   input stream of PSOA query
	 * 
	 * @return   query result containing all answers
	 * 
	 * */
	public QueryResult executeQuery(InputStream query)
	{
		String transQuery;
		List<String> queryVars;
		QueryResult result;
		
		m_translateQueryWatch.start();
		transQuery = m_translator.translateQuery(query);
		queryVars = m_translator.getQueryVars();
		m_translateQueryWatch.stop();
		
		m_executionWatch.start();
		if (m_engine instanceof ReusableKBEngine)
			result = ((ReusableKBEngine) m_engine).executeQuery(transQuery, queryVars);
		else
			result = m_engine.executeQuery(m_transKB, transQuery, queryVars);
		m_executionWatch.stop();
		
		result.inverseTranslate(m_translator);
		return result;
	}
	
	
	public void dispose()
	{
		m_engine.dispose();
	}
	
	public long kbTranslationTime()
	{
		return m_translateKBWatch.totalMicroSeconds();
	}
	
	public long queryTranslationTime()
	{
		return m_translateQueryWatch.totalMicroSeconds();
	}
	
	public long executionTime()
	{
		if (m_engine instanceof XSBEngine)
			return ((XSBEngine) m_engine).getTime();
		else
			return m_executionWatch.totalMicroSeconds();
	}
	/*
	private static void executeQuery(String transQuery, PrologTranslator translator)
	{
		Set<Entry<String, String>> varMapEntries;
		TermModel result;
		
		if (m_outputTrans)
		{
			println("Translated Query:");
			println(transQuery + ".");
			println();
		}
		varMapEntries = translator.getQueryVarMap().entrySet();
		
		println("Answer(s):");
		if (varMapEntries.isEmpty())
		{	
			println(_engine.deterministicGoal(transQuery)? "Yes" : "No");
		}
		else if (_getAllAnswers)
		{
			StringBuilder outputBuilder = new StringBuilder("findall([");
			for (Entry<String, String> entry : varMapEntries)
			{
				outputBuilder.append("\'?").append(entry.getValue()).append("=\',");
				outputBuilder.append(entry.getKey()).append(",");
			}
			outputBuilder.setCharAt(outputBuilder.length() - 1, ']');
			outputBuilder.append(",(").append(transQuery).append("),AS),buildTermModel(AS,LM)");
			
			result = (TermModel)_engine.deterministicGoal(outputBuilder.toString(), "[LM]")[0];
//			result = engine.deterministicGoal("findall([Q1],member(lo1,Q1),AS),buildTermModel(AS,LM)", "[LM]")[0];
			
			if (result.getChildCount() > 0)
			{
				Set<String> answers = new HashSet<String>();
				StringBuilder ansBuilder = new StringBuilder();
				boolean separator = false;
				for (TermModel bindings : result.flatList())
				{					
					for (TermModel term : bindings.flatList())
					{
						ansBuilder.append(translator.inverseTranslateTerm(term.toString()));
						if (separator)
							ansBuilder.append(",");
						separator = !separator;
					}
					ansBuilder.setLength(ansBuilder.length() - 1);
					
					String ans = ansBuilder.toString();
					if (answers.add(ans))
						println(ans);
					ansBuilder.setLength(0);
				}
			}
			println();
		}
		else
		{
			StringBuilder queryBuilder = new StringBuilder(transQuery);
			queryBuilder.append(",buildTermModel([");
			for (Entry<String, String> entry : varMapEntries)
			{
				queryBuilder.append("\'?").append(entry.getValue()).append("=\',");
				queryBuilder.append(entry.getKey()).append(",");
			}
			queryBuilder.setCharAt(queryBuilder.length() - 1, ']');
			queryBuilder.append(",LM)");

			String input = ";";
			Scanner sc = new Scanner(System.in);
			SolutionIterator iter = _engine.goal(queryBuilder.toString(), "[LM]");
			
			do
			{
				if (input.equals(";"))
				{
					if (!iter.hasNext())
					{
						System.out.println("No");
						break;
					}
					TermModel m = (TermModel)iter.next()[0];
					
					StringBuilder ansBuilder = new StringBuilder();
					boolean separator = false;
					for (TermModel term : m.flatList())
					{
						ansBuilder.append(translator.inverseTranslateTerm(term.toString()));
						if (separator)
							ansBuilder.append(",");
						separator = !separator;
					}
					ansBuilder.setCharAt(ansBuilder.length() - 1, '\t');
					
					System.out.print(ansBuilder);
				}
				else if (input.equals(""))
				{
					iter.cancel();
					break;
				}
				
				input = sc.nextLine();
			} while (true);
		}
*/
		public void shutdown() {
			// TODO Auto-generated method stub
			
		}
}
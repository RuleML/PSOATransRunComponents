package org.ruleml.psoa.psoatransrun;

import java.io.InputStream;
import java.util.List;

import org.ruleml.psoa.psoa2x.common.*;
import org.ruleml.psoa.psoa2x.psoa2prolog.*;
import org.ruleml.psoa.psoa2x.psoa2tptp.*;
import org.ruleml.psoa.psoatransrun.engine.*;
import org.ruleml.psoa.psoatransrun.prolog.*;
import org.ruleml.psoa.psoatransrun.tptp.*;
import org.ruleml.psoa.psoatransrun.test.Watch;
import org.ruleml.psoa.psoatransrun.utils.PSOATransRunException;

public class PSOATransRun {
	private Translator m_translator;
	private ExecutionEngine m_engine;
	private String m_transKB;
	private Watch m_translateKBWatch, m_translateQueryWatch, m_executionWatch;
	private int m_numPSOAAxioms, m_numTargetAxioms;
	
	public PSOATransRun(Translator t, ExecutionEngine e)
	{
		m_translator = t;
		m_engine = e;
		
		m_translateKBWatch = new Watch("KB Translation Watch");
		m_translateQueryWatch = new Watch("Query Translation Watch");
		m_executionWatch = new Watch("Execution Watch");
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
	
	public static PSOATransRun getInstantiation(String targetLang, TranslatorConfig config)
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
		String transKB;

		m_translateKBWatch.start();
		transKB = m_translator.translateKB(in);
		m_translateKBWatch.stop();
		
		if (m_engine instanceof ReusableKBEngine)
			((ReusableKBEngine) m_engine).loadKB(transKB);
		else
			m_transKB = transKB;
	}
	
	public QueryResult executeQuery(InputStream query)
	{
		String transQuery;
		List<String> queryVars;
		QueryResult result;
		
		m_translateQueryWatch.start();
		transQuery = m_translator.translateQuery(query);
		m_translateQueryWatch.stop();
		
		queryVars = m_translator.getQueryVars();
		
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
}
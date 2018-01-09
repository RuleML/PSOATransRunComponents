package org.ruleml.psoa.psoatransrun.prolog;

import java.util.Iterator;
import java.util.List;

import org.ruleml.psoa.psoatransrun.AnswerIterator;
import org.ruleml.psoa.psoatransrun.QueryResult;
import org.ruleml.psoa.psoatransrun.Substitution;
import org.ruleml.psoa.psoatransrun.SubstitutionSet;
import org.ruleml.psoa.psoatransrun.engine.ReusableKBEngine;
import org.ruleml.psoa.psoatransrun.test.Watch;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.SubprocessEngine;

abstract public class PrologEngine extends ReusableKBEngine {
	protected SubprocessEngine m_engine;
	private Watch m_exeWatch = new Watch("Real execution time");

	@Override
	public String language() {
		return "prolog";
	}
	
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
		return m_exeWatch.totalMilliSeconds();
	}

	public void resetTimer() {
		m_exeWatch.reset();
	}
	
	@Override
	public void shutdown() {
		if (m_engine != null)
			m_engine.shutdown();
	}
}

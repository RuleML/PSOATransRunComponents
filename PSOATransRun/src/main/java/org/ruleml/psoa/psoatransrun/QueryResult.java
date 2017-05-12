package org.ruleml.psoa.psoatransrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ruleml.psoa.psoa2x.common.Translator;

/**
 * Query result from engines
 * 
 * */
public class QueryResult
{
	private SubstitutionSet m_answers;
	private IteratorWrapper m_ansIter;
	
	/**
	 * Construct a query result with a boolean value
	 * 
	 * @param result   a boolean query result
	 * 
	 * */
	public QueryResult(boolean result)
	{
		m_answers = new SubstitutionSet();
		  
		if (result)
		{
			// Create an empty substitution for the "Yes" answer
			m_answers.add(new Substitution());
		}
	}
	
	/**
	 * Construct a query result from a set of substitutions 
	 * 
	 * @param substitutionSets    a set of substitutions
	 * 
	 * */
	public QueryResult(SubstitutionSet substitutionSets) {
		m_answers = substitutionSets;
	}

	
	/**
	 * Construct a query result from an answer iterator 
	 * 
	 * @param iter    an answer iterator
	 * 
	 * */
	public QueryResult(AnswerIterator iter) {
		m_ansIter = this.new IteratorWrapper(iter);
	}
	
	
	/**
	 * Add a new answer to the existing set of answers. This only works if the 
	 * query result was not constructed from an answer iterator. 
	 * 
	 * @param s    the substitution to be added
	 * 
	 * */
	public void addAnswer(Substitution s)
	{
		m_answers.add(s);
	}
	
	public boolean binaryResult()
	{
		return m_ansIter != null? m_ansIter.hasNext() : !m_answers.isEmpty();
	}

	/**
	 * Get an iterator to iterate over the answers
	 * */
	public AnswerIterator iterator() {
		if (m_ansIter != null)
			return m_ansIter;
		else
			return new SimpleAnswerIterator(m_answers.iterator());
	}
	
	public void inverseTranslate(Translator translator)
	{
		if (m_answers != null)
		{
			m_answers.inverseTranslate(translator);
		}
		else
		{
			m_ansIter.setInverseTranslator(translator);
		}
	}
	
	public SubstitutionSet getAnswers()
	{
		if (m_answers == null)
		{
			m_answers = new SubstitutionSet();
			
			while (m_ansIter.hasNext())
			{
				m_answers.add(m_ansIter.next());
			}
		}
		
		return m_answers;
	}
	
	public int numAnswers()
	{
		return getAnswers().size();
	}
	
	public int numCommonAnswers(QueryResult r)
	{
		return getAnswers().numCommonBindings(r.m_answers);
	}
	
	public static QueryResult parse(File f) throws FileNotFoundException
	{
		try (Scanner sc = new Scanner(f)) {
			String s = sc.nextLine();
			if (s.equalsIgnoreCase("yes"))
				return new QueryResult(true);
			else if (s.equalsIgnoreCase("no"))
				return new QueryResult(false);
			else
			{
				SubstitutionSet answers = SubstitutionSet.parseBindings(sc);
				answers.add(Substitution.parse(s));
				return new QueryResult(answers);
			}
		}
	}
	
	@Override
	public String toString() {
		return getAnswers().toString();
	}
	
	private class IteratorWrapper extends SimpleAnswerIterator {
		private Translator m_invTranslator = null;
		
		public IteratorWrapper(AnswerIterator iter) {
			super(iter);
		}

		public void setInverseTranslator(Translator t)
		{
			m_invTranslator = t;
		}
		
		@Override
		public Substitution next() {
			Substitution subs = m_iter.next();
			if (m_invTranslator != null)
				subs.inverseTranslate(m_invTranslator);
			if (m_answers == null)
				m_answers = new SubstitutionSet();
			m_answers.add(subs);
			return subs;
		}

		@Override
		public void dispose() {
			((AnswerIterator) m_iter).dispose();
			m_iter = null;
			m_invTranslator = null;
		}
	} 
}

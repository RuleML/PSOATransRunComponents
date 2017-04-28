package org.ruleml.psoa.psoatransrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import org.ruleml.psoa.psoa2x.common.Translator;

public class QueryResult implements Iterable<Substitution>
{
	private boolean m_result;
	private SubstitutionSet m_answers;
	private AnswerIterator m_ansIter;
	
	public QueryResult(boolean result)
	{
		m_result = result;
		if (m_result)
			m_answers = new SubstitutionSet();
	}
	
	public QueryResult(boolean result, SubstitutionSet substitutionSets)
	{
		if (!result)
		{
			if (substitutionSets != null)
				throw new IllegalArgumentException("Substitutions must be null if the query result is false");
		}
		else if (substitutionSets == null)
		{
			substitutionSets = new SubstitutionSet();
		}
		
		m_result = result;
		m_answers = substitutionSets;
	}
	
	public QueryResult(SubstitutionSet substitutionSets) {
		m_result = !substitutionSets.isEmpty(); 
		if (m_result)
		{
			m_answers = substitutionSets;
		}
	}

	public void addAnswer(Substitution s)
	{
		m_answers.add(s);
	}
	
	public boolean binaryResult()
	{
		return m_result;
	}
	
	public void inverseTranslate(Translator translator)
	{
		if (m_result)
		{
			m_answers.inverseTranslate(translator);
		}
	}
	
	public SubstitutionSet getAnswers()
	{
		return m_answers;
	}
	
	public int numAnswers()
	{
		return m_answers == null? 0 : m_answers.size();
	}
	
	public int numCommonAnswers(QueryResult r)
	{
		if (!m_result || !r.m_result)
			return 0;
		else
			return m_answers.numCommonBindings(r.m_answers);
	}
	
	public static QueryResult parse(File f) throws FileNotFoundException
	{
		Scanner sc = new Scanner(f);
		try
		{
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
		finally
		{
			sc.close();
		}
	}
	
	public static class AnswerIterator implements Iterator<Substitution> {

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Substitution next() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	@Override
	public String toString() {
		if (m_answers == null)
			return "no";
		
		if (m_answers.isEmpty())
			return m_result? "yes" : "no";
					
		return m_answers.toString();
	}

	@Override
	public Iterator<Substitution> iterator() {
		if (m_ansIter == null)
			return m_ansIter;
		
		return m_answers.iterator();
	}
}

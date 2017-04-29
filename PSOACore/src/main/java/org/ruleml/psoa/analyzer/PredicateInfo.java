package org.ruleml.psoa.analyzer;

import java.util.*;

/** 
 * Class for storing predicate information in KB.
 *  
 * */
public class PredicateInfo
{
	private String m_name;
	SortedSet<Integer> m_positionalArities;
	boolean m_hasOID, m_hasSlot;
	
	public PredicateInfo(String name)
	{
		m_name = name;
		m_positionalArities = new TreeSet<Integer>();
		m_hasOID = false;
		m_hasSlot = false;
	}
	
	public boolean isPurelyRelational()
	{
		return !m_hasOID && !m_hasSlot;
	}
	
	public SortedSet<Integer> getPositionalArities()
	{
		return Collections.unmodifiableSortedSet(m_positionalArities);
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public void addPositionalArity(int arity)
	{
		m_positionalArities.add(arity);
	}

	public void addPositionalArities(Collection<Integer> positionalArities)
	{
		m_positionalArities.addAll(positionalArities);
	}
	
	public void setHasOID(boolean b)
	{
		m_hasOID = b;
	}

	public void setHasSlot(boolean b)
	{
		m_hasSlot = b;
	}
	
	public boolean isTop()
	{
		return m_name.equals("Top");
	}
}

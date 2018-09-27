package org.ruleml.psoa.analyzer;

import java.util.*;

/** 
 * Class for storing predicate information in KB.
 *  
 * */
public class PredicateInfo
{
	private String m_name;
	private boolean m_isTop;
	SortedSet<Integer> m_positionalArities;
	boolean m_hasOID, m_hasSlot, m_hasIndepTuple, m_hasMultiTuple, m_hasNoDesc;
	
	public PredicateInfo(String name)
	{
		m_name = name;
		m_isTop = name.equals("Top");
		m_positionalArities = new TreeSet<Integer>();
		m_hasOID = false;
		m_hasSlot = false;
		m_hasIndepTuple = false;
		m_hasMultiTuple = false;
		m_hasNoDesc = false;
	}
	
	public boolean isRelational()
	{
		return !m_hasOID && !m_hasSlot && !m_hasIndepTuple && !m_hasMultiTuple && !m_hasNoDesc && !m_isTop;
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
	
	public void setHasIndepTuple(boolean b)
	{
		m_hasIndepTuple = b;
	}
	
	public void setHasMultiTuple(boolean b)
	{
		m_hasMultiTuple = b;
	}
	
	public void setHasNoDesc(boolean b)
	{
		m_hasNoDesc = b;
	}
	
	public boolean isTop()
	{
		return m_name.equals("Top");
	}
}

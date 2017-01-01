package org.ruleml.psoa;

import java.util.Set;

public class FreshNameGenerator
{
	private static int m_constID = 0, m_varID = 0;
	
	public static String freshConstName()
	{
		return String.valueOf(++m_constID);
	}
	
	public static String freshVarName()
	{
		return String.valueOf(++m_varID);
	}
	
	public static String freshConstName(Set<String> consts)
	{
		while (true)
		{
			String name = String.valueOf(++m_constID);
			if (!consts.contains(name))
				return name;
		}
	}
	
	public static String freshVarName(Set<String> vars)
	{
		while (true)
		{
			String name = String.valueOf(++m_varID);
			if (!vars.contains(name))
				return name;
		}
	}
	
	public static void reset()
	{
		m_constID = 0;
		m_varID = 0;
	}
	
	public static void resetVarCounter()
	{
		m_varID = 0;
	}
}
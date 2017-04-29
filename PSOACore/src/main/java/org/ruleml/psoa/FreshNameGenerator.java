package org.ruleml.psoa;

import java.util.Set;

/**
 * Generator for fresh constants and variables, using two 
 * global counters.
 * 
 * */
public class FreshNameGenerator
{
	private static int m_constID = 0, m_varID = 0;

	
	/**
	 * Generate fresh constant name
	 * 
	 * @return   the generated constant name
	 * 
	 */
	public static String freshConstName()
	{
		return String.valueOf(++m_constID);
	}
	
	/**
	 * Generate fresh variable name
	 * 
	 * @return   the generated variable name
	 * 
	 */
	public static String freshVarName()
	{
		return String.valueOf(++m_varID);
	}
	
	/**
	 * Generate fresh constant names that are not in the set <code>excludedNames</code>
	 * 
	 * @param excludedNames   names that should be avoided in the generation
	 * 
	 * @return   the generated constant name
	 * 
	 * */
	public static String freshConstName(Set<String> excludedNames)
	{
		while (true)
		{
			String name = String.valueOf(++m_constID);
			if (!excludedNames.contains(name))
				return name;
		}
	}

	
	/**
	 * Generate fresh variable names that are not in the set <code>excludedNames</code>
	 * 
	 * @param excludedNames   names that should be avoided in the generation
	 * 
	 * @return   the generated constant name
	 * 
	 * */
	public static String freshVarName(Set<String> excludedNames)
	{
		while (true)
		{
			String name = String.valueOf(++m_varID);
			if (!excludedNames.contains(name))
				return name;
		}
	}
	
	
	/**
	 * Reset global counters for constant and variable generation
	 * 
	 * */
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
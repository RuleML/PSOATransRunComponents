package org.ruleml.psoa;

import java.util.Set;

/**
 * Generator for fresh constants and variables, using two 
 * global counters.
 * 
 * */
public class FreshNameGenerator
{
	private static int s_constID = 0, s_varID = 0;
	
	/**
	 * Generate fresh constant name
	 * 
	 * @return   the generated constant name
	 * 
	 */
	public static String freshConstName()
	{
		return String.valueOf(++s_constID);
	}
	
	/**
	 * Generate fresh variable name
	 * 
	 * @return   the generated variable name
	 * 
	 */
	public static String freshVarName()
	{
		return String.valueOf(++s_varID);
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
			String name = String.valueOf(++s_constID);
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
			String name = String.valueOf(++s_varID);
			if (!excludedNames.contains(name))
				return name;
		}
	}
	
	/**
	 * Reset constant and variable generator states (global counters)
	 * 
	 * */
	public static void reset()
	{
		s_constID = 0;
		resetVarGen();
	}
	
	/**
	 * Reset variable generator state (global counter)
	 * 
	 * */
	public static void resetVarGen()
	{
		s_varID = 0;
	}
}
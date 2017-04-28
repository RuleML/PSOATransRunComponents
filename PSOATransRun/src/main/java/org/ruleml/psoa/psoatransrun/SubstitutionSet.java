package org.ruleml.psoa.psoatransrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.ruleml.psoa.psoa2x.common.Translator;

/**
 * The class is used for operating on substitution sets. 
 * Each instance of the class is a set of substitutions, 
 * which is used to represent a set of query answers in PSOATransRun.
 * 
 * */
public class SubstitutionSet extends HashSet<Substitution> {

	/**
	 * Generated serialization UID
	 */
	private static final long serialVersionUID = 5636749018485266783L;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Substitution b : this)
		{
			sb.append(b).append(System.getProperty("line.separator"));
		}
		
		return sb.toString();
	}
	
	public static SubstitutionSet parseBindings(File f) throws FileNotFoundException
	{
		Scanner sc = new Scanner(f);
		SubstitutionSet bindings = parseBindings(sc);
		sc.close();
		return bindings;
	}
	
	public int numCommonBindings(SubstitutionSet cmp)
	{
		int common = 0;
		for (Substitution b : this)
		{
			if (cmp.contains(b))
				common++;
		}
		
		return common;
	}
	
	public static SubstitutionSet parseBindings(Scanner sc)
	{
		SubstitutionSet bindings = new SubstitutionSet();
		while (sc.hasNext())
		{
			bindings.add(Substitution.parse(sc.nextLine()));
		}
		
		return bindings;
	}

	public void inverseTranslate(Translator translator) {
		for (Substitution bd : this)
		{
			bd.inverseTranslate(translator);
		}
	}
}

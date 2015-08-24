package org.ruleml.psoa.psoatransrun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.ruleml.psoa.psoa2x.common.Translator;

public class Bindings extends HashSet<Binding> {

	/**
	 * Generated serialization UID
	 */
	private static final long serialVersionUID = 5636749018485266783L;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Binding b : this)
		{
			sb.append(b).append(System.getProperty("line.separator"));
		}
		
		return sb.toString();
	}
	
	public static Bindings parseBindings(File f) throws FileNotFoundException
	{
		Scanner sc = new Scanner(f);
		Bindings bindings = parseBindings(sc);
		sc.close();
		return bindings;
	}
	
	public int numCommonBindings(Bindings cmp)
	{
		int common = 0;
		for (Binding b : this)
		{
			if (cmp.contains(b))
				common++;
		}
		
		return common;
	}
	
	public static Bindings parseBindings(Scanner sc)
	{
		Bindings bindings = new Bindings();
		while (sc.hasNext())
		{
			bindings.add(Binding.parse(sc.nextLine()));
		}
		
		return bindings;
	}

	public void inverseTranslate(Translator translator) {
		for (Binding bd : this)
		{
			bd.inverseTranslate(translator);
		}
	}
}

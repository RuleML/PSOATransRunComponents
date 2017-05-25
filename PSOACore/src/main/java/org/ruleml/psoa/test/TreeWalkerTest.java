package org.ruleml.psoa.test;

import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.PSOAKB;
import org.ruleml.psoa.PSOAQuery;

public class TreeWalkerTest
{
	public static void main(String[] args) throws Exception
	{		
		PSOAKB kb = new PSOAKB();
		
		kb.loadFromFile(args[0]);
		kb.setPrintAfterTransformation(true);
		kb.LPnormalize(new RelationalTransformerConfig());
		
		if (args.length == 2)
		{
			System.out.println();
			PSOAQuery query = new PSOAQuery(kb);
			query.loadFromFile(args[1]);
			query.setPrintAfterTransformation(true);
			query.LPnormalize(new RelationalTransformerConfig());
		}
	}
/*	
	public static void printNodeStream(CommonTreeNodeStream nodes)
	{
		Object o;
		boolean down = true;
		
		System.out.println("Node stream:");
		System.out.print("  (");
		while (!nodes.isEOF(o = nodes.nextElement()))
		{
			String s = o.toString();
			if (s.equals("DOWN"))
			{
				down = true;
				System.out.print(' ');
				System.out.print('(');
			}
			else if (s.equals("UP"))
			{
				System.out.print(')');
			}
			else
			{
				if(down)
					down = false;
				else
					System.out.print(' ');
				System.out.print(s);
			}
		}
		System.out.print(')');
		System.out.println();
		nodes.reset();
	}
	
	public static void printTreeAsNodeStream(CommonTree tree)
	{
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		printNodeStream(nodes);
	}
	
	public static CommonTreeNodeStream createAndPrintNodeStream(CommonTree tree)
	{
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		printNodeStream(nodes);
		return nodes;
	}
*/
}

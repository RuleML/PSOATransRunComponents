package org.ruleml.psoa.test;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.normalizer.*;
import org.ruleml.psoa.parser.*;

public class TreeWalkerTest
{
	public static void main(String[] args) throws Exception
	{
//		ANTLRStringStream fileInput = new ANTLRFileStream("D:\\Programs\\PSOATools\\PSOATransRun\\src\\test\\testCases\\testKB.psoa");
		ANTLRStringStream fileInput = new ANTLRFileStream("D:\\Programs\\PSOATools\\PSOATransRun\\test\\class_membership\\class_membership-KB.psoa");
		PSOAPSLexer lexer = new PSOAPSLexer(fileInput);
		TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		PSOAPSParser parser = new PSOAPSParser(tokens);

		System.err.flush();
		CommonTree parserTree = (CommonTree) parser.top_level_item().getTree(); // get tree from parser

		System.out.println("Before Transformation:");
		System.out.println(parserTree.toStringTree());
		
		// Create a tree node stream from resulting tree
		CommonTreeNodeStream nodes;
		CommonTree transformedTree = parserTree;
		
		nodes = createAndPrintNodeStream(parserTree);
		nodes.setTokenStream(tokens);
		
		System.out.println();
		System.out.println("Objectification:");
		transformedTree = (CommonTree)(new DiscriminativeObjectifier(nodes)).document().getTree();
		System.out.println(transformedTree.toStringTree());
		nodes = createAndPrintNodeStream(transformedTree);
		nodes.setTokenStream(tokens);
		
		System.out.println();
		System.out.println("Skolemization:");
		transformedTree = (CommonTree)(new Skolemizer(nodes)).document().getTree();
		System.out.println(transformedTree.toStringTree());
		nodes = createAndPrintNodeStream(transformedTree);
		nodes.setTokenStream(tokens);
		
		System.out.println();
		System.out.println("Slotribution and Tupribution:");
		transformedTree = (CommonTree)(new SlotTupributor(nodes)).document().getTree();
		System.out.println(transformedTree.toStringTree());
		nodes = createAndPrintNodeStream(transformedTree);
		nodes.setTokenStream(tokens);
		
		System.out.println();
		System.out.println("External Flattening:");
		transformedTree = (CommonTree)(new ExternalFlattener(nodes)).document().getTree();
		System.out.println(transformedTree.toStringTree());		
		nodes = createAndPrintNodeStream(transformedTree);
		nodes.setTokenStream(tokens);
		
		System.out.println();
		System.out.println("Conjunctive Head Splitting:");
		transformedTree = (CommonTree)(new ConjunctiveHeadSplitter(nodes)).document().getTree();
		System.out.println(transformedTree.toStringTree());
		printTreeAsNodeStream(transformedTree);
	}
	
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
}

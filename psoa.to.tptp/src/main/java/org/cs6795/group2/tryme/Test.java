package org.cs6795.group2.tryme;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

public class Test {
/*	public static void main(String[] args) throws Exception {

		// Create an input character stream from standard in
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		// Create an ExprLexer that feeds from that stream
		ExprLexer lexer = new ExprLexer(input);
		// Create a stream of tokens fed by the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// Create a parser that feeds off the token stream
		ExprParser parser = new ExprParser(tokens);
		// Begin parsing at rule prog
		parser.prog();

	}*/

	public static void main(String[] args) throws Exception {
		// Create an input character stream from standard in
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		// Create an ExprLexer that feeds from that stream
		ExprLexer lexer = new ExprLexer(input);
		// Create a stream of tokens fed by the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// Create a parser that feeds off the token stream
		ExprParser parser = new ExprParser(tokens);
		// Begin parsing at rule prog, get return value structure
		ExprParser.prog_return r = parser.prog();
		// WALK RESULTING TREE
		CommonTree t = (CommonTree) r.getTree(); // get tree from parser
		// Create a tree node stream from resulting tree
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
		Eval walker = new Eval(nodes); // create a tree parser
		walker.prog();
		// launch at start rule prog
	}

}

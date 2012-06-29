package org.ruleml;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

/** Command line utility to validate the syntax of PSOA RuleML files. */
public class Validator {

	public static void main(String[] args) {
		boolean importClosure = false;
		boolean hasQueryDoc = false;
		String queryDoc = "";
		String[] ruleBaseFileNames = null;
		LongOpt[] longOpts = new LongOpt[256];

		// Reserved short option names: i ?
		longOpts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?');
		longOpts[1] = new LongOpt("import_closure", LongOpt.NO_ARGUMENT, null,'i');
		longOpts[2] = new LongOpt("query", LongOpt.REQUIRED_ARGUMENT, null, 'q');

		Getopt optionsParse = new Getopt("", args, "?iq:", longOpts);
		for (int opt = optionsParse.getopt(); opt != -1; opt = optionsParse.getopt()) {
			
			switch (opt) {
				case '?':
					printUsage();
					System.exit(1);
	
				case 'i':
					importClosure = true;
					break;
					
				case 'q':
					hasQueryDoc = true;
					queryDoc = optionsParse.getOptarg();
					break;
					
				default:
					assert false;
			}
		}

		int optInd = optionsParse.getOptind();

		if (args.length > optInd) {
			ruleBaseFileNames = new String[args.length - optInd];
			for (int i = optInd; i < args.length; ++i)
				ruleBaseFileNames[i - optInd] = args[i];
		} else {
			System.out.println("% No rule base file specified.");
			printUsage();
			System.exit(1);
		}

		try {
			for (int i = 0; i < ruleBaseFileNames.length; ++i) {
//				createBasicTreeParser(ruleBaseFileNames[i], false).document();
			}
			System.out.println();
			if (hasQueryDoc) {
//				createBasicTreeParser(queryDoc, hasQueryDoc).queries();
			}
			
			

		} catch (Exception ex) {
			System.err.println("Error: " + ex);
			ex.printStackTrace();
			System.exit(1);
		}

	}

//	public static BasicRuleMLPresentationASTGrammar createBasicTreeParser(
//			String filepath, boolean isQuery) throws RecognitionException,
//			IOException {
//		ANTLRInputStream fileInput = new ANTLRInputStream(new FileInputStream(
//				filepath));
//		RuleMLPresentationSyntaxLexer lexer = new RuleMLPresentationSyntaxLexer(
//				fileInput);
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		RuleMLPresentationSyntaxParser parser = new RuleMLPresentationSyntaxParser(
//				tokens);
//		ParserRuleReturnScope r;
//
//		if (isQuery)
//			r = parser.queries();
//		else
//			r = parser.top_level_item();
//
//		CommonTree t = (CommonTree) r.getTree(); // get tree from parser
//		// Create a tree node stream from resulting tree
//		CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
//		return new BasicRuleMLPresentationASTGrammar(nodes);
//	}

	private static void printUsage() {

		System.out
				.println("Usage: org.ruleml.Validator [OPTIONS] <rule base file>+");
		System.out.println("Options:");
		System.out.println("\t--help -? \n\t\t Print this message.");
		System.out
				.println("\t--import_closure -i \n\t\tProcess the whole import closures of the rule bases.");
		System.out.println("\t--query -q <query document file>\n\t\tQuery document for rulebases.");
	} // printUsage()

} // class Validator
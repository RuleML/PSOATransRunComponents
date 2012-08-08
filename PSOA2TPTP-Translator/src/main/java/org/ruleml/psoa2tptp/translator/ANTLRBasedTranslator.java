package org.ruleml.psoa2tptp.translator;

import java.io.OutputStream;
import java.io.PrintStream;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.api.presentation_syntax_parser.*;

public abstract class ANTLRBasedTranslator extends Translator {
	abstract protected TranslatorWalker createTranslatorWalker(CommonTreeNodeStream astNodes);
	
//	@Override
//	public String translateKB(String kb) throws TranslatorException {
//		return translateKB(new ANTLRStringStream(kb));
//	}
	
	@Override
	public void translateKB(String kb, OutputStream out) throws TranslatorException {
		translateKB(new ANTLRStringStream(kb), out);
	}

	public void translateKB(CharStream kb, OutputStream out) throws TranslatorException {
		translate(kb, false, out);
	}
	
//	public String translateKB(CharStream kb) throws TranslatorException {
//		try {
//			return translateKBAST(getAST(kb, false));
//		} catch (RecognitionException e) {
//			throw new TranslatorException(e);
//		}
//	}

//	@Override
//	public String translateQuery(String query) throws TranslatorException {
//		return translateQuery(new ANTLRStringStream(query));
//	}
	
	@Override
	public void translateQuery(String query, OutputStream out) throws TranslatorException {
		translateQuery(new ANTLRStringStream(query), out);
	}
	
	public void translateQuery(CharStream query, OutputStream out) throws TranslatorException {
		translate(query, true, out);
	}
	
//	public String translateQuery(CharStream query) throws TranslatorException {
//		try {
//			return translateQueryAST(getAST(query, true));
//		} catch (RecognitionException e) {
//			throw new TranslatorException(e);
//		}
//	}
	
	public void translate(CharStream input, boolean isQuery, OutputStream out) throws TranslatorException
	{
		try {
			CommonTreeNodeStream astStream = new CommonTreeNodeStream(parse(input, isQuery));
			TranslatorWalker walker = createTranslatorWalker(astStream);
			walker.setOutputStream(getPrintStream(out));
			if (isQuery)
				walker.parseQuery();
			else
				walker.parseDocument();
		} catch (RecognitionException e) {
			throw new TranslatorException(e);
		}
	}
	
	public static CommonTree parse(CharStream input, boolean isQuery) throws RecognitionException
	{
		PSOARuleMLPSLexer lexer = new PSOARuleMLPSLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PSOARuleMLPSParser parser = new PSOARuleMLPSParser(tokens);
		PSOAObjectifier objectifier;
		Object tree;
		
		if (isQuery)
		{
			tree = parser.query().getTree();
			objectifier = new PSOAObjectifier(new CommonTreeNodeStream((CommonTree) tree));
			tree = objectifier.query().getTree();
		}
		else {
			tree = parser.top_level_item().getTree();
			objectifier = new PSOAObjectifier(new CommonTreeNodeStream((CommonTree) tree));
			tree = objectifier.document().getTree();
		}
		
		return (CommonTree) tree;
	}
	
	public static abstract class TranslatorWalker extends TreeParser {
		protected PrintStream m_outStream = System.out;
		
		public TranslatorWalker(TreeNodeStream input,
				RecognizerSharedState state) {
			super(input, state);
		}
		
		public void setOutputStream(PrintStream out)
		{
			m_outStream = getPrintStream(out);
		}
		
		abstract public void parseDocument() throws RecognitionException;
		abstract public void parseQuery() throws RecognitionException;
	}
}

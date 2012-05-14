package org.ruleml.psoa2tptp.translator;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.api.presentation_syntax_parser.*;
import java.io.*;

public class Translator {
	public static void convertDocument(String doc, OutputStream out) throws RecognitionException, IOException {
		DirectTranslator treeParser = documentTreeParser(doc);
		
		treeParser.setOutStream(out);
		treeParser.document();
	}
	
	public static void convertQuery(String doc, OutputStream out) throws RecognitionException, IOException {
		DirectTranslator treeParser = queryTreeParser(doc);
		
		treeParser.setOutStream(out);
		treeParser.queries();
	}
	
	protected static DirectTranslator documentTreeParser(String doc) throws IOException, RecognitionException {
		
		ANTLRInputStream in = in(byteStream(bytes(doc)));
		CommonTokenStream tokens = tokens(lexer(in));
		return new DirectTranslator(documentTreeNodes(tokens));
	}

	protected static DirectTranslator queryTreeParser(String query) throws RecognitionException, IOException {
		ANTLRInputStream in = in(byteStream(bytes(query)));
		CommonTokenStream tokens = tokens(lexer(in));
		return new DirectTranslator(queryTreeNodes(tokens));
	}
	
	protected static InputStream byteStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
	
	protected static byte[] bytes(String s) {
		return s.getBytes();
	}
	
	protected static ANTLRInputStream in(InputStream i) throws IOException {
		return new ANTLRInputStream(i);
	}
	
	protected static PSOARuleMLPSLexer lexer(ANTLRInputStream in) {
		return new PSOARuleMLPSLexer(in);
	}
	
	protected static CommonTokenStream tokens(Lexer lexer) {
		return new CommonTokenStream(lexer);
	}
	
	@SuppressWarnings("unchecked")
	protected static CommonTreeNodeStream documentTreeNodes(CommonTokenStream tokens) throws RecognitionException {
		ParserRuleReturnScope r = top(parser(tokens));
		return new CommonTreeNodeStream(r.getTree());
	}
	
	@SuppressWarnings("unchecked")
	protected static CommonTreeNodeStream queryTreeNodes(CommonTokenStream tokens) throws RecognitionException {
		ParserRuleReturnScope r = queries(parser(tokens));
		return new CommonTreeNodeStream(r.getTree());
	}
	
	protected static DirectTranslator treeParser(CommonTreeNodeStream nodes) {
		return new DirectTranslator(nodes);
	}
	
	protected static PSOARuleMLPSParser parser(CommonTokenStream tokens) {
		return new PSOARuleMLPSParser(tokens);
	}
	
	protected static ParserRuleReturnScope queries(PSOARuleMLPSParser parser) throws RecognitionException {
		return parser.queries();
	}
	
	protected static ParserRuleReturnScope top(PSOARuleMLPSParser parser) throws RecognitionException {
		return parser.top_level_item();
	}
}
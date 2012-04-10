package psoa.to.tptp.restful.resources;

import static java.util.Arrays.asList;
import static psoa.to.tptp.restful.resources.Collections.first;
import static psoa.to.tptp.restful.resources.Collections.map;
import static psoa.to.tptp.restful.resources.ShellUtil.echo;
import static psoa.to.tptp.restful.resources.ShellUtil.padl;
import static psoa.to.tptp.restful.resources.ShellUtil.parenthesize;
import static psoa.to.tptp.restful.resources.ShellUtil.quote;
import static psoa.to.tptp.restful.resources.ShellUtil.rredirect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.apache.log4j.Logger;
import org.ruleml.api.presentation_syntax_parser.BasicRuleMLPresentationASTGrammar;
import org.ruleml.api.presentation_syntax_parser.RuleMLPresentationSyntaxLexer;
import org.ruleml.api.presentation_syntax_parser.RuleMLPresentationSyntaxParser;

public enum Util {
;

	public static List<String> deserialize(String output) {
		return asList(output.split("\n"));
	}

	public static String serialize(List<String> sentences) {
		String str = "";
		for (String s : sentences) {
			str += s + "\n";
		}
		return str;
	}
	
	public static File tmpFile() {
		try {
			return File.createTempFile("tptp-", ".dat", file("/tmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File file(String path) {
		return new File(path);
	}
	
	public static PrintWriter writer(File f) {
		try {
			return new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String redirectTPTP(String tptp) {
		return padl(rredirect(parenthesize(echo(quote(tptp)))));
	}
	
	protected static String decode(String s){
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(Util.class).error("Failed to UTF-8 encode string.", e);
			return s;
		}
	}
	
	protected static Map<String,String> params(UriInfo info) {
		Map<String,String> params = map();
		for (Entry<String, String> e : flatten(rawParams(info))) {
			params.put(e.getKey().toUpperCase(), e.getValue());
		}
		return params;
	}
	
	private static MultivaluedMap<String,String> rawParams(UriInfo info) {
		return info.getQueryParameters();
	}
	
	private static Set<Entry<String,String>> flatten(MultivaluedMap<String, String> multi) {
		Map<String,String> m = map();
		for (Entry<String,List<String>> e : multi.entrySet()) {
			m.put(e.getKey(),first(e.getValue()));
		}
		return m.entrySet();
	}
	
	protected static String query(UriInfo info){
		return "";
	}
	
	
	protected static void convertDocument(String doc, OutputStream out) throws RecognitionException, IOException {
		BasicRuleMLPresentationASTGrammar treeParser = documentTreeParser(doc);
		
		treeParser.setOutStream(out);
		treeParser.document();
	}
	
	protected static void convertQuery(String doc, OutputStream out) throws RecognitionException, IOException {
		BasicRuleMLPresentationASTGrammar treeParser = queryTreeParser(doc);
		
		treeParser.setOutStream(out);
		treeParser.queries();
	}
	
	protected static BasicRuleMLPresentationASTGrammar documentTreeParser(String doc) throws IOException, RecognitionException {
		
		ANTLRInputStream in = in(byteStream(bytes(doc)));
		CommonTokenStream tokens = tokens(lexer(in));
		return new BasicRuleMLPresentationASTGrammar(documentTreeNodes(tokens));
	}

	protected static BasicRuleMLPresentationASTGrammar queryTreeParser(String query) throws RecognitionException, IOException {
		ANTLRInputStream in = in(byteStream(bytes(query)));
		CommonTokenStream tokens = tokens(lexer(in));
		return new BasicRuleMLPresentationASTGrammar(queryTreeNodes(tokens));
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
	
	protected static RuleMLPresentationSyntaxLexer lexer(ANTLRInputStream in) {
		return new RuleMLPresentationSyntaxLexer(in);
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
	
	protected static BasicRuleMLPresentationASTGrammar treeParser(CommonTreeNodeStream nodes) {
		return new BasicRuleMLPresentationASTGrammar(nodes);
	}
	
	protected static RuleMLPresentationSyntaxParser parser(CommonTokenStream tokens) {
		return new RuleMLPresentationSyntaxParser(tokens);
	}
	
	protected static ParserRuleReturnScope queries(RuleMLPresentationSyntaxParser parser) throws RecognitionException {
		return parser.queries();
	}
	
	protected static ParserRuleReturnScope top(RuleMLPresentationSyntaxParser parser) throws RecognitionException {
		return parser.top_level_item();
	}
	
	public static OutputStream out() {
		return new ByteArrayOutputStream();
	}
}

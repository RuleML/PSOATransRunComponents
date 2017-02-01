package org.ruleml.psoa.psoa2x.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.parser.*;
import org.ruleml.psoa.FreshNameGenerator;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.PSOAKB;
import org.ruleml.psoa.PSOAQuery;
import org.ruleml.psoa.analyzer.KBInfoCollector;
import org.ruleml.psoa.transformer.*;

public abstract class ANTLRBasedTranslator extends Translator {
	abstract protected TranslatorWalker createTranslatorWalker(TreeNodeStream astNodes);
	abstract protected <T extends PSOAInput<T>> T normalize(T input);
	protected PSOAKB m_kb;
	
	@Override
	public void translateKB(String kb, OutputStream out) throws TranslatorException {
		m_kb = new PSOAKB();
		m_kb.loadFromText(kb);
		translateKB(m_kb, out);
	}
	
	@Override
	public void translateKB(InputStream kb, OutputStream out) throws TranslatorException {
		try {
			FreshNameGenerator.reset();
			m_kb = new PSOAKB();
			m_kb.load(kb);
			translateKB(m_kb, out);
		} catch (IOException e) {
			throw new TranslatorException(e);
		}
	}

	public void translateKB(PSOAKB kb, OutputStream out) throws TranslatorException {
		try {
			kb.loadImports();
			kb.setPrintAfterTransformation(debugMode);
			TreeNodeStream stream = normalize(kb).getTreeNodeStream();
			TranslatorWalker walker = createTranslatorWalker(stream);
			walker.setOutputStream(getPrintStream(out));
			walker.document();
		} catch (RecognitionException e) {
			throw new TranslatorException(e);
		} catch (IOException e) {
			throw new TranslatorException(e);
		}
	}
	
	@Override
	public void translateQuery(String query, OutputStream out) throws TranslatorException {
		PSOAQuery psoaquery = new PSOAQuery(m_kb);
		psoaquery.loadFromText(query);
		translateQuery(psoaquery, out);
	}
	
	@Override
	public void translateQuery(InputStream query, OutputStream out) throws TranslatorException {
		try {
			PSOAQuery psoaquery = new PSOAQuery(m_kb);
			psoaquery.load(query);
			translateQuery(psoaquery, out);
		} catch (IOException e) {
			throw new TranslatorException(e);
		}
	}
	
	public void translateQuery(PSOAQuery query, OutputStream out) {
		try {
			query.setPrintAfterTransformation(debugMode);
			TreeNodeStream stream = normalize(query).getTreeNodeStream();
			TranslatorWalker walker = createTranslatorWalker(stream);
			walker.setOutputStream(getPrintStream(out));
			_queryVarMap = walker.query();
		} catch (RecognitionException e) {
			throw new TranslatorException(e);
		}
	}
	
	/*
	public void translateQuery(CharStream query, OutputStream out) throws TranslatorException {
		translate(query, true, out);
	}
	
	public void translate(CharStream input, boolean isQuery, OutputStream out) throws TranslatorException
	{
		try {
			CommonTreeNodeStream parseTreeStream = parse(input, isQuery);
			debugPrintTree(parseTreeStream);
			CommonTreeNodeStream astStream = preprocess(parseTreeStream, isQuery);
			TranslatorWalker walker = createTranslatorWalker(astStream);
			walker.setOutputStream(getPrintStream(out));
			if (isQuery)
				_queryVarMap = walker.query();
			else
				walker.document();
		} catch (RecognitionException e) {
			throw new TranslatorException(e);
		}
	}
	
	public static CommonTreeNodeStream parse(CharStream input, boolean isQuery) throws RecognitionException
	{
		PSOAPSLexer lexer = new PSOAPSLexer(input);
		TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		PSOAPSParser parser = new PSOAPSParser(tokens);
		Object tree;
		
		if (isQuery)
		{
			tree = parser.query().getTree();
		}
		else {
			tree = parser.top_level_item().getTree();
		}
		
		CommonTreeNodeStream stream = new CommonTreeNodeStream((CommonTree) tree);
		stream.setTokenStream(tokens);
		return stream;
	}
	
	protected CommonTreeNodeStream preprocess(CommonTreeNodeStream treeNodeStream, boolean isQuery) throws RecognitionException
	{
		ExistObjectifier objectifier;
		CommonTreeNodeStream newTreeNodeStream;
		
		if (isQuery)
		{
			objectifier = new ExistObjectifier(treeNodeStream);
			newTreeNodeStream = new CommonTreeNodeStream(objectifier.query().getTree());
			newTreeNodeStream.setTokenStream(treeNodeStream.getTokenStream());
		}
		else {
			objectifier = new ExistObjectifier(treeNodeStream);
			newTreeNodeStream = new CommonTreeNodeStream(objectifier.document().getTree());
			newTreeNodeStream.setTokenStream(treeNodeStream.getTokenStream());
		}
		
		return newTreeNodeStream;
	}
	
	protected static void debugPrintTree(CommonTreeNodeStream stream)
	{
		if (debugMode)
			System.out.println(((CommonTree)stream.getTreeSource()).toStringTree());
	}
	*/
	
	public static abstract class TranslatorWalker extends TreeParser {
		protected PrintStream _outStream = System.out;
		protected Map<String, String> _queryVarMap = new HashMap<String, String>();
		
		public TranslatorWalker(TreeNodeStream input,
				RecognizerSharedState state) {
			super(input, state);
		}
		
		public void setOutputStream(PrintStream out)
		{
			_outStream = getPrintStream(out);
		}
		
		protected void print(Object obj) {
	        _outStream.print(obj);
	    }
		
		protected void print(String str) {
	        _outStream.print(str);
	    }
		
		protected void print(Object... objs) {
			for (Object obj : objs)
			{
		        _outStream.print(obj);
			}
	    }
		
		protected void print(String... strs) {
			for (String s : strs)
			{
		        _outStream.print(s);
			}
	    }
		
	    protected void println(Object obj) {
	        _outStream.println(obj);
	    }
		
		protected void println(Object... objs) {
			for (Object obj : objs)
			{
		        _outStream.print(obj);
			}
			println();
	    }
		
		protected void println(String... strs) {
			for (String s : strs)
			{
		        _outStream.print(s);
			}
			println();
	    }
	    
	    protected void println() {
	        _outStream.println();
	    }
		
		abstract public void document() throws RecognitionException;
		abstract public Map<String, String> query() throws RecognitionException;
	}
}

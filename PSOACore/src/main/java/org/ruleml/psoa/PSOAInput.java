package org.ruleml.psoa;

import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.parser.*;
import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.utils.ANTLRTreeStreamConsumer;

/**
 * Abstract Java class for operating PSOA inputs, superclass 
 * of PSOAKB and PSOAQuery.
 * 
 * */

public abstract class PSOAInput<T extends PSOAInput<T>> {
	protected TreeAdaptor m_adaptor = new CommonTreeAdaptor();
	protected TokenStream m_tokens;
	protected Tree m_tree;
	protected boolean m_printAfterTransformation = false;
	protected PrintStream m_printStream = System.out;


	/**
	 * Parse a string into an ANTLR tree representing the PSOA input
	 * 
	 * @param input   the PSOA input
	 * 
	 * */
	public void loadFromText(String input) {
		try {
			load(new ANTLRStringStream(input));
		} catch (RecognitionException e) {
			throw new PSOARuntimeException("Failed to parse KB", e);
		}
	}

	/**
	 * Parse a file into an ANTLR tree representing the PSOA input
	 * 
	 * @param input   the PSOA input
	 * 
	 * */
	public void loadFromFile(String file) throws IOException {
		load(new FileInputStream(file));
	}

	/**
	 * Parse an input stream into an ANTLR tree representing the PSOA input
	 * 
	 * @param input   the PSOA input
	 * 
	 * */
	public void load(InputStream input) throws IOException {
		try {
			load(new ANTLRInputStream(input));
		} catch (RecognitionException e) {
			throw new PSOARuntimeException("Failed to parse input", e);
		}
	}


	/**
	 * Parse an ANTLR input stream into an ANTLR tree
	 * 
	 * @param input   the ANTLR input stream
	 * 
	 * */
	private void load(CharStream input) throws RecognitionException {
		PSOAPSLexer lexer = new PSOAPSLexer(input);
		m_tokens = new CommonTokenStream(lexer);
		PSOAPSParser parser = new PSOAPSParser(m_tokens);
		m_tree = (Tree) parse(parser).getTree();
	}

	abstract protected ParserRuleReturnScope parse(PSOAPSParser parser)
			throws RecognitionException;

	protected Tree getTree() {
		return m_tree;
	}

	public TreeNodeStream getTreeNodeStream() {
		CommonTreeNodeStream stream = new CommonTreeNodeStream(m_tree);
		stream.setTokenStream(m_tokens);
		return stream;
	}

	public void setPrintAfterTransformation(boolean b) {
		m_printAfterTransformation = b;
	}

	public void setStream(OutputStream out) {
		m_printStream = new PrintStream(out);
	}

	public T transform(String name, ANTLRTreeStreamConsumer actor) {
		return transform(name, actor, false);
	}

	abstract public T transform(String name, ANTLRTreeStreamConsumer actor,
			boolean newKBInst);

	/**
	 * Perform FOL-targeting normalization of PSOA input
	 * 
	 * @param config   transformer configuration
	 * 
	 * @return   the FOL-normalized PSOA input
	 * 
	 * */
	abstract public T FOLnormalize(RelationalTransformerConfig config);

	/**
	 * Perform LP-targeting normalization of PSOA input
	 * 
	 * @param config   transformer configuration
	 * 
	 * @return   the LP-normalized PSOA input
	 * 
	 * */
	abstract public T LPnormalize(RelationalTransformerConfig config);

	public void printTree() {
		m_printStream.println(m_tree.toStringTree());
	}
}

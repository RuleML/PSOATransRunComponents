package org.ruleml.psoa;

import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.parser.*;
import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.util.ANTLRTreeStreamConsumer;

public abstract class PSOAInput<T extends PSOAInput<T>> {
	protected TreeAdaptor m_adaptor = new CommonTreeAdaptor();
	protected TokenStream m_tokens;
	protected Tree m_tree;
	protected boolean m_printAfterTransformation = false;
	protected PrintStream m_printStream = System.out;

	public void loadFromText(String input) {
		try {
			load(new ANTLRStringStream(input));
		} catch (RecognitionException e) {
			throw new PSOARuntimeException("Failed to parse KB", e);
		}
	}

	public void loadFromFile(String file) throws IOException {
		load(new FileInputStream(file));
	}

	public void load(InputStream input) throws IOException {
		try {
			load(new ANTLRInputStream(input));
		} catch (RecognitionException e) {
			throw new PSOARuntimeException("Failed to parse input", e);
		}
	}

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

	abstract public T FOLnormalize(RelationalTransformerConfig config);

	abstract public T LPnormalize(RelationalTransformerConfig config);

	public void printTree() {
		m_printStream.println(m_tree.toStringTree());
	}
}

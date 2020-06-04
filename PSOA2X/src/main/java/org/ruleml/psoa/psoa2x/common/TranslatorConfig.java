package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.parser.ParserConfig;
import org.ruleml.psoa.transformer.TransformerConfig;

public abstract class TranslatorConfig {
	private ParserConfig m_parserConfig = new ParserConfig();
	private TransformerConfig m_transConfig;
	
	public ParserConfig getParserConfig() {
		return m_parserConfig;
	}
	
	public void setParserConfig(ParserConfig parserConfig) {
		m_parserConfig = parserConfig;
	}
	
	public TransformerConfig getTransformerConfig() {
		return m_transConfig;
	}
	
	public void setTransformerConfig(TransformerConfig transConfig) {
		m_transConfig = transConfig;
	}
	
	public void setDifferentiateObj(boolean differentiateObj) {
		m_transConfig.differentiateObj = differentiateObj;
	}
	
	public void setOmitMemtermInNegativeAtoms(boolean omitMemtermInNegativeAtoms) {
		m_transConfig.omitMemtermInNegativeAtoms = omitMemtermInNegativeAtoms;
	}
	
	public void setForallWrap(boolean forallWrap) {
	    m_transConfig.forallWrap = forallWrap;
	}
	
	public void setReconstruct(boolean reconstruct) {
		m_parserConfig.reconstruct = reconstruct;
	}
}

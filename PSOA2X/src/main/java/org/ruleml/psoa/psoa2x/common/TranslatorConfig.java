package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.transformer.TransformerConfig;

public abstract class TranslatorConfig {
	private TransformerConfig m_transConfig;
	
	public TranslatorConfig() {
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
}

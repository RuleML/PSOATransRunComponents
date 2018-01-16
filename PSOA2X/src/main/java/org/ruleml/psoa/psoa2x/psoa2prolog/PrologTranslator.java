/*
 * Prolog translator.
 * */

package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.psoa2x.common.*;

public class PrologTranslator extends ANTLRBasedTranslator {
	Config m_config;
	
	public static class Config extends RelationalTranslatorConfig { }
	
	public PrologTranslator()
	{
		this(new Config());
	}
	
	public PrologTranslator(Config config)
	{
		m_config = config;
		m_parserConfig = config.getParserConfig();
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.LPnormalize(m_config.getRelationalTransformerConfig());
	}
	
	@Override
	protected Converter createConverter(TreeNodeStream astNodes) {
		return new PrologConverter(astNodes, m_config);
	}
}

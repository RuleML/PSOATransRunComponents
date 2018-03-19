package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.parser.ParserConfig;
import org.ruleml.psoa.psoa2x.common.*;

public class TPTPTranslator extends ANTLRBasedTranslator {
	private Config m_config;
	
	public static class Config extends RelationalTranslatorConfig {}
	
	public TPTPTranslator()
	{
		this(new Config());
	}
	
	public TPTPTranslator(Config config)
	{
		m_config = config;
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.FOLnormalize(m_config.getRelationalTransformerConfig());
	}
	
	@Override
	protected Converter createConverter(TreeNodeStream astNodes) {
		return new TPTPConverter(astNodes);
	}

	@Override
	protected ParserConfig getParserConfig() {
		return m_config.getParserConfig();
	}
}

package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.psoa2x.common.*;

public class ASOTPTPTranslator extends ANTLRBasedTranslator {
	private PSOA2TPTPConfig m_config;
	
	public ASOTPTPTranslator()
	{
		this(new PSOA2TPTPConfig());
	}
	
	public ASOTPTPTranslator(PSOA2TPTPConfig config)
	{
		m_config = config;
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.FOLnormalize(m_config);
	}
	
	@Override
	protected Converter createTranslatorWalker(TreeNodeStream astNodes) {
		return new ASOTPTPConverter(astNodes, m_config);
	}
}

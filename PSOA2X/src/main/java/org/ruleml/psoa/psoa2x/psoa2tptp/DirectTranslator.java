package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.psoa2x.common.*;

public class DirectTranslator extends ANTLRBasedTranslator {
	private PSOA2TPTPConfig m_config;
	
	public DirectTranslator(PSOA2TPTPConfig config)
	{
		m_config = config;
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.FOLnormalize(m_config);
	}
	
	@Override
	protected TranslatorWalker createTranslatorWalker(TreeNodeStream astNodes) {
		return new DirectTranslatorWalker(astNodes);
	}

	@Override
	public String inverseTranslateTerm(String term) {
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

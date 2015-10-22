package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.psoa.psoa2x.common.*;

public class TPTPASOTranslator extends ANTLRBasedTranslator {
	private PSOA2TPTPConfig m_config;
	
	public TPTPASOTranslator(PSOA2TPTPConfig config)
	{
		m_config = config;
	}
	
	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new TPTPASOTranslatorWalker(astNodes, m_config);
	}

	@Override
	public String inverseTranslateTerm(String term) {
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

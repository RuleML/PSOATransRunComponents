/*
 * Prolog translator.
 * */

package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.psoa2x.common.*;

public class PrologTranslator extends ANTLRBasedTranslator {
	PSOA2PrologConfig m_config;
	
	public PrologTranslator()
	{
		this(new PSOA2PrologConfig());
	}
	
	public PrologTranslator(PSOA2PrologConfig config)
	{
		m_config = config;
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.LPnormalize(m_config);
	}
	
	@Override
	protected Converter createTranslatorWalker(TreeNodeStream astNodes) {
		return new PrologConverter(astNodes, m_config);
	}
}

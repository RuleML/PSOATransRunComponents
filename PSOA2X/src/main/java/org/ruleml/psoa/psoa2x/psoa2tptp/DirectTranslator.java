package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.psoa.psoa2x.common.*;

public class DirectTranslator extends ANTLRBasedTranslator {
	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new DirectTranslatorWalker(astNodes);
	}

	@Override
	public String inverseTranslateTerm(String term) {
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

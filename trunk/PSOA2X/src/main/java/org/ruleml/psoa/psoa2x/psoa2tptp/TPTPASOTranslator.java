package org.ruleml.psoa.psoa2x.psoa2tptp;

import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.psoa.psoa2x.common.*;

public class TPTPASOTranslator extends ANTLRBasedTranslator {

	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new TPTPASOTranslatorWalker(astNodes);
	}

	@Override
	public String inverseTranslateTerm(String term) {
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

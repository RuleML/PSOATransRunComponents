package org.ruleml.psoa2tptp.translator;

import org.antlr.runtime.tree.CommonTreeNodeStream;

public class TPTPASOTranslator extends ANTLRBasedTranslator {

	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new TPTPASOTranslatorWalker(astNodes);
	}

}

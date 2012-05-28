package org.ruleml.psoa2tptp.translator;

import org.antlr.runtime.tree.CommonTreeNodeStream;

public class DirectTranslator extends ANTLRBasedTranslator {
	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new DirectTranslatorWalker(astNodes);
	}
}

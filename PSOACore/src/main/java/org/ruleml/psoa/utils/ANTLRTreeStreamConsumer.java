package org.ruleml.psoa.utils;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.*;

@FunctionalInterface
public interface ANTLRTreeStreamConsumer {
	TreeRuleReturnScope apply(TreeNodeStream stream) throws RecognitionException;
}

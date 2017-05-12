package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.ruleml.psoa.transformer.RelationalTransformerConfig;

public class PSOA2PrologConfig extends RelationalTransformerConfig
{
	public PSOA2PrologConfig()
	{
		omitMemtermInNegtiveAtoms = false;
		differentiateObj = true;
		dynamicObj = true;
	}
}

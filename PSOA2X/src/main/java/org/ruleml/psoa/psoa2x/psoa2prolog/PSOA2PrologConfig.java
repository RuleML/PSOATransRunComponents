package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.ruleml.psoa.psoa2x.common.RelationalTranslatorConfig;

public class PSOA2PrologConfig extends RelationalTranslatorConfig
{
	public PSOA2PrologConfig()
	{
		reproduceClass = false;
		crossOverAxiom = false;
		dynamicObjectification = true;
	}
}

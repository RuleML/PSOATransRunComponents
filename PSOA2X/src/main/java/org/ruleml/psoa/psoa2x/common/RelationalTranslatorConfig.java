package org.ruleml.psoa.psoa2x.common;

import org.ruleml.psoa.transformer.RelationalTransformerConfig;

public abstract class RelationalTranslatorConfig extends TranslatorConfig {
	public RelationalTranslatorConfig() {
		setTransformerConfig(new RelationalTransformerConfig());
	}
	
	public RelationalTransformerConfig getRelationalTransformerConfig() {
		return (RelationalTransformerConfig) getTransformerConfig();
	}
	
	public void setDynamicObj(boolean dynamicObj) {
		getRelationalTransformerConfig().dynamicObj = dynamicObj;
	}
}
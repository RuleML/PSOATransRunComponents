package org.ruleml.psoa.x2psoa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface X2PSOA
{
	public void translate(InputStream input, OutputStream output) throws IOException;
}

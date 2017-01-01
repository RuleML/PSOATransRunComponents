package org.ruleml.psoa.x2psoa;

import java.io.PrintStream;

public class IOUtil
{
	public static void print(PrintStream stream, Object... objs)
	{
		for (Object obj : objs) {
			stream.print(obj);
		}
	}
	
	public static void println(PrintStream stream, Object... objs)
	{
		for (Object obj : objs) {
			stream.print(obj);
		}
		stream.println("");
	}
}

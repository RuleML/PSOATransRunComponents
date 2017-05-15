package org.ruleml.psoa.utils;

import java.io.OutputStream;
import java.io.PrintStream;

public class IOUtil {
	public static void print(PrintStream output, Object... objs)
	{
		for (Object obj : objs) {
			output.print(obj);
		}
	}
	
	public static void print(Object... objs)
	{
		print(System.out, objs);
	}
	
	public static void printErr(Object... objs)
	{
		print(System.err, objs);
	}
	
	public static void println(PrintStream output, Object... objs)
	{
		print(output, objs);
		output.println();
	}
	
	public static void println(Object... objs)
	{
		println(System.out, objs);
	}
	
	public static void printErrln(Object... objs)
	{
		println(System.err, objs);
	}
	
	public static PrintStream getPrintStream(OutputStream out) {
		if (out instanceof PrintStream)
			return (PrintStream)out;
		
		return new PrintStream(out);
	}
}

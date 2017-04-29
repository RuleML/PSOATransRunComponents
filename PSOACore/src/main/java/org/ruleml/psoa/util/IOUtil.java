package org.ruleml.psoa.util;

import java.io.PrintStream;

public class IOUtil {	
	public static void print(PrintStream output, Object... objs)
	{
		for (Object obj : objs) {
			System.out.print(obj);
		}
	}
	
	public static void print(Object... objs)
	{
		print(System.out, objs);
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
	
	public static void printErr(Object... objs)
	{
		print(System.err, objs);
	}
	
	public static void printErrln(Object... objs)
	{
		println(System.err, objs);
	}
}

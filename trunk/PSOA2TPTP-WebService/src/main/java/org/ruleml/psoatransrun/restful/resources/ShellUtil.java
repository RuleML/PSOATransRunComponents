package org.ruleml.psoa2tptp.restful.resources;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.*;

public class ShellUtil
{
	public static DefaultExecuteResultHandler resultHandler() {
		return new DefaultExecuteResultHandler();
	}
	
	private static Executor executor(long timeout) {
		Executor e = new DefaultExecutor();
		e.setWatchdog(new ExecuteWatchdog(timeout));
		return e;
	}
	
	public static int execute(CommandLine cl, OutputStream out)
	{
		return execute(cl, out, ExecuteWatchdog.INFINITE_TIMEOUT);
	}
	
	public static int execute(CommandLine cl, OutputStream out, long timeout)
	{
		Executor exec = executor(timeout);
		PumpStreamHandler psh = new PumpStreamHandler(out, out);
		exec.setStreamHandler(psh);
		
		try
		{
			return exec.execute(cl);
		}
		catch (ExecuteException e)
		{
			throw new PSOATransRunException("Execution Error", e); 
		} catch (IOException e)
		{
			throw IOUtil.runtimeIOException(e);
		}
	}

	public static String concat(String... strs)
	{
		switch (strs.length)
		{
			case 0:
				return "";
			case 1:
				return strs[0];
			case 2:
				return strs[0].concat(strs[1]);
			default:
				StringBuilder sb = new StringBuilder();
				for (String s : strs)
				{
					sb.append(s);
				}
				return sb.toString();
		}
	}
	
	public static String echo(String s) {
		return "echo ".concat(s);
	}
	
	public static String padl(String s) {
		return " ".concat(s);
	}
	
	public static String quote(String s) {
		return concat("\"", s, "\"");
	}
	
	public static String parenthesize(String s) {
		return concat("(", s, ")");
	}

	public static String rredirect(String s) {
		return "<".concat(s);
	}
	
	public static String subshell(String s) {
		return concat("(", s, ")");
	}
}

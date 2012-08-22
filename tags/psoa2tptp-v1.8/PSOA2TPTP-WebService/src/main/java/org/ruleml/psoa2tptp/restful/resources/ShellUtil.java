package org.ruleml.psoa2tptp.restful.resources;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.*;

public enum ShellUtil {
	;
	public static final String VKERNEL;
	public static final String VKERNELWRAPPER = "vkernelwrapper";
	public static final String ANSWER_CONFIG_PATH;
	public static final String ECHO = "echo";	
	public static final String RREDIRECT = "<";
	public static final String LPAREN = "(";
	public static final String RPAREN = ")";
	
	static {
		String dirPath = System.getProperty("psoa2tptpBinDir"),
			   vpbin = "vkernel", ansConfig = "answer_predicates.xml";
		
		if (dirPath == null)
		{
			dirPath = ".";
		}
		
		File dir = new File(dirPath);
		VKERNEL = (new File(dir, vpbin)).getPath();
		ANSWER_CONFIG_PATH = (new File(dir, ansConfig)).getPath();
	}
	
	public static String rredirect(String s) {
		return RREDIRECT.concat(s);
	}
	
	public static String subshell(String s) {
		return wrap(s,"(",")");
	}
	
	public static String wrap(String s, String head, String tail) {
		return head.concat(s).concat(tail);
	}
	
	public static DefaultExecuteResultHandler resultHandler() {
		return new DefaultExecuteResultHandler();
	}
	
	public static ExecuteWatchdog watchdog() {
		return new ExecuteWatchdog(60*1000);
	}
	
	public static Executor executor(ExecuteWatchdog dog) {
		Executor e = new DefaultExecutor();
		e.setWatchdog(dog);
		return e;
	}
	
	public static int execute(CommandLine cl, OutputStream out) throws ExecuteException, IOException {
		Executor e = executor(watchdog());
		PumpStreamHandler psh = new PumpStreamHandler(out, out);
		e.setStreamHandler(psh);
		return e.execute(cl);
	}
	
	public static CommandLine cl(String s) {
		return new CommandLine(s);
	}

	public static String echo(String s) {
		return ECHO.concat(padl(s));
	}
	
	public static String padl(String s) {
		return " ".concat(s);
	}
	
	public static String quote(String s) {
		return "\"".concat(s).concat("\"");
	}
	
	public static String parenthesize(String s) {
		return LPAREN.concat(s).concat(RPAREN);
	}
}

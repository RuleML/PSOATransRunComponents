package org.ruleml.psoa.x2psoa;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.*;

public class X2PSOAMain
{
	public static void main(String[] args) throws IOException {		
		LongOpt[] opts = new LongOpt[]
		{
			new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?'),
			new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null,'i'),
//			new LongOpt("printTrans", LongOpt.NO_ARGUMENT, null,'p'),
			new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null,'o')
		};

		Getopt optionsParser = new Getopt("", args, "?i:o:", opts);
		FileInputStream kbStream = null;
		PrintStream outputStream = System.out;
		X2PSOA translator = null;
		String arg;
		
		for (int opt = optionsParser.getopt(); opt != -1; opt = optionsParser.getopt())
		{
			switch (opt) {
				case '?':
					printUsage();
					return;
	
				case 'i':
					arg = optionsParser.getOptarg();
					String suffix = arg.substring(arg.lastIndexOf('.') + 1);
					if (suffix.equals("n3") || suffix.equals("nt") || suffix.equals("ttl"))
						translator = new RDF2PSOA();
					else
						printErrlnAndExit("Unsupported input format");
					
					try
					{
						kbStream = new FileInputStream(arg);
					}
					catch (FileNotFoundException e)
					{
						printErrlnAndExit("Cannot find KB file " + arg);
					}
					catch (SecurityException e)
					{
						printErrlnAndExit("Unable to read KB file " + arg);
					}
					break;
				case 'o':
					arg = optionsParser.getOptarg();
					if (!arg.endsWith(".psoa"))
						printErrlnAndExit("Translation output file name must end with .psoa");
					outputStream = new PrintStream(arg);
					break;
				default:
					System.out.println("Unsupported option: " + opt);
					printUsage();
					System.exit(1);
			}
		}
		
		translator.translate(kbStream, outputStream);
	}
	
	private static void printUsage()
	{
		System.out.println("Usage: java -jar X2PSOA.jar -i <input> -o <output>");
		System.out.println("  -i,--input        Input Knowledge Base (KB)");
		System.out.println("  -o,--output       Output PSOA KB");
	}
	
	public static void printErrlnAndExit(String s)
	{
		System.err.println(s);
		System.exit(1);
	}
}

package org.ruleml.psoa.psoatransrun;

import java.io.*;
import java.util.Scanner;

import org.ruleml.psoa.psoa2x.psoa2prolog.PSOA2PrologConfig;
import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;
import org.ruleml.psoa.psoatransrun.prolog.XSBEngine;
import org.ruleml.psoa.psoatransrun.prolog.XSBEngineConfig;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import static org.ruleml.psoa.utils.IOUtil.*;

/**
 * Command line interface of PSOATransRun
 * 
 */
public class PSOATransRunCmdLine {
	// Entrance of PSOATransRun program
	public static void main(String[] args) throws IOException {
		// Parse command line options
		LongOpt[] opts = new LongOpt[] {
				new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?'),
				new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("printTrans", LongOpt.NO_ARGUMENT, null, 'p'),
				new LongOpt("outputTrans", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
				new LongOpt("xsbfolder", LongOpt.REQUIRED_ARGUMENT, null, 'x'),
				new LongOpt("query", LongOpt.REQUIRED_ARGUMENT, null, 'q'),
				new LongOpt("allAns", LongOpt.NO_ARGUMENT, null, 'a'),
				new LongOpt("staticOnly", LongOpt.NO_ARGUMENT, null, 's'),
				new LongOpt("echoInput", LongOpt.NO_ARGUMENT, null, 'e') };

		Getopt optionsParser = new Getopt("", args, "?i:po:x:q:ad:t:rse", opts);
		File kbFile = null;
		FileInputStream kbStream = null, queryStream = null;
		String arg;

		PSOA2PrologConfig translatorConfig = new PSOA2PrologConfig();
		XSBEngineConfig engineConfig = new XSBEngineConfig();
		boolean outputTrans = false, showOrigKB = false, getAllAnswers = false;

		for (int opt = optionsParser.getopt(); opt != -1; opt = optionsParser
				.getopt()) {
			switch (opt) {
			case '?':
				printUsage();
				return;

			case 'i':
				arg = optionsParser.getOptarg();
				try {
					kbFile = new File(arg);
					kbStream = new FileInputStream(kbFile);
				}
				catch (FileNotFoundException e) {
					printErrln("Cannot find KB file ", arg);
					System.exit(1);
				}
				catch (SecurityException e) {
					printErrln("Unable to read KB file ", arg);
					System.exit(1);
				}
				break;
			case 'q':
				arg = optionsParser.getOptarg();
				try {
					queryStream = new FileInputStream(arg);
				}
				catch (FileNotFoundException e) {
					printErrln("Cannot find query file ", arg,
							". Read from console.");
				}
				catch (SecurityException e) {
					printErrln("Unable to read query file ", arg,
							". Read from console.");
				}
				break;
//			case 'r':
//				translatorConfig.reproduceClass = true;
//				break;
			case 'p':
				outputTrans = true;
				break;
			case 'o':
				engineConfig.transKBPath = optionsParser.getOptarg();
				break;
			case 'x':
				engineConfig.xsbFolderPath = optionsParser.getOptarg();
				break;
			case 'a':
				getAllAnswers = true;
				break;
			case 's':
				translatorConfig.dynamicObj = false;
				break;
			case 'e':
				showOrigKB = true;
			default:
				assert false;
			}
		}

		// Initialize PSOATransRun
		XSBEngine engine = null;

		try {
			engine = new XSBEngine(engineConfig);
		}
		catch (PSOATransRunException e) {
			printErrln(e.getMessage());
			System.exit(1);
		}

		PrologTranslator translator = new PrologTranslator(translatorConfig);
		PSOATransRun system = new PSOATransRun(translator, engine);

		system.setPrintTrans(outputTrans);

		// Print initial PSOA KB if requested
		if (showOrigKB) {
			println("Original KB:");
			try (BufferedReader reader = new BufferedReader(
					new FileReader(kbFile))) {
				do {
					String line = reader.readLine();
					if (line != null)
						println(line);
					else
						break;
				} while (true);
			}

			println();
		}

		try {
			// Load KB
			system.loadKB(kbStream);
			println("KB Loaded");

			if (queryStream != null) {
				// Execute query from file input
				QueryResult result = system.executeQuery(queryStream);
				printQueryResult(result, getAllAnswers, null);
			}
			else {
				try (Scanner sc = new Scanner(System.in)) {
					// Console query loop
					do {
						println("Input Query:");
						if (!sc.hasNext())
							break;

						String query = sc.nextLine();
						QueryResult result = system.executeQuery(query);
						printQueryResult(result, getAllAnswers, sc);
						println();
					} while (true);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			system.shutdown();
		}
	}

	private static void printQueryResult(QueryResult result,
			boolean getAllAnswers, Scanner reader) {
		println("Answer(s):");

		if (getAllAnswers) {
			println(result);
		}
		else {
			AnswerIterator iter = result.iterator();
			try {
				if (!iter.hasNext()) {
					println("No");
				}
				else {
					// Output first answer
					Substitution answer = iter.next();
					print(answer, "\t");
	
					// Handle user requests for more answers if the first answer is not "yes"
					if (!answer.isEmpty()) {
						while(reader.hasNext()) {
							String input = reader.nextLine();
							if (input.equals(";")) {
								if (iter.hasNext())
									print(iter.next(), "  \t");
								else {
									print("No");
									break;
								}
							}
							else if (input.isEmpty()) {
								break;
							}
						}
					}
					
					println();
				}
			}
			finally
			{
				iter.dispose();
			}
		}
	}

	private static void printUsage() {
		println("Usage: java -jar PSOATransRun.jar -i <kb> [-e] [-p] [-o <translated KB output>] [-q <query>] [-a] [-s] [-x <xsb folder>]");
		println("Options:");
		println("  -?,--help         Print the help message");
		println("  -a,--allAns       Retrieve all answers for each query at once");
		println("  -i,--input        Input Knowledge Base (KB)");
		println("  -e,--echoInput    Echo input KB to standard output");
		println("  -q,--query        Query document for the KB. If the query document");
		println("                    is not specified, the engine will read queries");
		println("                    from the standard input.");
		println("  -p,--printTrans   Print translated KB and queries to standard output");
		println("  -o,--outputTrans  Save translated KB to the designated file");
		println("  -x,--xsbfolder    Specifies XSB installation folder. The default path is ");
		println("                    obtained from the environment variable XSB_DIR");
		println("  -s,--staticOnly   Apply static objectification only");
	}
}

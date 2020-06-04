package org.ruleml.psoa.psoatransrun;

import java.io.*;
import java.util.Scanner;

import org.ruleml.psoa.psoa2x.common.Translator;
import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;
import org.ruleml.psoa.psoa2x.psoa2tptp.TPTPTranslator;
import org.ruleml.psoa.psoatransrun.engine.ExecutionEngine;
import org.ruleml.psoa.psoatransrun.prolog.SWIEngine;
import org.ruleml.psoa.psoatransrun.prolog.XSBEngine;
import org.ruleml.psoa.psoatransrun.test.TestSuite;
import org.ruleml.psoa.psoatransrun.tptp.VampirePrimeEngine;

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
				new LongOpt("longhelp", LongOpt.NO_ARGUMENT, null, '~'),
				new LongOpt("targetLang", LongOpt.REQUIRED_ARGUMENT, null, 'l'),
				new LongOpt("backend", LongOpt.REQUIRED_ARGUMENT, null, 'b'),
				new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("query", LongOpt.REQUIRED_ARGUMENT, null, 'q'),
				new LongOpt("explicitLocalConstants", LongOpt.NO_ARGUMENT, null, 'c'),
				new LongOpt("test", LongOpt.NO_ARGUMENT, null, 't'),
				new LongOpt("numRuns", LongOpt.REQUIRED_ARGUMENT, null, 'n'),
				new LongOpt("echoInput", LongOpt.NO_ARGUMENT, null, 'e'),
				new LongOpt("printTrans", LongOpt.NO_ARGUMENT, null, 'p'),
				new LongOpt("outputTrans", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
				new LongOpt("prologFolder", LongOpt.REQUIRED_ARGUMENT, null, 'x'),
				new LongOpt("allAns", LongOpt.NO_ARGUMENT, null, 'a'),
				new LongOpt("timeout", LongOpt.REQUIRED_ARGUMENT, null, 'm'),
				new LongOpt("staticOnly", LongOpt.NO_ARGUMENT, null, 's'),
				new LongOpt("undiff", LongOpt.NO_ARGUMENT, null, 'u'),
				new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
				new LongOpt("omitNegMem", LongOpt.NO_ARGUMENT, null, 'z'),
				new LongOpt("dense", LongOpt.NO_ARGUMENT, null, 'd'),
				new LongOpt("fAllWrap", LongOpt.NO_ARGUMENT, null, 'f'),
		};

		Getopt optionsParser = new Getopt("", args, "?l:i:b:q:tn:epo:x:am:csuvzdf", opts);

		boolean outputTrans = false, showOrigKB = false, getAllAnswers = false, 
				dynamicObj = true, omitNegMem = false, differentiated = true,
				isTest = false, dense = false, verbose = false, reconstruct = true,
				fAllWrap = false;
		String inputKBPath = null, inputQueryPath = null, lang = null, transKBPath = null,
			   prologPath = null, prologBackend = null;
		int timeout = -1, numRuns = 1;
		
		for (int opt = optionsParser.getopt(); opt != -1; opt = optionsParser
				.getopt()) {
			switch (opt) {
			case '?':
				printUsage(false);
				return;
			case '~':
				printUsage(true);
				return;
			case 'l':
				lang = optionsParser.getOptarg();
				break;
			case 'b':
				prologBackend = optionsParser.getOptarg();
				break;
			case 'd':
				dense = true;
				break;
			case 'i':
				inputKBPath = optionsParser.getOptarg();
				break;
			case 'q':
				inputQueryPath = optionsParser.getOptarg();
				break;
			case 'c':
				reconstruct = false;
				break;
			case 'm':
				try {
					timeout = Integer.parseInt(optionsParser.getOptarg());
				}
				catch (NumberFormatException e) {
					printErrlnAndExit("Incorrect number format for --timeout");
				}
				break;
			case 't':
				isTest = true;
				break;
			case 'n':
				try {
					numRuns = Integer.parseInt(optionsParser.getOptarg());
				}
				catch (NumberFormatException e) {
					printErrlnAndExit("Incorrect number format for --numRuns");
				}
				break;
			case 'e':
				showOrigKB = true;
				break;
			case 'p':
				outputTrans = true;
				break;
			case 'o':
				transKBPath = optionsParser.getOptarg();
				break;
			case 'x':
				prologPath = optionsParser.getOptarg();
				break;
			case 'a':
				getAllAnswers = true;
				break;
			case 's':
				dynamicObj = false;
				break;
			case 'u':
				differentiated = false;
				break;
			case 'v':
				verbose = true;
				break;
			case 'z':
				omitNegMem = true;
				break;
			case 'f':
				fAllWrap = true;
				break;
			default:
				assert false;
			}
		}
		
		// Display system name
		println(PSOATransRun.getSystemName(lang, prologBackend));
		
		// Check whether input KB file / directory has been specified
		if (inputKBPath == null) {
			printErrlnAndExit("No input KB specified");
		}

		// Initialize PSOATransRun
		Translator translator = null;
		ExecutionEngine engine = null;
		
		try {
			if (lang == null || lang.equalsIgnoreCase("prolog"))
			{
				PrologTranslator.Config transConfig = new PrologTranslator.Config();
				transConfig.setDynamicObj(dynamicObj);
				transConfig.setOmitMemtermInNegativeAtoms(omitNegMem);
				transConfig.setForallWrap(fAllWrap);
				transConfig.setDifferentiateObj(differentiated);
				transConfig.setReconstruct(reconstruct);
				translator = new PrologTranslator(transConfig);
				
				if (prologBackend == null || prologBackend.equalsIgnoreCase("xsb"))
				{
					XSBEngine.Config engineConfig = new XSBEngine.Config();
					engineConfig.transKBPath = transKBPath;
					engineConfig.xsbFolderPath = prologPath;
					engine = new XSBEngine(engineConfig);
				
					if (timeout > 0)
						printErrln("Ignore -t option: only applicable for the target language TPTP");
				}	
				else if (prologBackend.equalsIgnoreCase("swi"))
				{
					SWIEngine.Config engineConfig = new SWIEngine.Config();
					engineConfig.transKBPath = transKBPath;
					engineConfig.swiFolderPath = prologPath;
					engine = new SWIEngine(engineConfig);
				
					if (timeout > 0)
						printErrln("Ignore -t option: only applicable for the target language TPTP");
				} 
				else
				{
					printErrlnAndExit("Unsupported Prolog backend: ", prologBackend);
				}
			}
			else if (lang.equalsIgnoreCase("tptp"))
			{
				TPTPTranslator.Config transConfig = new TPTPTranslator.Config();
				transConfig.setDynamicObj(dynamicObj);
				transConfig.setOmitMemtermInNegativeAtoms(omitNegMem);
				transConfig.setForallWrap(fAllWrap);
				transConfig.setDifferentiateObj(differentiated);
				transConfig.setReconstruct(reconstruct);
				translator = new TPTPTranslator(transConfig);
				VampirePrimeEngine.Config engineConfig = new VampirePrimeEngine.Config();
				if (timeout > 0)
					engineConfig.timeout = timeout;
				engineConfig.transKBPath = transKBPath;
				engine = new VampirePrimeEngine(engineConfig);
				
				if (prologPath != null)
					printErrln("Ignore -x option: only applicable for the target language \"prolog\"");
				if (prologBackend != null)
					printErrln("Ignore -b option: only applicable for the target language \"prolog\"");
			}
			else
			{
				printErrlnAndExit("Unsupported language: ", lang);
			}
		}
		catch (PSOATransRunException e) {
			printErrlnAndExit(e.getMessage());
		}

		PSOATransRun system = new PSOATransRun(translator, engine);
		system.setPrintTrans(outputTrans);
		
		// Run tests
		if (isTest)
		{			
			try {
				TestSuite ts = new TestSuite(inputKBPath, system, numRuns, verbose);
				ts.run();
				ts.outputSummary();
				System.exit(0);
			}
			catch (PSOATransRunException e)
			{
				printErrlnAndExit(e.getMessage());
			}
		}
		
		try {
			// Print input PSOA KB if requested
			if (showOrigKB) {
				println("Original KB:");
				
				try (BufferedReader reader = new BufferedReader(
						new FileReader(inputKBPath))) {
					String line;
					while ((line = reader.readLine()) != null)
						println(line);
				}

				println();
			}
			
			// Load KB file
			system.loadKBFromFile(inputKBPath);
			println("KB Loaded. Enter Queries:");
			println();

			// Execute query from file input
			if (inputQueryPath != null) {
				try (FileInputStream queryStream = new FileInputStream(inputQueryPath);
					 Scanner sc = new Scanner(System.in)) {
					QueryResult result = system.executeQuery(queryStream);
					printQueryResult(result, getAllAnswers, sc);
					return;
				}
				catch (FileNotFoundException e) {
					printErrln("Cannot find query file ", inputQueryPath,
							". Read from console.");
				}
				catch (SecurityException e) {
					printErrln("Unable to read query file ", inputQueryPath,
							". Read from console.");
				}
			}

			// Execute query from console
			try (Scanner sc = new Scanner(System.in)) {
				// Console query loop
				do {
					print("> ");
					if (!sc.hasNext())
					{
						println();
						break;
					}
					
					String query = sc.nextLine();
					try {
						QueryResult result = system.executeQuery(query, getAllAnswers);
						printQueryResult(result, getAllAnswers, sc);
					}
					// The catch part could be later refined with specific kinds of 
					// exceptions that would not interfere future query executions, e.g.:
					// catch (PSOATransRunException | TranslatorException e)
					catch (Exception e)  
					{
						if (dense) {
							printErrln(e.getMessage());
						}
						else
							e.printStackTrace();
					}
					println();
				} while (true);
			}
		}
		catch (FileNotFoundException e) {
			printErrlnAndExit("Cannot find KB file ", inputKBPath);
		}
		catch (SecurityException e) {
			printErrlnAndExit("Unable to read KB file ", inputKBPath);
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
						while(true) {
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
								println(iter.hasNext()? "Yes" : "No");
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
	
	private static void printErrlnAndExit(Object... objs)
	{
		printErrln(objs);
		System.exit(1);
	}
	
	private static void printUsage(boolean isLong) {
		println("Usage: java -jar PSOATransRun.jar -i <kb> [-e] [-p] [-c] [-o <translated KB output>] [-q <query>] [-a] [-u] [-s] [-x <xsb folder>]");
		println("Options:");
		println("  -?,--help         Print the help message");
		println("  -a,--allAns       Retrieve all answers for each query at once");
		println("  -i,--input        Input Knowledge Base (KB)");
		println("  -e,--echoInput    Echo input KB to standard output");
		println("  -q,--query        Query document for the KB. If the query document");
		println("                    is not specified, the engine will read queries");
		println("                    from the standard input.");
		println("  -c,--explicitLocalConstants  Require explicit underscores for local constants");
		println("  -p,--printTrans   Print translated KB and queries to standard output");
		println("  -o,--outputTrans  Save translated KB to the designated file");
		println("  -l,--targetLang   Translation target language (currently support");
		println("                    \"prolog\" (default) and \"tptp\")");
		println("  -b,--backend      Backend for target language \"prolog\" (currently support");
		println("                    \"xsb\" (default) and \"swi\")");
		println("  -x,--prologFolder Specifies Prolog installation folder. The default path is ");
		println("                    obtained from the environment variable XSB_DIR for XSB Prolog,");
		println("                    or from default install locations for SWI Prolog");
		println("  -u,--undiff       Perform undifferentiated objectification");
		println("  -s,--staticOnly   Perform static objectification only");
		println("  -d,--denseErrorMsgs  Display dense error messages");
		println("  -f,--fAllWrap     Turn warning into error upon discovery of unquantified free variables in clauses");
		
		if (isLong)
		{
			println("     --longhelp     Print the help message, including commands for internal use");
			println("  -t,--test         Run PSOATransRun tests (-i specifies the directory");
			println("                    for the test suite)");
			println("  -n,--numRuns      Number of runs for each test case");
			println("  -m,--timeout      Timeout (only supported for the TPTP instantiation");
			println("                    of PSOATransRun)");
			println("  -v,--verbose      Show output for each test case while running tests");
			println("  -z,--omitNegMem   Omit memterm in the describution of negative occurrences");
			println("                    of psoa atoms with at least one dependent descriptor");
		}
	}
}

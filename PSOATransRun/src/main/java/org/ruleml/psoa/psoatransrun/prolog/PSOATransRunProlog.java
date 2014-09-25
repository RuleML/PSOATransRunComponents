package org.ruleml.psoa.psoatransrun.prolog;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.*;
import java.util.Map.Entry;
import java.util.*;

import org.apache.commons.exec.OS;
import org.ruleml.psoa.psoa2x.common.TranslatorException;
import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;

import com.declarativa.interprolog.*;
import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

public class PSOATransRunProlog
{
	private static PrologEngine _engine;
	private static boolean _outputTrans = false;
	
	public static void main(String[] args) throws TranslatorException, IOException
	{
		LongOpt[] opts = new LongOpt[]
		{
			new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?'),
			new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null,'i'),
			new LongOpt("outputTrans", LongOpt.OPTIONAL_ARGUMENT, null,'t'),
//			new LongOpt("output", LongOpt.OPTIONAL_ARGUMENT, null,'o'),
			new LongOpt("maxtime", LongOpt.REQUIRED_ARGUMENT, null,'m'),
			new LongOpt("xsbfolder", LongOpt.REQUIRED_ARGUMENT, null,'x'),
			new LongOpt("query", LongOpt.REQUIRED_ARGUMENT, null, 'q')
		};

		Getopt optionsParser = new Getopt("", args, "?i:t::o::q:x:", opts);
		FileInputStream kbStream = null,
						queryStream = null;
//		boolean outputTrans = false;
		String xsbFolder = null;
		String arg;
		
		for (int opt = optionsParser.getopt(); opt != -1; opt = optionsParser.getopt())
		{
			switch (opt) {
				case '?':
					printUsage();
					return;
	
				case 'i':
					arg = optionsParser.getOptarg(); 
					try
					{
						kbStream = new FileInputStream(arg);
					}
					catch (FileNotFoundException e)
					{
						printErrln("Cannot find KB file ", arg);
						System.exit(1);
					}
					catch (SecurityException e)
					{
						printErrln("Unable to read KB file ", arg);
						System.exit(1);
					}
					break;
					
				case 'q':
					arg = optionsParser.getOptarg(); 
					try
					{
						queryStream = new FileInputStream(arg);
					}
					catch (FileNotFoundException e)
					{
						printErrln("Cannot find query file ", arg, ". Read from console.");
					}
					catch (SecurityException e)
					{
						printErrln("Unable to read query file ", arg, ". Read from console.");
					}
					break;
				
				case 't':
					_outputTrans = true;
//					optionsParser.getOptarg();
					break;
				case 'x':
					xsbFolder = optionsParser.getOptarg();
					break;
				default:
					assert false;
			}
		}
		
		if (kbStream == null)
			printErrlnAndExit("Input KB must be specified.");
		
		if (xsbFolder == null)
			xsbFolder = System.getenv("XSB_DIR");
		
		if (xsbFolder == null)
			printErrlnAndExit("Unable to locate XSB installation folder.");
		
		File f = new File(xsbFolder);
		if (!(f.exists() && f.isDirectory()))
			printErrlnAndExit("XSB installation folder ", xsbFolder, " does not exist");
		
		if (OS.isFamilyUnix() || OS.isFamilyMac())
		{
			f = new File(f, "config");
			File[] subdirs = f.listFiles();
			if (subdirs == null || subdirs.length == 0)
				printErrlnAndExit("Cannot find XSB binary: ", f.getAbsolutePath(), " does not exist or is empty.");
			
			File xsbFile = null;
			for (File dir : subdirs)
			{
				File f1 = new File(dir, "bin/xsb");
				if (f1.canExecute())
					xsbFile = f1;
				
				if (dir.getName().contains("x86"))
					break;
			}
			
			if (xsbFile != null)
				_engine = new XSBSubprocessEngine(xsbFile.getAbsolutePath());
			else
				printErrlnAndExit("Cannot find executable xsb binary in ", f.getAbsolutePath());
		}
		else if (OS.isFamilyWindows())
		{
			f = new File(f, "config\\x86-pc-windows\\bin\\xsb");
			_engine = new XSBSubprocessEngine(f.getAbsolutePath());
		}
		else
		{
			printErrlnAndExit("Unsupported operating system.");
		}
		
		PrologTranslator translator = new PrologTranslator();
		String transKB = translator.translateKB(kbStream);
		
		File transKBFile = tmpFile("tmp-", ".pl");
		PrintWriter writer = new PrintWriter(transKBFile);
		writer.println(":- auto_table.");
		writer.print(transKB);
		writer.close();

		if (_outputTrans)
		{
			println("Translated KB:");
			println(transKB);
		}

		if (_engine.consultAbsolute(transKBFile))
			println("KB Loaded");
		else
		{
			_engine.interrupt();
			println("Failed to load KB");
		}
		
		if (queryStream != null)
		{
			String transQuery = translator.translateQuery(queryStream);
			
			answerQuery(transQuery, translator.getQueryVarMap());
		}
		else
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String psoaQuery;
			
			do 
			{
				println("Input Query:");
				psoaQuery = reader.readLine();
				if (psoaQuery == null)
					break;
				
				try
				{
					String transQuery = translator.translateQuery(psoaQuery);
					answerQuery(transQuery, translator.getQueryVarMap());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				println();
			} while (true);
			
			reader.close();
		}
		
		if (_engine != null)
			_engine.shutdown();
	}

	private static void answerQuery(String transQuery, Map<String, String> queryVarMap)
	{
		Set<Entry<String, String>> varMapEntries;
		TermModel result;
		
		if (_outputTrans)
		{
			println("Translated Query:");
			println(transQuery + ".");
		}
		varMapEntries = queryVarMap.entrySet();
		
		println();
		println("Answer(s):");
		if (varMapEntries.isEmpty())
		{
			println(_engine.deterministicGoal(transQuery));
		}
		else
		{
			StringBuilder outputBuilder = new StringBuilder("findall([");
			for (Entry<String, String> entry : varMapEntries)
			{
				outputBuilder.append("\'?").append(entry.getValue()).append("=\',");
				outputBuilder.append(entry.getKey()).append(",");
			}
			outputBuilder.setCharAt(outputBuilder.length() - 1, ']');
			outputBuilder.append(",(").append(transQuery).append("),AS),buildTermModel(AS,LM)");
			
			result = (TermModel)_engine.deterministicGoal(outputBuilder.toString(), "[LM]")[0];
//			result = engine.deterministicGoal("findall([Q1],member(lo1,Q1),AS),buildTermModel(AS,LM)", "[LM]")[0];
			
			if (result.getChildCount() > 0)
			{
				Set<String> answers = new HashSet<String>();
				StringBuilder ansBuilder = new StringBuilder();
				boolean separator = false;
				for (TermModel bindings : result.getChildren())
				{
					for (TermModel term : bindings.getChildren())
					{
						ansBuilder.append(term);
						if (separator)
							ansBuilder.append(",");
						separator = !separator;
					}
					ansBuilder.setLength(ansBuilder.length() - 1);
					
					String ans = ansBuilder.toString();
					if (answers.add(ans))
						println(ans);
					ansBuilder.setLength(0);
				}
			}
		}
	}
	
	private static void printUsage()
	{
		println("Usage: java -jar PSOATransRun.jar --jar -i <rulebase> [-q <query>] [-t] [-x]");
		println("Options:");
		println("  -?,--help         Print the help message");
//		println("");
		println("  -i,--input        Input Knowledge Base (KB)");
//		println("\t--import_closure -i \n\t\tProcess the whole import closures of the rule bases.");
		println("  -q,--query        Query document for the KB. If the query document");
		println("                    is not specified, the engine will read query input");
		println("                    from the console.");
		println("  -t,--outputTrans  Output translated KB and query");
		println("  -x,--xsbfolder    Specifies XSB installation folder. The default path is ");
		println("                    obtained from the environment variable XSB_DIR");
		
	} // End printUsage()
}

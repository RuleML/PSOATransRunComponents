package org.ruleml.psoa.psoatransrun.prolog;

import java.io.*;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ruleml.psoa.psoa2x.common.TranslatorException;
import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTranslator;

import com.declarativa.interprolog.*;
import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;

public class PSOATransRunProlog {
	private static PrologEngine _engine;
	
	public static void main(String[] args) throws TranslatorException, IOException
	{
		PrologTranslator translator = new PrologTranslator();
		String transKB = translator.translateKB(new FileInputStream(args[0]));
		
		File transKBFile = tmpFile("tmp-", ".pl");
		PrintWriter writer = new PrintWriter(transKBFile);
		writer.println(":- auto_table.");
		writer.print(transKB);
		writer.close();
		
//		System.out.println("Translated KB:");
//		System.out.println(transKB);
		System.out.println("KB Loaded");
		
		if (args.length > 1)
		{
			String transQuery = translator.translateQuery(new FileInputStream(args[1]));
			
			answerQuery(transKBFile, transQuery, translator.getQueryVarMap());
		}
		else
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String psoaQuery;
			
			do 
			{
				System.out.println("Input Query:");
				psoaQuery = reader.readLine();
				if (psoaQuery == null)
					break;
				
				try
				{
					String transQuery = translator.translateQuery(psoaQuery);
					answerQuery(transKBFile, transQuery, translator.getQueryVarMap());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				System.out.println();
			} while (true);
			
			reader.close();
		}
		
		if (_engine != null)
			_engine.shutdown();
		
//		Object result = engine.deterministicGoal("add(X,Y,pair(1,1)),buildTermModel([X,Y],LM)", "[LM]")[0];
		
		
//		engine.command("import append/3 from basics");
//		Object[] bindings = engine.deterministicGoal(
//				"name(User,UL),append(\"Hello,\", UL, ML), name(Message,ML)", "[string(User)]",
//				new Object[]{System.getProperty("user.name")}, "[string(Message)]");
//				"name(User,UL),append(\"Hello,\", UL, ML), name(Message,ML)", "[string(User)]",
//				new Object[]{System.getProperty("user.name")}, "[ML]");
//		String message = (String)bindings[0];
//		System.out.println("\nMessage:"+message);
		
		// the above demonstrates object passing both ways; 
		// since we may simply concatenate strings, an alternative coding would be:
//		bindings = engine.deterministicGoal(
//				"name('"+System.getProperty("user.name")+"',UL),append(\"Hello,\", UL, ML), name(Message,ML)", "[string(Message)]");
//		 (notice the ' surrounding the user name, unnecessary in the first case)
//		System.out.println("Same:" + bindings[0]);
	}

	private static void answerQuery(File transKBFile, String transQuery, Map<String, String> queryVarMap)
	{
		Set<Entry<String, String>> varMapEntries;
		TermModel result;
		
		_engine = new XSBSubprocessEngine("D:\\Software\\Development\\XSBProlog\\config\\x64-pc-windows\\bin\\xsb.exe");
		
//		System.out.println("Translated Query:");
//		System.out.println(transQuery + ".");
		varMapEntries = queryVarMap.entrySet();

		if (!_engine.consultAbsolute(transKBFile))
		{
			System.out.println("Failed to load KB");
			_engine.interrupt();
		}
		
		System.out.println();
		System.out.println("Answer(s):");
		if (varMapEntries.isEmpty())
		{
			System.out.println(_engine.deterministicGoal(transQuery));
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
						System.out.println(ans);
					ansBuilder.setLength(0);
				}
			}
		}
	}
	
	private static void printUsage() {

		System.out.println("Usage: PSOATransRunProlog <rulebase> <query>");
		System.out.println("Options:");
		System.out.println("\t--help -? \n\t\t Print this message.");
//		System.out.println("\t--import_closure -i \n\t\tProcess the whole import closures of the rule bases.");
		System.out.println("\t--query -q <query document file>\n\t\tQuery document for rulebases.");
	} // End printUsage()
}

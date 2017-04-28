package org.ruleml.psoa.psoatransrun.tptp;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.commons.exec.CommandLine;
import org.ruleml.psoa.psoatransrun.*;
import org.ruleml.psoa.psoatransrun.engine.ExecutionEngine;

import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;
import static org.ruleml.psoa.psoatransrun.utils.ShellUtil.execute;

public class VampirePrimeEngine extends ExecutionEngine {
	private VampirePrimeEngineConfig m_config;
	
	public VampirePrimeEngine(VampirePrimeEngineConfig config) {
		m_config = config;
	}

	@Override
	public QueryResult executeQuery(String kb, String query, List<String> queryVars) {
		File input = tmpFile("request-", ".p");

		PrintWriter w = writer(input);
		w.println(kb);
		w.println(query);
		w.close();
		
		CommandLine cl = new CommandLine(m_config.binPath);		
		cl.addArguments(new String[]{ "-t", String.valueOf(m_config.timeout), "-m",
				"300000", "--elim_def", "0", "--selection", "8",
				"--config", m_config.answerPredicatePath, "--max_number_of_answers", "100",
				"--inconsistencies_as_answers", "off",
				"--limited_abs_lit_chaining", "on",
				"--proof", "on", input.getAbsolutePath()
		});
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		execute(cl, out, (m_config.timeout + 60) * 1000);
		
		try {
			return parseVPResult(new ByteArrayInputStream(out.toByteArray()));
		}
		catch (RuntimeException e)
		{
			System.out.println("hello");
			System.out.println(out.toString());
			throw e;
		}
	}
	
	private static QueryResult parseVPResult(InputStream out)
	{
		 Scanner reader = new Scanner(out);
		 String line = null, nextLine = null;
		 SubstitutionSet bindings = new SubstitutionSet();
	 
		 do
		 {
			 while (!reader.nextLine().equals("%========== End of statistics. ======================"))
				 continue;
			 
			 if(reader.nextLine().startsWith("% Proof not found."))
				 break;
			 
			 // Read until "========== Proof: =========="
			 while (!reader.nextLine().endsWith("="))
				 continue;
			 
			 // Locate last line of proof
			 do
			 {
				 line = nextLine;
				 nextLine = reader.nextLine();
			 } while (!nextLine.endsWith("="));
			 
			 int ind = line.indexOf("<<ans>>") + 7;
			 
			 if (line.charAt(ind) != '(')
				 // Successful ground query
				 return new QueryResult(true);
			 
			 bindings.add(parseVPBinding(line.substring(ind + 1, line.lastIndexOf(')'))));
			 
			 reader.nextLine();
			 reader.nextLine();
			 reader.nextLine();
		 } while (true);
		 
		 return new QueryResult(bindings);
	}
	
	private static Substitution parseVPBinding(String str)
	{
		Substitution b = new Substitution();
		String[] splits = str.split(",");
		
		for (int i = 0; i < splits.length; i += 2)
		{
			String value = splits[i+1]; 
			if (value.startsWith("l"))
				value = "_" + value.substring(1);
			b.addPair(splits[i].substring(1, splits[i].length() - 4), value);
		}
		
		return b;
	}
	
	public static void main(String[] args) {
		String input = "% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~memterm(X3,lperson) \\/ <<ans>>(\"?Ancestor = \",X3,\"?Who = \",X4) \\/ ~sloterm(X3,ldescendent,X4) /1/12/0/0/ 0pe [in ]\r\n" + 
				"% 4. memterm(lAmy,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 7. sloterm(lAmy,lchild,lFred) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 17. ~memterm(X0,lperson) \\/ sloterm(X0,ldescendent,X1) \\/ ~sloterm(X0,lchild,X1) /1/11/0/0/ 0pe [in ]\r\n" + 
				"% 19. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /1/12/3/1/ 0pe [pp ns 1]\r\n" + 
				"% 22. memterm(lAmy,lperson) /1/3/0/1/ 0pe [pp 4]\r\n" + 
				"% 25. sloterm(lAmy,lchild,lFred) /1/4/0/1/ 0pe [pp 7]\r\n" + 
				"% 34. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /1/11/7/1/ 0pe [pp ns 17]\r\n" + 
				"% * 35. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /7/12/3/2/ 0pe [pp ns 19]\r\n" + 
				"% * 38. memterm(lAmy,lperson) /3/3/0/2/ 0pe vip [pp 22]\r\n" + 
				"% * 41. sloterm(lAmy,lchild,lFred) /4/4/0/2/ 0pe [pp 25]\r\n" + 
				"% * 50. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /11/11/7/2/ 0pe [pp ns 34]\r\n" + 
				"% * 54. sloterm(lAmy,ldescendent,lFred) /4/4/0/3/ 0pe vip [br fsr 38,41,50]\r\n" + 
				"% 57.  | <<ans>>(\"?Ancestor = \",lAmy,\"?Who = \",lFred) /1/5/0/4/ 0pe vip [br fsr 38,35,54]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 1 answer found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~memterm(X3,lperson) \\/ <<ans>>(\"?Ancestor = \",X3,\"?Who = \",X4) \\/ ~sloterm(X3,ldescendent,X4) /1/12/0/0/ 0pe [in ]\r\n" + 
				"% 8. memterm(lEva,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 10. sloterm(lEva,lchild,lAmy) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 17. ~memterm(X0,lperson) \\/ sloterm(X0,ldescendent,X1) \\/ ~sloterm(X0,lchild,X1) /1/11/0/0/ 0pe [in ]\r\n" + 
				"% 19. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /1/12/3/1/ 0pe [pp ns 1]\r\n" + 
				"% 26. memterm(lEva,lperson) /1/3/0/1/ 0pe [pp 8]\r\n" + 
				"% 28. sloterm(lEva,lchild,lAmy) /1/4/0/1/ 0pe [pp 10]\r\n" + 
				"% 34. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /1/11/7/1/ 0pe [pp ns 17]\r\n" + 
				"% * 35. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /7/12/3/2/ 0pe [pp ns 19]\r\n" + 
				"% * 42. memterm(lEva,lperson) /3/3/0/2/ 0pe vip [pp 26]\r\n" + 
				"% * 44. sloterm(lEva,lchild,lAmy) /4/4/0/2/ 0pe [pp 28]\r\n" + 
				"% * 50. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /11/11/7/2/ 0pe [pp ns 34]\r\n" + 
				"% * 55. sloterm(lEva,ldescendent,lAmy) /4/4/0/3/ 0pe vip [br fsr 42,44,50]\r\n" + 
				"% 59.  | <<ans>>(\"?Ancestor = \",lEva,\"?Who = \",lAmy) /1/5/0/4/ 0pe vip [br fsr 42,35,55]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 2 answers found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~memterm(X3,lperson) \\/ <<ans>>(\"?Ancestor = \",X3,\"?Who = \",X4) \\/ ~sloterm(X3,ldescendent,X4) /1/12/0/0/ 0pe [in ]\r\n" + 
				"% 11. memterm(lTom,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 13. sloterm(lTom,lchild,lAmy) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 17. ~memterm(X0,lperson) \\/ sloterm(X0,ldescendent,X1) \\/ ~sloterm(X0,lchild,X1) /1/11/0/0/ 0pe [in ]\r\n" + 
				"% 19. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /1/12/3/1/ 0pe [pp ns 1]\r\n" + 
				"% 29. memterm(lTom,lperson) /1/3/0/1/ 0pe [pp 11]\r\n" + 
				"% 31. sloterm(lTom,lchild,lAmy) /1/4/0/1/ 0pe [pp 13]\r\n" + 
				"% 34. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /1/11/7/1/ 0pe [pp ns 17]\r\n" + 
				"% * 35. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /7/12/3/2/ 0pe [pp ns 19]\r\n" + 
				"% * 45. memterm(lTom,lperson) /3/3/0/2/ 0pe vip [pp 29]\r\n" + 
				"% * 47. sloterm(lTom,lchild,lAmy) /4/4/0/2/ 0pe [pp 31]\r\n" + 
				"% * 50. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /11/11/7/2/ 0pe [pp ns 34]\r\n" + 
				"% * 56. sloterm(lTom,ldescendent,lAmy) /4/4/0/3/ 0pe vip [br fsr 45,47,50]\r\n" + 
				"% 61.  | <<ans>>(\"?Ancestor = \",lTom,\"?Who = \",lAmy) /1/5/0/4/ 0pe vip [br fsr 45,35,56]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 3 answers found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~memterm(X3,lperson) \\/ <<ans>>(\"?Ancestor = \",X3,\"?Who = \",X4) \\/ ~sloterm(X3,ldescendent,X4) /1/12/0/0/ 0pe [in ]\r\n" + 
				"% 4. memterm(lAmy,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 7. sloterm(lAmy,lchild,lFred) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 8. memterm(lEva,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 10. sloterm(lEva,lchild,lAmy) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 15. ~sloterm(X0,lchild,X1) \\/ ~memterm(X0,lperson) \\/ ~memterm(X1,lperson) \\/ sloterm(X0,ldescendent,X2) \\/ ~sloterm(X1,ldescendent,X2) /1/18/0/0/ 0pe [in ]\r\n" + 
				"% 17. ~memterm(X0,lperson) \\/ sloterm(X0,ldescendent,X1) \\/ ~sloterm(X0,lchild,X1) /1/11/0/0/ 0pe [in ]\r\n" + 
				"% 19. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /1/12/3/1/ 0pe [pp ns 1]\r\n" + 
				"% 22. memterm(lAmy,lperson) /1/3/0/1/ 0pe [pp 4]\r\n" + 
				"% 25. sloterm(lAmy,lchild,lFred) /1/4/0/1/ 0pe [pp 7]\r\n" + 
				"% 26. memterm(lEva,lperson) /1/3/0/1/ 0pe [pp 8]\r\n" + 
				"% 28. sloterm(lEva,lchild,lAmy) /1/4/0/1/ 0pe [pp 10]\r\n" + 
				"% 33. ~sloterm(X0,lchild,X1) \\/ ~sloterm(X1,ldescendent,X2) | sloterm(X0,ldescendent,X2) \\/ ~memterm(X0,lperson) \\/ ~memterm(X1,lperson) /1/18/10/1/ 0pe [pp ns 15]\r\n" + 
				"% 34. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /1/11/7/1/ 0pe [pp ns 17]\r\n" + 
				"% * 35. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /7/12/3/2/ 0pe [pp ns 19]\r\n" + 
				"% * 38. memterm(lAmy,lperson) /3/3/0/2/ 0pe vip [pp 22]\r\n" + 
				"% * 41. sloterm(lAmy,lchild,lFred) /4/4/0/2/ 0pe [pp 25]\r\n" + 
				"% * 42. memterm(lEva,lperson) /3/3/0/2/ 0pe vip [pp 26]\r\n" + 
				"% * 44. sloterm(lEva,lchild,lAmy) /4/4/0/2/ 0pe [pp 28]\r\n" + 
				"% * 49. ~sloterm(X0,lchild,X1) \\/ ~sloterm(X1,ldescendent,X2) | sloterm(X0,ldescendent,X2) \\/ ~memterm(X1,lperson) \\/ ~memterm(X0,lperson) /18/18/10/2/ 0pe [pp ns 33]\r\n" + 
				"% * 50. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /11/11/7/2/ 0pe [pp ns 34]\r\n" + 
				"% * 52. ~sloterm(lAmy,ldescendent,X0) | sloterm(lEva,ldescendent,X0) /8/8/4/3/ 0pe vip [br fsr ns 38,42,44,49]\r\n" + 
				"% * 54. sloterm(lAmy,ldescendent,lFred) /4/4/0/3/ 0pe vip [br fsr 38,41,50]\r\n" + 
				"% * 63. sloterm(lEva,ldescendent,lFred) /4/4/0/4/ 0pe [br 54,52]\r\n" + 
				"% 65.  | <<ans>>(\"?Ancestor = \",lEva,\"?Who = \",lFred) /1/5/0/5/ 0pe vip [br fsr 42,35,63]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 4 answers found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~memterm(X3,lperson) \\/ <<ans>>(\"?Ancestor = \",X3,\"?Who = \",X4) \\/ ~sloterm(X3,ldescendent,X4) /1/12/0/0/ 0pe [in ]\r\n" + 
				"% 4. memterm(lAmy,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 7. sloterm(lAmy,lchild,lFred) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 11. memterm(lTom,lperson) /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 13. sloterm(lTom,lchild,lAmy) /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 15. ~sloterm(X0,lchild,X1) \\/ ~memterm(X0,lperson) \\/ ~memterm(X1,lperson) \\/ sloterm(X0,ldescendent,X2) \\/ ~sloterm(X1,ldescendent,X2) /1/18/0/0/ 0pe [in ]\r\n" + 
				"% 17. ~memterm(X0,lperson) \\/ sloterm(X0,ldescendent,X1) \\/ ~sloterm(X0,lchild,X1) /1/11/0/0/ 0pe [in ]\r\n" + 
				"% 19. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /1/12/3/1/ 0pe [pp ns 1]\r\n" + 
				"% 22. memterm(lAmy,lperson) /1/3/0/1/ 0pe [pp 4]\r\n" + 
				"% 25. sloterm(lAmy,lchild,lFred) /1/4/0/1/ 0pe [pp 7]\r\n" + 
				"% 29. memterm(lTom,lperson) /1/3/0/1/ 0pe [pp 11]\r\n" + 
				"% 31. sloterm(lTom,lchild,lAmy) /1/4/0/1/ 0pe [pp 13]\r\n" + 
				"% 33. ~sloterm(X0,lchild,X1) \\/ ~sloterm(X1,ldescendent,X2) | sloterm(X0,ldescendent,X2) \\/ ~memterm(X0,lperson) \\/ ~memterm(X1,lperson) /1/18/10/1/ 0pe [pp ns 15]\r\n" + 
				"% 34. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /1/11/7/1/ 0pe [pp ns 17]\r\n" + 
				"% * 35. ~sloterm(X0,ldescendent,X1) | <<ans>>(\"?Ancestor = \",X0,\"?Who = \",X1) \\/ ~memterm(X0,lperson) /7/12/3/2/ 0pe [pp ns 19]\r\n" + 
				"% * 38. memterm(lAmy,lperson) /3/3/0/2/ 0pe vip [pp 22]\r\n" + 
				"% * 41. sloterm(lAmy,lchild,lFred) /4/4/0/2/ 0pe [pp 25]\r\n" + 
				"% * 45. memterm(lTom,lperson) /3/3/0/2/ 0pe vip [pp 29]\r\n" + 
				"% * 47. sloterm(lTom,lchild,lAmy) /4/4/0/2/ 0pe [pp 31]\r\n" + 
				"% * 49. ~sloterm(X0,lchild,X1) \\/ ~sloterm(X1,ldescendent,X2) | sloterm(X0,ldescendent,X2) \\/ ~memterm(X1,lperson) \\/ ~memterm(X0,lperson) /18/18/10/2/ 0pe [pp ns 33]\r\n" + 
				"% * 50. ~sloterm(X0,lchild,X1) | sloterm(X0,ldescendent,X1) \\/ ~memterm(X0,lperson) /11/11/7/2/ 0pe [pp ns 34]\r\n" + 
				"% * 53. ~sloterm(lAmy,ldescendent,X0) | sloterm(lTom,ldescendent,X0) /8/8/4/3/ 0pe vip [br fsr ns 38,45,47,49]\r\n" + 
				"% * 54. sloterm(lAmy,ldescendent,lFred) /4/4/0/3/ 0pe vip [br fsr 38,41,50]\r\n" + 
				"% * 64. sloterm(lTom,ldescendent,lFred) /4/4/0/4/ 0pe [br 54,53]\r\n" + 
				"% 67.  | <<ans>>(\"?Ancestor = \",lTom,\"?Who = \",lFred) /1/5/0/5/ 0pe vip [br fsr 45,35,64]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 5 answers found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"% Proof not found. Empty passive.\r\n" + 
				"";
		
		QueryResult r = parseVPResult(new ByteArrayInputStream(input.getBytes()));
		System.out.println(r);
	}
}

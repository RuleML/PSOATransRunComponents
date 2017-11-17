package org.ruleml.psoa.psoatransrun.tptp;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.ruleml.psoa.psoa2x.common.Translator;
import org.ruleml.psoa.psoa2x.common.TranslatorFactory;
import org.ruleml.psoa.psoatransrun.*;
import org.ruleml.psoa.psoatransrun.engine.EngineConfig;
import org.ruleml.psoa.psoatransrun.engine.ReusableKBEngine;

import static org.ruleml.psoa.utils.IOUtil.*;
import static org.ruleml.psoa.psoatransrun.utils.IOUtil.*;
import static org.ruleml.psoa.psoatransrun.utils.ShellUtil.execute;

public class VampirePrimeEngine extends ReusableKBEngine {
	private Config m_config;
	private File m_transKBFile;

	public static class Config extends EngineConfig {
		public String binPath, answerPredicatePath, transKBPath;
	}
	
	/**
	 * VampirePrime engine configuration
	 * 
	 * */
	public VampirePrimeEngine() {
		this(new Config());
	}

	public VampirePrimeEngine(Config config) {
		m_config = config;
		ClassLoader loader = this.getClass().getClassLoader();
		if (config.binPath == null)
		{
			File bin = extractFromResource(loader, "vampirePrime/vkernel");
			bin.setExecutable(true, true);
			config.binPath = bin.getAbsolutePath();
		}
		
		if (config.answerPredicatePath == null)
			config.answerPredicatePath = extractFromResource(loader, "vampirePrime/answer_predicates.xml").getAbsolutePath();
		
		String transKBPath = config.transKBPath;
		try
		{
			if (transKBPath != null)
			{
				if (!transKBPath.endsWith(".p"))
					throw new PSOATransRunException("TPTP translation output file name must end with .p: " + transKBPath);
				m_transKBFile = new File(transKBPath);
				m_transKBFile.createNewFile();
			}
			else
				m_transKBFile = tmpFile("tmp-kb-", ".p");
		}
		catch (IOException e)
		{
			throw new PSOATransRunException(e);
		}
	}

	@Override
	public String language() {
		return "tptp";
	}

	@Override
	public void loadKB(String kb) {
		try(PrintWriter writer = new PrintWriter(m_transKBFile))
		{
			writer.print(kb);
		}
		catch (FileNotFoundException e) {
			throw new PSOATransRunException(e);
		}
	}

	@Override
	public QueryResult executeQuery(String query, List<String> queryVars,
			boolean getAllAnswers) {
		File queryFile = tmpFile("tmp-query-", ".p");
		
		try(PrintWriter writer = new PrintWriter(queryFile))
		{
			writer.print(query);
		}
		catch (FileNotFoundException e) {
			throw new PSOATransRunException(e);
		}
		
		CommandLine cl = new CommandLine(m_config.binPath);
		cl.addArguments(new String[] { "-t", String.valueOf(m_config.timeout),
				"-m", "2048000", "--elim_def", "0", "--selection", "8",
				"--config", m_config.answerPredicatePath,
				"--max_number_of_answers", "400",
				"--inconsistencies_as_answers", "off",
				// "--limited_abs_lit_chaining", "on",
				"--proof", "on",
				m_transKBFile.getAbsolutePath(),
				queryFile.getAbsolutePath() });

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		execute(cl, out, (m_config.timeout + 60) * 1000);
		
		try {
			return parseVPResult(new ByteArrayInputStream(out.toByteArray()), queryVars);
		} catch (RuntimeException e) {
			printErrln("Failed to parse VampirePrime output:");
			printErrln(out);
			throw e;
		}
	}
	
	private static QueryResult parseVPResult(InputStream out, List<String> queryVars) {
		String line = null, nextLine = null;
		SubstitutionSet answers = new SubstitutionSet();
		
		try (Scanner reader = new Scanner(out)) {
			do {
				while (!reader.nextLine().equals(
						"%========== End of statistics. ======================"))
					continue;
	
				if (reader.nextLine().startsWith("% Proof not found."))
					break;
	
				// Read until "========== Proof: =========="
				while (!reader.nextLine().endsWith("="))
					continue;
	
				// Locate last line of proof
				do {
					line = nextLine;
					nextLine = reader.nextLine();
				} while (!nextLine.endsWith("="));
				
				int ind = line.indexOf("<<ans>>") + 7;
	
				if (line.charAt(ind) != '(')
					// Successful ground query
					return new QueryResult(true);
	
				answers.add(parseVPBinding(
						line.substring(ind + 1, line.lastIndexOf(')')), queryVars));
	
				reader.nextLine();
				reader.nextLine();
				reader.nextLine();
			} while (true);
		}

		return new QueryResult(answers);
	}

	private static Pattern s_varStrPattern = Pattern.compile("\\\"\\?\\w+= \\\",");
	
	private static Substitution parseVPBinding(String bindings, List<String> queryVars) {
		Substitution b = new Substitution();
		Matcher matcher = s_varStrPattern.matcher(bindings);
		matcher.find(0);
		
		int start = matcher.end();
		for (String var : queryVars) {			
			if(matcher.find(start))
			{
				b.addPair(var, bindings.substring(start, matcher.start() - 1));
				start = matcher.end();
			}
			else
			{
				b.addPair(var, bindings.substring(start));
				break;
			}
		}

		return b;
	}

	public static void main(String[] args) {
		String input = "%=================================================================\r\n" + 
				"% Vampire kernel v0.18 (0.17 + bug fix in clausification)\r\n" + 
				"% Started: \r\n" +
				"% Problem: /tmp/PSOATransRun/request-1.p\r\n" + 
				"% Command: /tmp/PSOATransRun/vampirePrime/vkernel -t -1 -m 300000 --elim_def 0 --selection 8 --config /tmp/PSOATransRun/vampirePrime/answer_predicates.xml --max_number_of_answers 100 --inconsistencies_as_answers off --limited_abs_lit_chaining on --proof on /tmp/PSOATransRun/request-1.p \r\n" + 
				"%=================================================================\r\n" + 
				"% Reading parameters from /tmp/PSOATransRun/vampirePrime/answer_predicates.xml\r\n" + 
				"% Parameters were read from /tmp/PSOATransRun/vampirePrime/answer_predicates.xml\r\n" + 
				"% New Vampire Kernel session, time limit: 0.0 sec.\r\n" + 
				"%=============== Preprocessing by simplification 0 ============\r\n" + 
				"%======== Preprocessing has been done in 1 stages.\r\n" + 
				"% WARNING: No equality. All equality related features are switched off!\r\n" + 
				"%================= Statistics: =======================\r\n" + 
				"% time 0 (0 current loop)\r\n" + 
				"% memory 1086Kb (1113Kb in chunks), total buffer deficit 0Kb\r\n" + 
				"%=== Generated clauses:\r\n" + 
				"%      total 125 (95 res + 0 eq + 30 other)\r\n" + 
				"%      forward subsumed 36\r\n" + 
				"%      eq.tautologies   0\r\n" + 
				"%      prop.tautologies 0\r\n" + 
				"%      simplified 51\r\n" + 
				"%         by FSR          51\r\n" + 
				"%         by demod.       0\r\n" + 
				"%               on splitting branches 0\r\n" + 
				"%         by built-in th. 0\r\n" + 
				"%         by eq. res.     0\r\n" + 
				"%      split 0  avg.components 1  diff.components 0\r\n" + 
				"%      useless:\r\n" + 
				"%        too deep inferences 0\r\n" + 
				"%        non-factorable answers 0\r\n" + 
				"%        func. in abstr. lit.   0\r\n" + 
				"%        too long abstr. lit. chains  0\r\n" + 
				"%        too heavy 0\r\n" + 
				"%        too heavy literals 0\r\n" + 
				"%        too long 0\r\n" + 
				"%        too deep 0\r\n" + 
				"%        too many variables 0\r\n" + 
				"%        refused allocation 0\r\n" + 
				"%=== Retained clauses:\r\n" + 
				"%      total 86\r\n" + 
				"%      used 67\r\n" + 
				"%      back subsumed 0\r\n" + 
				"%      back simplified 0\r\n" + 
				"%        on splitting branches 0\r\n" + 
				"%      rewritten by def. unfolding 0\r\n" + 
				"%      orphans murdered 0\r\n" + 
				"%               passive 0\r\n" + 
				"%               active  0\r\n" + 
				"%      recycled 0\r\n" + 
				"%      currently passive 6 (2147483648 reachable)\r\n" + 
				"%      reserved passive 0\r\n" + 
				"%      currently active 67\r\n" + 
				"%========== End of statistics. ======================\r\n" + 
				"% Proof found. Thanks to Tanya!\r\n" + 
				"%================== Proof: ======================\r\n" + 
				"% 1. ~sloterm(X13,'_south',X9) \\/ ~sloterm(X13,'_north',X11) \\/ ~sloterm(X13,'_west',X12) \\/ ~sloterm(X13,'_east',X10) \\/ ~prdtupterm(X13,'_compassRose',X8) \\/ <<ans>>(\"?I= \",X13,\"?W= \",X12,\"?N= \",X11,\"?E= \",X10,\"?S= \",X9,\"?C= \",X8) \\/ ~memterm(X13,'_compassRose') /1/36/0/0/ 0pe [in ]\r\n" + 
				"% 2. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ memterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_compassRose') /1/36/0/0/ 0pe [in ]\r\n" + 
				"% 3. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ prdtupterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_compassRose',X2) /1/37/0/0/ 0pe [in ]\r\n" + 
				"% 4. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ sloterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_south',X5) /1/37/0/0/ 0pe [in ]\r\n" + 
				"% 5. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ sloterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_east',X3) /1/37/0/0/ 0pe [in ]\r\n" + 
				"% 6. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ sloterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_north',X6) /1/37/0/0/ 0pe [in ]\r\n" + 
				"% 7. ~prdtupterm(X0,'_betweenObjRel',X4,X2,X3) \\/ ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~prdtupterm(X1,'_betweenObjRel',X6,X2,X5) \\/ ~sloterm(X1,'_orient','_northSouth') \\/ ~memterm(X1,'_betweenObjRel') \\/ sloterm(vampireSkF0(X6,X5,X4,X3,X2,X1,X0),'_west',X4) /1/37/0/0/ 0pe [in ]\r\n" + 
				"% 9. sloterm('_a3','_orient','_westEast') /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 10. prdtupterm('_a3','_betweenObjRel','_pacific','_usa','_atlantic') /1/6/0/0/ 0pe [in ]\r\n" + 
				"% 11. memterm('_a3','_betweenObjRel') /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 13. sloterm('_a2','_orient','_northSouth') /1/4/0/0/ 0pe [in ]\r\n" + 
				"% 14. prdtupterm('_a2','_betweenObjRel','_canada','_usa','_mexico') /1/6/0/0/ 0pe [in ]\r\n" + 
				"% 15. memterm('_a2','_betweenObjRel') /1/3/0/0/ 0pe [in ]\r\n" + 
				"% 16. ~sloterm(X0,'_west',X1) \\/ ~sloterm(X0,'_north',X2) \\/ ~sloterm(X0,'_east',X3) \\/ ~sloterm(X0,'_south',X4) | <<ans>>(\"?I= \",X0,\"?W= \",X1,\"?N= \",X2,\"?E= \",X3,\"?S= \",X4,\"?C= \",X5) \\/ ~prdtupterm(X0,'_compassRose',X5) \\/ ~memterm(X0,'_compassRose') /1/36/7/1/ 0pe [pp ns 1]\r\n" + 
				"% 17. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ ~memterm(X4,'_betweenObjRel') \\/ memterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_compassRose') /1/36/24/1/ 0pe [pp ns 2]\r\n" + 
				"% 18. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ prdtupterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_compassRose',X2) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /1/37/25/1/ 0pe [pp ns 3]\r\n" + 
				"% 19. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ sloterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_south',X3) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /1/37/25/1/ 0pe [pp ns 4]\r\n" + 
				"% 20. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X4,'_orient','_northSouth') \\/ ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0(X5,X6,X1,X3,X2,X4,X0),'_east',X3) \\/ ~memterm(X0,'_betweenObjRel') \\/ ~memterm(X4,'_betweenObjRel') /1/37/25/1/ 0pe [pp ns 5]\r\n" + 
				"% 21. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ sloterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_north',X1) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /1/37/25/1/ 0pe [pp ns 6]\r\n" + 
				"% 22. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X4,'_orient','_northSouth') \\/ ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0(X5,X6,X1,X3,X2,X4,X0),'_west',X1) \\/ ~memterm(X0,'_betweenObjRel') \\/ ~memterm(X4,'_betweenObjRel') /1/37/25/1/ 0pe [pp ns 7]\r\n" + 
				"% 24. sloterm('_a3','_orient','_westEast') /1/4/0/1/ 0pe [pp 9]\r\n" + 
				"% 25. prdtupterm('_a3','_betweenObjRel','_pacific','_usa','_atlantic') /1/6/0/1/ 0pe [pp 10]\r\n" + 
				"% 26. memterm('_a3','_betweenObjRel') /1/3/0/1/ 0pe [pp 11]\r\n" + 
				"% 28. sloterm('_a2','_orient','_northSouth') /1/4/0/1/ 0pe [pp 13]\r\n" + 
				"% 29. prdtupterm('_a2','_betweenObjRel','_canada','_usa','_mexico') /1/6/0/1/ 0pe [pp 14]\r\n" + 
				"% 30. memterm('_a2','_betweenObjRel') /1/3/0/1/ 0pe [pp 15]\r\n" + 
				"% * 31. ~sloterm(X0,'_west',X1) \\/ ~sloterm(X0,'_north',X2) \\/ ~sloterm(X0,'_east',X3) \\/ ~sloterm(X0,'_south',X4) | <<ans>>(\"?I= \",X0,\"?W= \",X1,\"?N= \",X2,\"?E= \",X3,\"?S= \",X4,\"?C= \",X5) \\/ ~prdtupterm(X0,'_compassRose',X5) \\/ ~memterm(X0,'_compassRose') /23/36/7/2/ 0pe [pp ns 16]\r\n" + 
				"% * 32. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') \\/ memterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_compassRose') /36/36/24/2/ 0pe [pp ns 17]\r\n" + 
				"% * 33. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ prdtupterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_compassRose',X2) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /37/37/25/2/ 0pe [pp ns 18]\r\n" + 
				"% * 34. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ sloterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_south',X3) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /37/37/25/2/ 0pe [pp ns 19]\r\n" + 
				"% * 35. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X4,'_orient','_northSouth') \\/ ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0(X5,X6,X1,X3,X2,X4,X0),'_east',X3) \\/ ~memterm(X0,'_betweenObjRel') \\/ ~memterm(X4,'_betweenObjRel') /37/37/25/2/ 0pe [pp ns 20]\r\n" + 
				"% * 36. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X0,'_orient','_northSouth') \\/ ~sloterm(X4,'_orient','_westEast') \\/ sloterm(vampireSkF0(X1,X3,X5,X6,X2,X0,X4),'_north',X1) \\/ ~memterm(X4,'_betweenObjRel') \\/ ~memterm(X0,'_betweenObjRel') /37/37/25/2/ 0pe [pp ns 21]\r\n" + 
				"% * 37. ~prdtupterm(X0,'_betweenObjRel',X1,X2,X3) \\/ ~prdtupterm(X4,'_betweenObjRel',X5,X2,X6) | ~sloterm(X4,'_orient','_northSouth') \\/ ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0(X5,X6,X1,X3,X2,X4,X0),'_west',X1) \\/ ~memterm(X0,'_betweenObjRel') \\/ ~memterm(X4,'_betweenObjRel') /37/37/25/2/ 0pe [pp ns 22]\r\n" + 
				"% * 39. sloterm('_a3','_orient','_westEast') /4/4/0/2/ 0pe vip [pp 24]\r\n" + 
				"% * 40. prdtupterm('_a3','_betweenObjRel','_pacific','_usa','_atlantic') /6/6/0/2/ 0pe [pp 25]\r\n" + 
				"% * 41. memterm('_a3','_betweenObjRel') /3/3/0/2/ 0pe vip [pp 26]\r\n" + 
				"% * 43. sloterm('_a2','_orient','_northSouth') /4/4/0/2/ 0pe vip [pp 28]\r\n" + 
				"% * 44. prdtupterm('_a2','_betweenObjRel','_canada','_usa','_mexico') /6/6/0/2/ 0pe [pp 29]\r\n" + 
				"% * 45. memterm('_a2','_betweenObjRel') /3/3/0/2/ 0pe vip [pp 30]\r\n" + 
				"% * 48. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_northSouth') \\/ sloterm(vampireSkF0(X1,X2,'_pacific','_atlantic','_usa',X0,'_a3'),'_west','_pacific') \\/ ~memterm(X0,'_betweenObjRel') /24/24/18/3/ 0pe vip [br fsr ns 39,41,40,37]\r\n" + 
				"% * 52. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0('_canada','_mexico',X1,X2,'_usa','_a2',X0),'_north','_canada') \\/ ~memterm(X0,'_betweenObjRel') /24/24/18/3/ 0pe vip [br fsr ns 43,45,44,36]\r\n" + 
				"% * 60. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_northSouth') \\/ sloterm(vampireSkF0(X1,X2,'_pacific','_atlantic','_usa',X0,'_a3'),'_east','_atlantic') \\/ ~memterm(X0,'_betweenObjRel') /24/24/18/3/ 0pe vip [br fsr ns 39,41,40,35]\r\n" + 
				"% * 64. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_westEast') \\/ sloterm(vampireSkF0('_canada','_mexico',X1,X2,'_usa','_a2',X0),'_south','_mexico') \\/ ~memterm(X0,'_betweenObjRel') /24/24/18/3/ 0pe vip [br fsr ns 43,45,44,34]\r\n" + 
				"% * 70. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_westEast') \\/ prdtupterm(vampireSkF0('_canada','_mexico',X1,X2,'_usa','_a2',X0),'_compassRose','_usa') \\/ ~memterm(X0,'_betweenObjRel') /24/24/18/3/ 0pe vip [br fsr ns 43,45,44,33]\r\n" + 
				"% * 76. ~prdtupterm(X0,'_betweenObjRel',X1,'_usa',X2) | ~sloterm(X0,'_orient','_westEast') \\/ ~memterm(X0,'_betweenObjRel') \\/ memterm(vampireSkF0('_canada','_mexico',X1,X2,'_usa','_a2',X0),'_compassRose') /23/23/17/3/ 0pe vip [br fsr ns 43,45,44,32]\r\n" + 
				"% * 83. sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_west','_pacific') /11/11/0/4/ 0pe vip [br fsr 43,45,44,48]\r\n" + 
				"% * 85. memterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose') /10/10/0/4/ 0pe vip [br fsr 39,41,40,76]\r\n" + 
				"% * 86. sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_north','_canada') /11/11/0/4/ 0pe vip [br fsr 39,41,40,52]\r\n" + 
				"% * 87. ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_west',X0) \\/ ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_east',X1) \\/ ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_south',X2) | <<ans>>(\"?I= \",vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),\"?W= \",X0,\"?N= \",'_canada',\"?E= \",X1,\"?S= \",X2,\"?C= \",X3) \\/ ~prdtupterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose',X3) /44/64/11/5/ 0pe vip [br fsr ns 85,31,86]\r\n" + 
				"% * 88. sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_east','_atlantic') /11/11/0/4/ 0pe vip [br fsr 43,45,44,60]\r\n" + 
				"% * 90. sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_south','_mexico') /11/11/0/4/ 0pe vip [br fsr 39,41,40,64]\r\n" + 
				"% * 93. prdtupterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose','_usa') /11/11/0/4/ 0pe vip [br fsr 39,41,40,70]\r\n" + 
				"% * 98. ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_east',X0) \\/ ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_south',X1) | <<ans>>(\"?I= \",vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),\"?W= \",'_pacific',\"?N= \",'_canada',\"?E= \",X0,\"?S= \",X1,\"?C= \",X2) \\/ ~prdtupterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose',X2) /33/53/11/6/ 0pe [br ns 83,87]\r\n" + 
				"% * 101. ~sloterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_south',X0) | <<ans>>(\"?I= \",vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),\"?W= \",'_pacific',\"?N= \",'_canada',\"?E= \",'_atlantic',\"?S= \",X0,\"?C= \",X1) \\/ ~prdtupterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose',X1) /22/42/11/7/ 0pe [br ns 88,98]\r\n" + 
				"% * 103. ~prdtupterm(vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),'_compassRose',X0) | <<ans>>(\"?I= \",vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),\"?W= \",'_pacific',\"?N= \",'_canada',\"?E= \",'_atlantic',\"?S= \",'_mexico',\"?C= \",X0) /11/31/0/8/ 0pe [br 90,101]\r\n" + 
				"% 104.  | <<ans>>(\"?I= \",vampireSkF0('_canada','_mexico','_pacific','_atlantic','_usa','_a2','_a3'),\"?W= \",'_pacific',\"?N= \",'_canada',\"?E= \",'_atlantic',\"?S= \",'_mexico',\"?C= \",'_usa') /1/20/0/9/ 0pe [br 93,103]\r\n" + 
				"%==============  End of proof. ==================\r\n" + 
				"% 1 answer found so far.\r\n" + 
				"% Resuming saturation in attempt to find another answer...\r\n" + 
				"% Resuming main loop...\r\n" + 
				"%================= Statistics: =======================\r\n" + 
				"% time 0 (0 current loop)\r\n" + 
				"% memory 1103Kb (1113Kb in chunks), total buffer deficit 0Kb\r\n" + 
				"%=== Generated clauses:\r\n" + 
				"%      total 147 (117 res + 0 eq + 30 other)\r\n" + 
				"%      forward subsumed 53\r\n" + 
				"%      eq.tautologies   0\r\n" + 
				"%      prop.tautologies 0\r\n" + 
				"%      simplified 53\r\n" + 
				"%         by FSR          53\r\n" + 
				"%         by demod.       0\r\n" + 
				"%               on splitting branches 0\r\n" + 
				"%         by built-in th. 0\r\n" + 
				"%         by eq. res.     0\r\n" + 
				"%      split 0  avg.components 1  diff.components 0\r\n" + 
				"%      useless:\r\n" + 
				"%        too deep inferences 0\r\n" + 
				"%        non-factorable answers 0\r\n" + 
				"%        func. in abstr. lit.   0\r\n" + 
				"%        too long abstr. lit. chains  0\r\n" + 
				"%        too heavy 0\r\n" + 
				"%        too heavy literals 0\r\n" + 
				"%        too long 0\r\n" + 
				"%        too deep 0\r\n" + 
				"%        too many variables 0\r\n" + 
				"%        refused allocation 0\r\n" + 
				"%=== Retained clauses:\r\n" + 
				"%      total 91\r\n" + 
				"%      used 78\r\n" + 
				"%      back subsumed 0\r\n" + 
				"%      back simplified 0\r\n" + 
				"%        on splitting branches 0\r\n" + 
				"%      rewritten by def. unfolding 0\r\n" + 
				"%      orphans murdered 0\r\n" + 
				"%               passive 0\r\n" + 
				"%               active  0\r\n" + 
				"%      recycled 0\r\n" + 
				"%      currently passive 0 (2147483648 reachable)\r\n" + 
				"%      reserved passive 0\r\n" + 
				"%      currently active 78\r\n" + 
				"%========== End of statistics. ======================\r\n" + 
				"% Proof not found. Empty passive.";
		Translator t = TranslatorFactory.createTranslator("tptp");
		t.translateKB("Document()");
		t.translateQuery("?I#_compassRose(?C _west->?W _north->?N _east->?E _south->?S)");
		QueryResult r = parseVPResult(
				new ByteArrayInputStream(input.getBytes()), Arrays.asList("QI", "QW", "QN", "QE", "QS", "QC"));
		r.inverseTranslate(t);
		println(r);
	}
}

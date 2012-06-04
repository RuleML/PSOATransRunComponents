package org.ruleml.psoa2tptp.translator;

import java.util.ArrayList;
import java.util.List;

public class PSOATranslatorUtil {
	public static final String Hash = "#";
	public static final String Period = ".";
	public static final String Comma = ", ";
	public static final String Amp = " & ";
	public static final String ImpliedBy = " <= ";
	public static final String Member = "member";
	public static final String TupTerm = "tupterm";
	public static final String SlotTerm = "sloterm";
	public static final String Hypothesis = "hypothesis";
	public static final String Axiom = "axiom";
	public static final String Conjecture = "conjecture";
	public static final String FOF = "fof";

	public static boolean isNull(Object o) 
	{ 
		return o==null;
	}

	public static int counter = 1;
	public static String randomOID() {
		return "int:oid"+counter++;
	}

	public static String getVarName(String var) {
		char c = var.charAt(1);
		if (Character.isUpperCase(c)) {
			return var.substring(1);
		}
		
		return String.valueOf(Character.toUpperCase(c)).concat(var.substring(2));
	}
	
	public static String getConstName(String con) {
		char c = con.charAt(0);
		if (Character.isLowerCase(c))
			return con;
		
		return String.valueOf(Character.toLowerCase(con.charAt(0))).concat(con.substring(1));
	}
	
	public static StringBuilder psoaStr(String type, List<String> tuples, List<String> slots) {
		return psoaStr(randomOID(), type, tuples, slots);
	}
	
	public static StringBuilder psoaStr(String oid, String type, List<String> tuples, List<String> slots) {
		if (oid == null) {
			oid = randomOID();
		}

		StringBuilder sb = member(oid,type);
		if (tuples.isEmpty() && slots.isEmpty()) {
			return sb;
		}
		
		for(String s : tuples) {
			sb.append(amp(tuple(oid, s)));
		}
		
		for (String t : slots) {
			sb.append(amp(slot(oid, t)));
		}
		
		return sb;
	}
	
	public static StringBuilder parenthesize(StringBuilder b) {		
		return b.insert(0, "(").append(")");
	}

	public static StringBuilder fofSentence(StringBuilder b) {
		return parenthesize(b).insert(0, FOF).append(Period);
	}
	
	public static StringBuilder member(String oid, String type) {
		return builder(Member).append(parenthesize(builder(oid).append(Comma).append(type)));
	}

	public static StringBuilder tuple(String oid, String term) {
		return builder(TupTerm).append(parenthesize(builder(oid).append(Comma).append(term)));
	}
	
	public static StringBuilder slot(String oid, String term) {
		return builder(SlotTerm).append(parenthesize(builder(oid).append(Comma).append(term)));
	}
	
	public static StringBuilder amp(StringBuilder b) {
		return b.insert(0, Amp);
	}

	public static StringBuilder existStr(StringBuilder varList, StringBuilder clause) {
		return varList.insert(0, "?[").append(']').append(parenthesize(clause));
	}
	
	public static StringBuilder allStr(StringBuilder b) {
		return b.insert(0, "![").append(']');
	}
	
	public static void collectTerm(StringBuilder b, String term) {
		if (b.length() > 0) {
			b.append(Comma).append(term);
		} else {
			b.append(term);
		}
	}
	
	public static void collectTerms(StringBuilder b, String...strings) {
		for(String s : strings) {
			collectTerm(b, s);
		}
	}
	
	public static StringBuilder fofFactSentence(StringBuilder b) {
		return fofSentence(builder(randomFactId()).append(Comma + Hypothesis + Comma).append(b));
	}
	
	public static StringBuilder fofRuleSentence(StringBuilder b) {
		return fofSentence(builder(randomRuleId()).append(Comma + Axiom + Comma).append(b));
	}

	public static StringBuilder fofConjSentence(StringBuilder b) {
		return fofSentence(builder(randomConjectureId()).append(Comma + Conjecture + Comma).append(b));
	}
	
	public static StringBuilder ruleStr(StringBuilder head, StringBuilder body) {
		return parenthesize(head).append(ImpliedBy).append(parenthesize(body));
	}
	
	public static int factCounter = 1;
	public static String randomFactId() {
		return "fact" + factCounter++;
	}
	
	public static int ruleCounter = 1;
	public static String randomRuleId() {
		return "rule" + ruleCounter++;
	}
	
	public static String randomConjectureId() {
		return "conj" + ruleCounter++;
	}
	
	public static StringBuilder builder() { return builder(""); }
	public static StringBuilder builder(String s) { return new StringBuilder(200).append(s); }
	
	public static <T> List<T> list() {
		return new ArrayList<T>();
	}
}

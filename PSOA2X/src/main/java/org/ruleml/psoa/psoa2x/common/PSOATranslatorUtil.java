package org.ruleml.psoa.psoa2x.common;

import java.util.*;

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
	private static final Map<String, String> builtInMap = new HashMap<String, String>(16); 
	
	static
	{
		builtInMap.put("pred:numeric-equal", "\'=:=\'");
		builtInMap.put("pred:numeric-not-equal", "\'=\\=\'");
		builtInMap.put("pred:numeric-greater-than", "\'>\'");
		builtInMap.put("pred:numeric-greater-than-or-equal", "\'>=\'");
		builtInMap.put("pred:numeric-less-than", "\'<\'");
		builtInMap.put("pred:numeric-less-than-or-equal", "\'=<\'");
		builtInMap.put("func:numeric-add", "\'+\'");
		builtInMap.put("func:numeric-subtract", "\'-\'");
		builtInMap.put("func:numeric-multiply", "\'*\'");
		builtInMap.put("func:numeric-divide", "\'/\'");
		builtInMap.put("func:numeric-integer-divide", "\'//'");
		builtInMap.put("func:numeric-mod", "mod");
	}
	
	public static boolean isNull(Object o) 
	{ 
		return o==null;
	}

	public static int counter = 1;
	public static String randomOID() {
		return "int:oid"+counter++;
	}

	public static String getVarName(String var) {
		return "Q".concat(var);
	}
	
	public static String getConstName(String con) {
		return "l".concat(con);
	}
	
	public static String getIRIConstName(String iri) {
		return builder("\'").append(iri).append("\'").toString();
	}
	
	public static void appendIRIConst(StringBuilder b, String iri)
	{
		String builtIn = buildInStr(iri);
		if (builtIn == null)
			b.append("\'").append(iri).append("\'");
		else
			b.append(builtIn);
	}
	
	public static String buildInStr(String iri)
	{
		return builtInMap.get(iri);
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
			sb.append(amp(tupterm(oid, s)));
		}
		
		for (String t : slots) {
			sb.append(amp(sloterm(oid, t)));
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

	public static StringBuilder tupterm(String oid, String terms) {
		return builder(TupTerm).append(parenthesize(builder(oid).append(Comma).append(terms)));
	}
	
	public static StringBuilder sloterm(String oid, String term) {
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
	   
	public static void append(StringBuilder b, String... strs)
	{
		for (String str : strs)
		{
			b.append(str);
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
	
	public static String inverseTranslateTerm(String term)
	{		
		// TODO: change to correct term translation 
		if (term.charAt(0) != 'Q')
		{
//			if (term.startsWith("'") && term.endsWith("'"))
//				return term.substring(1, term.length() - 1);
//			else
			return term;
		}
		else
			return '?' + term.substring(1);
	}
	
	public static StringBuilder builder() { return builder(""); }
	public static StringBuilder builder(String s) { return new StringBuilder(200).append(s); }
	
	public static <T> List<T> list() {
		return new ArrayList<T>();
	}
}

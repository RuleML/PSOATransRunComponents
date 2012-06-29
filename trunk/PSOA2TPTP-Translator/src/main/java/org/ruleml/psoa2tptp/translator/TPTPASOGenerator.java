package org.ruleml.psoa2tptp.translator;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

import logic.is.power.tptp_parser.*;
import logic.is.power.tptp_parser.SimpleTptpParserOutput.AnnotatedFormula;
import logic.is.power.tptp_parser.TptpParserOutput.*;

public class TPTPASOGenerator {
	private TptpParserOutput m_factory = new SimpleTptpParserOutput();
	private Map<String, String> m_varMap = new HashMap<String, String>();
	private static final String PREFIX_Var = "Q",
								PREFIX_Rulename = "ax",
								PREFIX_FactQueryname = "query",
								PREFIX_NonFactQueryname = "query",
								PRED_Member = "member",
								PRED_TupTerm = "tupterm",
								PRED_SlotTerm = "sloterm",
								PRED_Subclass = "subclass",
								PRED_Answer = "ans";
								
	private int m_numRules = 0;
	private static DecimalFormat m_numFormat = new DecimalFormat();
	
	static {
		m_numFormat.setMinimumIntegerDigits(2);
	}
	
	public TPTPASOGenerator()
	{
		this(new SimpleTptpParserOutput());
	}
	
	public TPTPASOGenerator(TptpParserOutput factory)
	{
		m_factory = factory;
	}
	
	public Term getConst(String name)
	{
		char firstChar = name.charAt(0), loweredChar = Character.toLowerCase(firstChar);
		if (loweredChar != firstChar)
		{
			String loweredCharStr = String.valueOf(loweredChar);
			if (name.length() > 1)
				name = loweredCharStr.concat(name.substring(1));
			else
				name = loweredCharStr;
		}
		
		return m_factory.createPlainTerm(name, null);
	}
	
	public String getVariableName(String varname)
	{
		String tptpVarName = m_varMap.get(varname);
		if (tptpVarName == null)
		{
			for (int i = varname.length() - 1; i >= 1; i--) {
				if (!Character.isLetterOrDigit(varname.charAt(i)))
					throw new TranslatorException("Name of variables can only consist of letters or variables");
			}
			tptpVarName = PREFIX_Var.concat(varname.substring(1));
			m_varMap.put(varname, tptpVarName);
		}
		
		return tptpVarName;
	}
	
	public Term getVariable(String varname)
	{
		return m_factory.createVariableTerm(getVariableName(varname));
	}
	
	public FofFormula getImplies(FofFormula head, FofFormula body)
	{
//		return m_factory.createBinaryFormula(head, BinaryConnective.ReverseImplication, body);
		return m_factory.createBinaryFormula(body, BinaryConnective.Implication, head);
	}
	
	public FofFormula getExist(Iterable<String> vars, FofFormula formula)
	{
		for (String var : vars) {
			if (m_varMap.remove(var) == null)
				throw new TranslatorException("Incorrect usage of variable " + var + " in the quantification of the formula " + formula);
		}
		return m_factory.createQuantifiedFormula(Quantifier.Exists, vars, formula);
	}
	
	public FofFormula getForAll(Iterable<String> vars, FofFormula formula)
	{
		for (String var : vars) {
			if (m_varMap.remove(var) == null)
				throw new TranslatorException("Incorrect usage of variable " + var + " in the quantification of the formula " + formula);
		}
		return m_factory.createQuantifiedFormula(Quantifier.ForAll, vars, formula);
	}
	
	private FofFormula getForAll(FofFormula formula)
	{
		FofFormula f = m_factory.createQuantifiedFormula(Quantifier.ForAll, m_varMap.values(), formula);
		m_varMap.clear();
		return f;
	}
	
	public FofFormula getAndFormula(FofFormula left, FofFormula right)
	{
		return m_factory.createBinaryFormula(left, BinaryConnective.And, right);
	}
	
	public FofFormula getOrFormula(FofFormula left, FofFormula right)
	{
		return m_factory.createBinaryFormula(left, BinaryConnective.Or, right);
	}
	
	public FofFormula getAtomicFormula(String pred, List<Term> args)
	{
		return m_factory.atomAsFormula(m_factory.createPlainAtom(pred, args));
	}
	
	private FofFormula getAndFormulaIfNotNull(FofFormula left, FofFormula right)
	{
		if (left == null)
			return right;
		
		return m_factory.createBinaryFormula(left, BinaryConnective.And, right);
	}
	
	public FofFormula getPSOAFormula(Term oid, Term classTerm, Set<? extends List<Term>> tuples, Set<? extends List<Term>> slots)
	{
		FofFormula f = null;
		ArrayList<Term> terms = new ArrayList<TptpParserOutput.Term>();
		
		if (classTerm != null)
		{
			terms.add(oid);
			terms.add(classTerm);
			
			f = getAtomicFormula(PRED_Member, terms);
		}
		
		for (List<Term> tuple : tuples) {
			terms.clear();
			terms.add(oid);
			terms.addAll(tuple);
			f = getAndFormulaIfNotNull(f, getAtomicFormula(PRED_TupTerm, terms));
		}
		
		for (List<Term> slot : slots) {
			terms.clear();
			terms.add(oid);
			terms.addAll(slot);
			f = getAndFormulaIfNotNull(f, getAtomicFormula(PRED_SlotTerm, terms));
		}
		
		if (f == null)
			throw new TranslatorException(oid + "Top() is not supported");
		
		return f;
	}
	
	public FofFormula getSubClassFormula(Term subClass, Term superClass)
	{
		List<Term> terms = new ArrayList<Term>();
		terms.add(subClass);
		terms.add(superClass);
		return getAtomicFormula(PRED_Subclass, terms);
	}
	
	public AnnotatedFormula getAnnotatedAxiom(FofFormula f)
	{
		if (!m_varMap.isEmpty())
		{
			throw new TranslatorException("Missing variable " +
								m_varMap.keySet().iterator().next() +
								" in the quantification of the formula " +
								f + ".");
		}
		
		String name = PREFIX_Rulename.concat(String.valueOf(m_numFormat.format(++m_numRules)));		
		return (AnnotatedFormula)m_factory.createFofAnnotated(name, FormulaRole.Axiom, f, null, 0);
	}
	
	public AnnotatedFormula getAnnotatedQuery(FofFormula f)
	{
		if (!m_varMap.isEmpty())
		{
			ArrayList<Term> terms = new ArrayList<Term>();
			for (Entry<String, String> varPair : m_varMap.entrySet()) {			
				terms.add(m_factory.createPlainTerm(dblQuoted(varPair.getKey().concat(" = ")), null));
				terms.add(m_factory.createVariableTerm(varPair.getValue()));
			}
			FofFormula conc = getPositionalAtom(PRED_Answer, terms);
			f = getForAll(getImplies(conc, f));
			
			return (AnnotatedFormula)m_factory.createFofAnnotated(PREFIX_NonFactQueryname, FormulaRole.Theorem, f, null, 0);
		}
		else
		{
			FofFormula conc = m_factory.atomAsFormula(m_factory.createPlainAtom(PRED_Answer, null));
			f = getImplies(conc, f);
			// PREFIX_Querynames.concat(String.valueOf(m_numFormat.format(++m_numQueries)));
			return (AnnotatedFormula)m_factory.createFofAnnotated(PREFIX_FactQueryname, FormulaRole.Theorem, f, null, 0);
		}
	}

	public FofFormula getPositionalAtom(String pred, Iterable<Term> args)
	{
		return m_factory.atomAsFormula(m_factory.createPlainAtom(pred, args));
	}
	
	private String quoted(String name) {
		return '\'' + name + '\'';
	}
	
	private String dblQuoted(String name) {
		return '\"' + name + '\"';
	}
}

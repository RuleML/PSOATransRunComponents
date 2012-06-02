package org.ruleml.psoa2tptp.translator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import logic.is.power.tptp_parser.*;
import logic.is.power.tptp_parser.SimpleTptpParserOutput.AnnotatedFormula;
import logic.is.power.tptp_parser.TptpParserOutput.*;

public class TPTPASOGenerator {
	private TptpParserOutput m_factory = new SimpleTptpParserOutput();
	private static final String PREFIX_Var = "V_",
								PREFIX_Rulenames = "ax",
								PREFIX_Querynames = "conj",
								PRED_Member = "member",
								PRED_TupTerm = "tupterm",
								PRED_SlotTerm = "sloterm",
								PRED_Subclass = "subclass";
	private static int m_numRules = 0, m_numQueries = 0;
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
		for (int i = varname.length() - 1; i >= 0; i--) {
			if (!Character.isLetterOrDigit(varname.charAt(i)))
				throw new TranslatorException("Name of variables can only consist of letters or variables");
		}
		
		return PREFIX_Var.concat(varname);
	}
	
	public Term getVariable(String varname)
	{		
		return m_factory.createVariableTerm(getVariableName(varname));
	}
	
	public FofFormula getImplies(FofFormula head, FofFormula body)
	{
		return m_factory.createBinaryFormula(head, BinaryConnective.ReverseImplication, body);
	}
	
	public FofFormula getExist(Iterable<String> vars, FofFormula formula)
	{
		return m_factory.createQuantifiedFormula(Quantifier.Exists, vars, formula);
	}
	
	public FofFormula getForAll(Iterable<String> vars, FofFormula formula)
	{
		return m_factory.createQuantifiedFormula(Quantifier.ForAll, vars, formula);
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
	
	public FofFormula getPSOAFormula(Term oid, Term classTerm, Set<? extends List<Term>> tuples, Set<? extends List<Term>> slots)
	{
		FofFormula f;
		ArrayList<Term> terms = new ArrayList<TptpParserOutput.Term>();
		terms.add(oid);
		terms.add(classTerm);
		
		f = getAtomicFormula(PRED_Member, terms);
		for (List<Term> tuple : tuples) {
			terms.clear();
			terms.add(oid);
			terms.addAll(tuple);
			f = getAndFormula(f, getAtomicFormula(PRED_TupTerm, terms));
		}
		
		for (List<Term> slot : slots) {
			terms.clear();
			terms.add(oid);
			terms.addAll(slot);
			f = getAndFormula(f, getAtomicFormula(PRED_SlotTerm, terms));
		}
		
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
		String name = PREFIX_Rulenames.concat(String.valueOf(m_numFormat.format(++m_numRules)));		
		return (AnnotatedFormula)m_factory.createFofAnnotated(name, FormulaRole.Axiom, f, null, 0);
	}
	
	public AnnotatedFormula getAnnotatedQuery(FofFormula f)
	{
		String name = PREFIX_Querynames.concat(String.valueOf(m_numFormat.format(++m_numQueries)));		
		return (AnnotatedFormula)m_factory.createFofAnnotated(name, FormulaRole.Conjecture, f, null, 0);
	}
}

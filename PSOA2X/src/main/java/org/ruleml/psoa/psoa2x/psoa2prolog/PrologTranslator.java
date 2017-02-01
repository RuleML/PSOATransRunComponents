package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.TreeNodeStream;
import org.ruleml.psoa.FreshNameGenerator;
import org.ruleml.psoa.PSOAInput;
import org.ruleml.psoa.PSOAKB;
import org.ruleml.psoa.PSOAQuery;
import org.ruleml.psoa.analyzer.KBInfoCollector;
import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.psoa2x.common.*;

public class PrologTranslator extends ANTLRBasedTranslator {
	PSOA2PrologConfig m_config;
	
	public PrologTranslator(PSOA2PrologConfig config)
	{
		m_config = config;
	}

	@Override
	protected <T extends PSOAInput<T>> T normalize(T input) {
		return input.LPnormalize(m_config);
	}
	
	@Override
	protected TranslatorWalker createTranslatorWalker(TreeNodeStream astNodes) {
		return new PSOA2PrologWalker(astNodes, m_config);
	}
	
/*	
	@Override
	protected CommonTreeNodeStream preprocess(CommonTreeNodeStream treeNodeStream, boolean isQuery) throws RecognitionException
	{
		Unnester unnester;
		DiscriminativeObjectifier objectifier;
		Skolemizer skolemizer;
		SlotTupributor slotTupributor;
		ExternalFlattener externalFlattener;
		ConjunctiveHeadSplitter splitter;
		QueryRewriter rewriter;
		TokenStream tokens = treeNodeStream.getTokenStream();
		
		if (isQuery)
		{			
			debugPrintln("Unnest Query");
			unnester = new Unnester(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(unnester.query().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			if (m_config.dynamicObjectification)
			{
				debugPrintln("Rewrite query");
				rewriter = new QueryRewriter(treeNodeStream, m_KBInfo);
				treeNodeStream = new CommonTreeNodeStream(rewriter.query().getTree());
				treeNodeStream.setTokenStream(tokens);
				debugPrintTree(treeNodeStream);
				
				objectifier = new DiscriminativeObjectifier(treeNodeStream, m_KBInfo);
				objectifier.setReducedObjectification(true);
			}
			else
				objectifier = new DiscriminativeObjectifier(treeNodeStream);
			
			debugPrintln("Objectify query");
			treeNodeStream = new CommonTreeNodeStream(objectifier.query().getTree());
			treeNodeStream.setTokenStream(tokens);	
			debugPrintTree(treeNodeStream);
			
			debugPrintln("Slotribute and tupribute query");
			slotTupributor = new SlotTupributor(treeNodeStream);
			slotTupributor.setReproduceClass(m_config.reproduceClass);
			treeNodeStream = new CommonTreeNodeStream(slotTupributor.query().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			debugPrintln("Flatten externals in query");
			externalFlattener = new ExternalFlattener(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(externalFlattener.query().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
		}
		else
		{
			FreshNameGenerator.reset();
			
			debugPrintln("Unnest KB");
			unnester = new Unnester(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(unnester.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			if (m_config.dynamicObjectification)
			{
				m_KBInfo = new KBInfoCollector(treeNodeStream);
				m_KBInfo.document().getTree();
				treeNodeStream.reset();
//				treeNodeStream = new CommonTreeNodeStream(objectifier.document().getTree());
				objectifier = new DiscriminativeObjectifier(treeNodeStream, m_KBInfo);
				objectifier.setReducedObjectification(true);
			}
			else
			{
				objectifier = new DiscriminativeObjectifier(treeNodeStream);
			}
			
			debugPrintln("Objectify KB");
			treeNodeStream = new CommonTreeNodeStream(objectifier.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			// Skolemization
			debugPrintln("Skolemize KB");
			skolemizer = new Skolemizer(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(skolemizer.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			debugPrintln("Slotribute and tupribute KB");
			slotTupributor = new SlotTupributor(treeNodeStream);
			slotTupributor.setReproduceClass(m_config.reproduceClass);
			treeNodeStream = new CommonTreeNodeStream(slotTupributor.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			debugPrintln("Flatten externals in KB");
			externalFlattener = new ExternalFlattener(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(externalFlattener.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
			debugPrintln("Split conjunctive-headed KB rules");
			splitter = new ConjunctiveHeadSplitter(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(splitter.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			debugPrintTree(treeNodeStream);
			
//			System.out.println("Finished normalizing KB.");
//			System.out.println("Normalized tree:");
//			printTree(treeNodeStream);
//			System.out.println();
		}
		
		return treeNodeStream;
	}
*/
	@Override
	public String inverseTranslateTerm(String term) {
		
//		if (queryVar.charAt(0) != 'Q')
//			throw new TranslatorException(queryVar + " is not a variable from a Prolog-translated PSOA KB");
		
//		return '?' + queryVar.substring(1);
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

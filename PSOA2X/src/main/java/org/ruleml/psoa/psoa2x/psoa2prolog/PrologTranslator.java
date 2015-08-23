package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.psoa.normalizer.*;
import org.ruleml.psoa.psoa2x.common.ANTLRBasedTranslator;
import org.ruleml.psoa.psoa2x.common.PSOATranslatorUtil;
import org.ruleml.psoa.psoa2x.common.TranslatorException;

public class PrologTranslator extends ANTLRBasedTranslator {
	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new PSOA2PrologWalker(astNodes);
	}
	
	@Override
	protected CommonTreeNodeStream preprocess(CommonTreeNodeStream treeNodeStream, boolean isQuery) throws RecognitionException
	{
		DiscriminativeObjectifier objectifier;
		Skolemizer skolemizer;
		SlotTupributor slotTupributor;
		ExternalFlattener externalFlattener;
		ConjunctiveHeadSplitter splitter;
		TokenStream tokens = treeNodeStream.getTokenStream();
		
		if (isQuery)
		{
//			System.out.println("Objectify Query");
			objectifier = new DiscriminativeObjectifier(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(objectifier.query().getTree());
			treeNodeStream.setTokenStream(tokens);			
			
//			System.out.println("Slotribute and Tupribute Query");
			slotTupributor = new SlotTupributor(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(slotTupributor.query().getTree());
			treeNodeStream.setTokenStream(tokens);
			
//			System.out.println("Flatten Externals for Query");
			externalFlattener = new ExternalFlattener(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(externalFlattener.query().getTree());
			treeNodeStream.setTokenStream(tokens);
			
//			System.out.println();
		}
		else
		{
//			System.out.println("Objectify KB");
			objectifier = new DiscriminativeObjectifier(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(objectifier.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			
			// Skolemization
			skolemizer = new Skolemizer(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(skolemizer.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			
//			System.out.println("Slotribute and Tupribute KB");
			slotTupributor = new SlotTupributor(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(slotTupributor.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			printTree(treeNodeStream);
			
//			System.out.println("Flatten Externals for KB");
			externalFlattener = new ExternalFlattener(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(externalFlattener.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			printTree(treeNodeStream);
			
//			System.out.println("Split Conjunctive Head for KB");
			splitter = new ConjunctiveHeadSplitter(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(splitter.document().getTree());
			treeNodeStream.setTokenStream(tokens);
			printTree(treeNodeStream);
			
//			System.out.println("Finished normalizing KB.");
//			System.out.println("Normalized tree:");
//			printTree(treeNodeStream);
//			System.out.println();
		}
		
		return treeNodeStream;
	}

	@Override
	public String inverseTranslateTerm(String term) {
		
//		if (queryVar.charAt(0) != 'Q')
//			throw new TranslatorException(queryVar + " is not a variable from a Prolog-translated PSOA KB");
		
//		return '?' + queryVar.substring(1);
		return PSOATranslatorUtil.inverseTranslateTerm(term);
	}
}

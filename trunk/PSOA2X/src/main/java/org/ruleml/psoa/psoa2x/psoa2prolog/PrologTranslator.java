package org.ruleml.psoa.psoa2x.psoa2prolog;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.ruleml.psoa.normalizer.*;
import org.ruleml.psoa.psoa2x.common.ANTLRBasedTranslator;

public class PrologTranslator extends ANTLRBasedTranslator {
	@Override
	protected TranslatorWalker createTranslatorWalker(
			CommonTreeNodeStream astNodes) {
		return new PSOA2PrologWalker(astNodes);
	}
	
	@Override
	protected CommonTreeNodeStream preprocess(CommonTreeNodeStream treeNodeStream, boolean isQuery) throws RecognitionException
	{
		OIDConstuctorObjectifier objectifier;
		SlotTupributor slotTupributor;
		ExternalFlattener externalFlattener;
		ConjunctiveHeadSplitter splitter;
		TokenStream tokens = treeNodeStream.getTokenStream();
		
		if (isQuery)
		{
//			System.out.println("Objectify Query");
			objectifier = new OIDConstuctorObjectifier(treeNodeStream);
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
			
			System.out.println();
		}
		else
		{
//			System.out.println("Objectify KB");
			objectifier = new OIDConstuctorObjectifier(treeNodeStream);
			treeNodeStream = new CommonTreeNodeStream(objectifier.document().getTree());
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
			System.out.println();
		}
		
		return treeNodeStream;
	}
}

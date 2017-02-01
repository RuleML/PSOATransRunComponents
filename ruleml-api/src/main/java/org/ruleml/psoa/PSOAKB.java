/* 
 * PSOA KB 
 * 
 * 
 * */

package org.ruleml.psoa;

import java.io.*;
import java.net.URL;
import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.analyzer.KBInfoCollector;
import org.ruleml.psoa.parser.PSOAPSParser;
import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.util.ANTLRTreeStreamConsumer;
import org.ruleml.psoa.x2psoa.*;

public class PSOAKB extends PSOAInput<PSOAKB>
{
	private KBInfoCollector m_kbInfo;
	private List<String[]> m_imports;
	private Set<String> m_localConsts;
	private Map<String, String> m_namespaceTable;
	
	public KBInfoCollector getKBInfo() {
		return m_kbInfo;
	}
	
	Map<String, String> getNamespaceTable()
	{
		return m_namespaceTable;
	}
	
	/**
	 * Load document from the given iri path and entailment 
	 * 
	 * @param iri
	 * @param entailment
	 * @throws IOException
	 */
	public void load(String iri, String entailment) throws IOException
	{
		URL url = new URL(iri);
		final InputStream psoaIn;
		
		if (iri.endsWith(".psoa"))
		{
			psoaIn = url.openStream();
		}
		else 
		{
			X2PSOA translator;
			
			if (iri.endsWith(".n3") || iri.endsWith(".nt") || iri.endsWith(".ttl"))
			{
				if (entailment == null)
				{
					throw new IllegalArgumentException("Missing entailment for file " + iri);
				}					
				else if (!entailment.equals("http://www.w3.org/ns/entailment/Simple"))
				{
					throw new IllegalArgumentException("Unsupported entailment for file " + iri);
				}
				
				translator = new RDF2PSOA();
			}
			else
			{
				throw new IllegalArgumentException("Unsupported input format");
			}
			
			try (PipedOutputStream psoaOut = new PipedOutputStream()) {
				// translate and load may be separated in different threads
				psoaIn = new PipedInputStream(psoaOut, 10485760);
				translator.translate(url.openStream(), psoaOut);
			}
		}
			
		load(psoaIn);
		psoaIn.close();
	}
	
	public PSOAKB loadImports() throws IOException {
		if (m_imports.isEmpty())
			return this;
		
		Set<String> importedDocURLs = new HashSet<String>();
		List<PSOAKB> kbs = new ArrayList<PSOAKB>();
		Queue<String[]> importQueue = new LinkedList<String[]>(m_imports);
		String[] importDoc;
		
		while ((importDoc = importQueue.poll()) != null)
		{
			String iri = importDoc[0], entail = importDoc[1];

			// Imported IRIs are stored in the set importedDocURLs to avoid being 
			// imported again, thus avoiding the need for 
			if (importedDocURLs.add(iri))
			{
				
				PSOAKB kb = new PSOAKB();
				kb.load(iri, entail);
				importQueue.addAll(kb.m_imports);
				kbs.add(kb);
			}
		}
		
		return merge(kbs);
	}

	@Override
	protected ParserRuleReturnScope parse(PSOAPSParser parser) throws RecognitionException {
		ParserRuleReturnScope ret = parser.document();
		m_imports = parser.getImports();
		m_localConsts = parser.getLocalConsts();
		m_namespaceTable = parser.getNamespaceTable();
		return ret;
	}
	
	/**
	 * Merge the current KB with a collection of kbs. Local constants in kbs are renamed. 
	 * 
	 * @param kbs KBs to be merged with the current KB
	 * @return The merged KB
	 */
	public PSOAKB merge(List<PSOAKB> kbs) {
		Tree root = (Tree) m_adaptor.nil();
		root.addChild(m_tree);
		for (PSOAKB kb : kbs) {
			// Rename every KB to be merged except the current KB
			root.addChild(kb.rename(m_localConsts).getTree());
		}
		m_tree = root;
		return transform("merging", stream -> (new Concatenater(stream)).documents());
	}
	
	/**
	 * Concatenate the current KB with a collection of kbs
	 * 
	 * @param kbs KBs to be concatenated with the current KB
	 * @return The concatenated KB
	 */
	public PSOAKB concatenate(Collection<PSOAKB> kbs)
	{
		Tree root = (Tree) m_adaptor.nil();
		root.addChild(m_tree);
		for (PSOAKB kb : kbs) {
			// No renaming
			root.addChild(kb.getTree());
		}
		m_tree = root;
		return transform("concatenation", stream -> (new Concatenater(stream)).document());
	}
	
	/**
	 * Rename local constants in a KB
	 * 
	 * @param excludedNames The names that are disallowed to be used as new constant names
	 * @return Renamed KB
	 */
	public PSOAKB rename(Set<String> excludedNames)
	{
		return transform("renaming", stream -> {
			Renamer renamer = new Renamer(stream);
			renamer.setExcluded(excludedNames);
			return renamer.document();
		});
	}
	
	public PSOAKB unnest()
	{
		return transform("unnesting", stream -> (new Unnester(stream)).document());
	}
	
	public PSOAKB objectify(boolean differentiate, boolean dynamic)
	{
		return transform("objectification", stream -> {
			if (dynamic)
			{
				m_kbInfo = new KBInfoCollector(stream);
				m_kbInfo.document().getTree();
				stream.reset();
			}
			
			if (differentiate)
			{	
				DifferentiateObjectifier objectifier = new DifferentiateObjectifier(stream);
				objectifier.setDynamic(dynamic, m_kbInfo);
				return objectifier.document();
			}
			else
			{
				ExistObjectifier objectifier = new ExistObjectifier(stream);
				return objectifier.document();
			}
		});
	}
	
	public PSOAKB rewriteSubclass()
	{
		return this;
	}
	
	public PSOAKB skolemize()
	{
		return transform("Skolemization", stream -> (new Skolemizer(stream)).document());
	}
	
	public PSOAKB slotTupribute(boolean reproduceClass)
	{
		return transform("slotribution/tupribution", stream -> {
			SlotTupributor slotTupributor = new SlotTupributor(stream);
			slotTupributor.setReproduceClass(reproduceClass);
			return slotTupributor.document();			
		});
	}
	
	public PSOAKB flatten()
	{
		return transform("flattening", stream -> (new ExternalFlattener(stream)).document());
	}
	
	public PSOAKB splitConjunctiveConclusion()
	{
		return transform("conclusion splitting", stream -> (new ConjunctiveHeadSplitter(stream)).document());
	}
	
	@Override
	public PSOAKB transform(String name, ANTLRTreeStreamConsumer actor, boolean newKBInst)
	{
		try
		{
			if (newKBInst)
			{
				/* TODO: Create new instance */
				return null;
			}
			else
			{
				m_tree = (Tree) actor.apply(getTreeNodeStream()).getTree();
				if (m_printAfterTransformation)
				{
					m_printStream.println(String.format("After %s :", name));
					printTree();
				}
				return this;
			}
		}
		catch (RecognitionException e)
		{
			String msg = String.format("Failed to parse input for %s transformation", name);
			throw new PSOATransformerException(msg, e);
		}
	}

	@Override
	public PSOAKB FOLnormalize(RelationalTransformerConfig config) {
		return unnest().
			   rewriteSubclass().
			   objectify(config.differentiateObj, config.dynamicObj).
			   slotTupribute(config.reproduceClass);
	}

	@Override
	public PSOAKB LPnormalize(RelationalTransformerConfig config) {
		return FOLnormalize(config).
			   skolemize().
			   flatten().
			   splitConjunctiveConclusion();
	}
}

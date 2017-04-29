/* 
 * Java class for operating PSOA queries.
 * 
 * */

package org.ruleml.psoa;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.ruleml.psoa.parser.PSOAPSParser;
import org.ruleml.psoa.transformer.*;
import org.ruleml.psoa.util.ANTLRTreeStreamConsumer;

public class PSOAQuery extends PSOAInput<PSOAQuery>
{
	private PSOAKB m_kb;
	
	public PSOAQuery() {
		m_kb = null;
	}
	
	public PSOAQuery(PSOAKB kb) {
		m_kb = kb;
	}
	
	@Override
	protected ParserRuleReturnScope parse(PSOAPSParser parser) throws RecognitionException {
		return parser.query(m_kb.getNamespaceTable());
	}
	
	public PSOAQuery rename()
	{
		return transform("renaming", stream -> (new Renamer(stream)).query());
	}
	
	public PSOAQuery unnest()
	{
		return transform("unnesting", stream -> (new Unnester(stream)).query());
	}
	
	public PSOAQuery objectify(boolean differentiate, boolean dynamic)
	{
		PSOAQuery q;
		
		if (dynamic)
		{
			q = transform("dynamic objectification", stream -> (new QueryRewriter(stream, m_kb.getKBInfo())).query());
		}
		else
			q = this;
		
		return q.transform("static objectification", stream -> {			
			if (differentiate)
			{
				DifferentiateObjectifier objectifier = new DifferentiateObjectifier(stream);
				objectifier.setDynamic(dynamic, m_kb.getKBInfo());
				return objectifier.query();
			}
			else
			{
				ExistObjectifier objectifier = new ExistObjectifier(stream);
				return objectifier.query();
			}
		});
	}
	
	public PSOAQuery slotTupribute(boolean reproduceClass)
	{
		return transform("slotribution/tupribution", stream -> {
			SlotTupributor slotTupributor = new SlotTupributor(stream);
			slotTupributor.setReproduceClass(reproduceClass);
			return slotTupributor.query();			
		});
	}
	
	public PSOAQuery flatten()
	{
		return transform("flattening", stream -> (new ExternalFlattener(stream)).query());
	}
	
	@Override
	public PSOAQuery transform(String name, ANTLRTreeStreamConsumer actor, boolean newKBInst)
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
	
	
	/**
	 * Perform FOL-targeting normalization of a PSOA query
	 * 
	 * @param config   transformer configuration
	 * 
	 * @return the FOL-normalized PSOA query
	 * 
	 * */
	@Override
	public PSOAQuery FOLnormalize(RelationalTransformerConfig config) {
		return unnest().
			   objectify(config.differentiateObj, config.dynamicObj).
			   slotTupribute(config.reproduceClass);
	}

	
	/**
	 * Perform LP-targeting normalization of a PSOA query
	 * 
	 * @param config   transformer configuration
	 * 
	 * @return   the LP-normalized PSOA query
	 * 
	 * */
	@Override
	public PSOAQuery LPnormalize(RelationalTransformerConfig config) {
		return FOLnormalize(config).
			   flatten();
	}
}

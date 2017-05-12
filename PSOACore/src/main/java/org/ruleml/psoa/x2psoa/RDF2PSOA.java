package org.ruleml.psoa.x2psoa;

import java.io.*;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.*;
import org.eclipse.rdf4j.rio.helpers.*;
import org.eclipse.rdf4j.model.vocabulary.*;

/**
 * Class for translating RDF/N3 KBs to PSOA presentation syntax.
 * 
 **/
public class RDF2PSOA implements X2PSOA {
	private static ValueFactory factory = SimpleValueFactory.getInstance();
	private PrintStream m_output;
	
	private ParserConfig getConfig() {
		ParserConfig config = new ParserConfig();
		config.set(BasicParserSettings.PRESERVE_BNODE_IDS, true);
		return config;
	}
	
	@Override
	public void translate(InputStream input, OutputStream output) throws IOException {
		Model results = Rio.parse(input, "", RDFFormat.N3, getConfig(), factory, new ParseErrorLogger());
		m_output = new PrintStream(output);
		
		m_output.println("Document (");
		m_output.println("  Group (");
		for (Statement s : results)
		{
			m_output.print("    ");
			convert(s);
			m_output.println();
		}
		m_output.println("  )");
		m_output.println(")");
	}
	
	public void convert(Statement s)
	{
		convert(s.getSubject());
		IRI prop = s.getPredicate(); 
		if (prop.equals(RDF.TYPE))
		{
			m_output.print("#");
			convert(s.getObject());
		}
		else if (prop.equals(RDFS.SUBCLASSOF))
		{
			m_output.print("##");
			convert(s.getObject());
		}
		else
		{
			m_output.print("#Top(");
			convert(s.getPredicate());
			m_output.print("->");
			convert(s.getObject());
			m_output.print(")");
		}
	}
	
	public void convert(Value v)
	{
		if (v instanceof IRI)
			convert((IRI)v);
		else if (v instanceof Literal)
			convert((Literal)v);
		else if (v instanceof BNode)
			convert((BNode)v);
		else
			throw new IllegalArgumentException(v.stringValue());
	}
	
	public void convert(IRI iri)
	{
		m_output.print("<");
		m_output.print(iri);
		m_output.print(">");
	}
	
	public void convert(BNode b)
	{
		m_output.print("_");
		m_output.print(b.getID());
	}
	
	public void convert(Literal s)
	{
		m_output.print(s);
	}
}

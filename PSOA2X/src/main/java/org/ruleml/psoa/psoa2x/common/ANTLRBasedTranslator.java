package org.ruleml.psoa.psoa2x.common;

import static org.ruleml.psoa.utils.IOUtil.*;

import java.io.*;
import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.ruleml.psoa.*;

public abstract class ANTLRBasedTranslator extends Translator {
	abstract protected Converter createTranslatorWalker(TreeNodeStream astNodes);
	abstract protected <T extends PSOAInput<T>> T normalize(T input);
	protected PSOAKB m_kb;
	protected Converter m_queryConverter;

	/**
	 * Translate the input KB and write the outcome into an output stream 
	 * 
	 * @param kb   KB input string
	 * @param out   translation output stream
	 * 
	 * */
	@Override
	public void translateKB(String kb, OutputStream out) throws TranslatorException {
		m_kb = new PSOAKB();
		m_kb.loadFromText(kb);
		translateKB(m_kb, out);
	}


	/**
	 * Translate the input KB and write the outcome into an output stream 
	 * 
	 * @param kb   KB input stream
	 * @param out   translation output stream
	 * 
	 * */
	@Override
	public void translateKB(InputStream kb, OutputStream out) throws TranslatorException {
		try {
			FreshNameGenerator.reset();
			m_kb = new PSOAKB();
			m_kb.load(kb);
			translateKB(m_kb, out);
		} catch (IOException e) {
			throw new TranslatorException(e);
		}
	}


	/**
	 * Translate the input KB and write the outcome into an output stream 
	 * 
	 * @param kb   input PSOAKB object 
	 * @param out   translation output stream
	 * 
	 * */
	public void translateKB(PSOAKB kb, OutputStream out) throws TranslatorException {
		try {
			kb.loadImports();
			kb.setPrintAfterTransformation(debugMode);
			TreeNodeStream stream = normalize(kb).getTreeNodeStream();
			Converter converter = createTranslatorWalker(stream);
			converter.setOutputStream(getPrintStream(out));
			converter.document();
		} catch (RecognitionException | RewriteCardinalityException | IOException e) {
			throw new TranslatorException(e);
		}
	}
	
	/**
	 * Translate the input query and write the outcome into an output stream. 
	 * The translation assumes the query is posed to the most recent KB 
	 * translated by the translator object.
	 * 
	 * @param query   input query string
	 * @param out   translation output stream
	 * 
	 * */
	@Override
	public void translateQuery(String query, OutputStream out) throws TranslatorException {
		PSOAQuery psoaquery = new PSOAQuery(m_kb);
		psoaquery.loadFromText(query);
		translateQuery(psoaquery, out);
	}
	
	/**
	 * Translate the input query and write the outcome into an output stream. 
	 * The translation assumes the query is posed to the most recent KB 
	 * translated by the translator object.
	 * 
	 * @param query   input query stream
	 * @param out   translation output stream
	 * 
	 * */
	@Override
	public void translateQuery(InputStream query, OutputStream out) throws TranslatorException {
		try {
			PSOAQuery psoaquery = new PSOAQuery(m_kb);
			psoaquery.load(query);
			translateQuery(psoaquery, out);
		} catch (IOException e) {
			throw new TranslatorException(e);
		}
	}
	
	/**
	 * Translate the input query and write the outcome into an output stream. 
	 * The translation assumes the query is posed to the most recent KB 
	 * translated by the translator object.
	 * 
	 * @param query   input PSOAQuery object
	 * @param out   translation output stream
	 * 
	 * */
	public void translateQuery(PSOAQuery query, OutputStream out) {
		try {
			FreshNameGenerator.resetVarGen();
			query.setPrintAfterTransformation(debugMode);
			TreeNodeStream stream = normalize(query).getTreeNodeStream();
			m_queryConverter = createTranslatorWalker(stream);
			m_queryConverter.setOutputStream(getPrintStream(out));
			m_queryVarMap = m_queryConverter.query();
		} catch (RecognitionException | RewriteCardinalityException e) {
			throw new TranslatorException(e);
		}
	}
	
	@Override
	public String inverseTranslateTerm(String term) {
		return m_queryConverter.inverseTranslateTerm(term);
	}
	
	public static abstract class Converter extends TreeParser {
		protected PrintStream m_outStream = System.out;
		protected Map<String, String> m_queryVarMap = new HashMap<String, String>();
		protected StringBuilder m_buffer = new StringBuilder(256);
		private static SortedSet<BufferIndex> s_index = new TreeSet<BufferIndex>();
		
		public Converter(TreeNodeStream input,
				RecognizerSharedState state) {
			super(input, state);
		}
		
		public void setOutputStream(PrintStream out)
		{
			m_outStream = getPrintStream(out);
		}
		
		protected void flush()
		{
			if (!s_index.isEmpty())
				throw new TranslatorException("Active buffer indices must be discarded before flushing.");
			m_outStream.print(m_buffer);
			m_buffer.setLength(0);
		}
		
		protected void flushln()
		{
			flush();
			m_outStream.println();
		}
		
		protected BufferIndex getBufferIndex()
		{
			BufferIndex index = new BufferIndex(m_buffer.length());
			s_index.add(index);
			return index;
		}
		
		protected void replace(BufferIndex startIndex, int offset, String s) {
			int end = startIndex.value + offset;
			m_buffer.replace(startIndex.value, end, s);
			
			BufferIndex endIndex = new BufferIndex(end);
			for(BufferIndex index :
				  s_index.subSet(new BufferIndex(startIndex.value + 1), endIndex))
			{
				index.value = m_buffer.length();
			}
			
			int indOffset = s.length() - offset;
			if (indOffset != 0)
			{	
				for(BufferIndex index : s_index.tailSet(endIndex))
				{
					index.offset(indOffset);
				}
			}
		}
		
		protected void replace(BufferIndex startIndex, String s) {
			int end = m_buffer.length();
			m_buffer.replace(startIndex.value, end, s);
			for(BufferIndex index : s_index.tailSet(new BufferIndex(startIndex.value + 1)))
			{
				index.value = end;
			}
		}
		
		protected void trimEnd(int len)
		{
			int newLen = m_buffer.length() - len;
			BufferIndex endIndex = new BufferIndex(newLen);
			for(BufferIndex index : s_index.tailSet(endIndex))
			{
				index.value = newLen;
			}
			
			m_buffer.setLength(newLen);
		}
		
		/**
		 * Append a string to buffer
		 * 
		 * @param str   the string to send
		 * */
		protected void append(String str)
		{
			m_buffer.append(str);
		}

		
		/**
		 * Append strings to buffer
		 * 
		 * @param strs   the strings to send
		 * 
		 * */
		protected void append(String... strs) {
			for (String str : strs)
			{
				m_buffer.append(str);
			}
	    }

		
		/**
		 * Append string representations of objects to buffer
		 * 
		 * @param objs   the objects to send
		 * 
		 * */
		protected void append(Object... objs) {
			for (Object obj : objs)
			{
				m_buffer.append(obj);
			}
	    }
		
		protected void print(Object obj) {
	        m_outStream.print(obj);
	    }
		
		protected void print(String str) {
	        m_outStream.print(str);
	    }
		
		protected void print(Object... objs) {
			for (Object obj : objs)
			{
		        m_outStream.print(obj);
			}
	    }
		
		protected void print(String... strs) {
			for (String s : strs)
			{
		        m_outStream.print(s);
			}
	    }
		
	    protected void println(Object obj) {
	        m_outStream.println(obj);
	    }
		
		protected void println(Object... objs) {
			for (Object obj : objs)
			{
		        m_outStream.print(obj);
			}
			println();
	    }
		
		protected void println(String... strs) {
			for (String s : strs)
			{
		        m_outStream.print(s);
			}
			println();
	    }
	    
	    protected void println() {
	        m_outStream.println();
	    }

	    public static class BufferIndex implements Comparable<BufferIndex> {
	    	private int value;
	    	
	    	public BufferIndex(int index)
	    	{
	    		this.value = index;
	    	}
	    	
	    	public void offset(int x)
	    	{
	    		value += x;
	    	}

	    	public void dispose()
	    	{
	    		s_index.remove(this);
	    	}
	    	
			@Override
			public int compareTo(BufferIndex o) {
				return Integer.compare(this.value, o.value);
			}
	    }
	    
	    /**
	     * Translate KB 
	     * 
	     * */
		abstract public void document() throws RecognitionException;
		
	    /**
	     * Translate query 
	     * 
	     * @return   a map from the original free query variables to 
	     * variables in the target language
	     * 
	     * */
		abstract public Map<String, String> query() throws RecognitionException;
		
		/**
		 * Inverse translation of terms
		 * 
		 * @param term   a term in the target language
		 * @return   the translated term in PSOA
		 * 
		 * */
		abstract public String inverseTranslateTerm(String term);
	}
}

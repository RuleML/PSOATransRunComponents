package org.ruleml.psoa.transformer;

public class PSOATransformerException extends PSOARuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6897279569333436927L;
	
	public PSOATransformerException()
	{
		super();
	}
	
	public PSOATransformerException(String msg)
	{
		super(msg);
	}
	
	public PSOATransformerException(Throwable t)
	{
		super(t);
	}
	
	public PSOATransformerException(String msg, Throwable t)
	{
		super(msg, t);
	}
}

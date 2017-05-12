package org.ruleml.psoa.psoatransrun;

public class PSOATransRunException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1221646925809401590L;

	public PSOATransRunException()
	{
		super();
	}
	
	public PSOATransRunException(String msg)
	{
		super(msg);
	}
	
	public PSOATransRunException(Exception e)
	{
		super(e);
	}
	
	public PSOATransRunException(String msg, Exception e)
	{
		super(msg, e);
	}
}

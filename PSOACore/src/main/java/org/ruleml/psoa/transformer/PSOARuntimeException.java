package org.ruleml.psoa.transformer;

public class PSOARuntimeException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1777570871047732114L;

	/**
	 * 
	 */
	
	public PSOARuntimeException()
	{
		super();
	}
	
	public PSOARuntimeException(String msg)
	{
		super(msg);
	}
	
	public PSOARuntimeException(Throwable t)
	{
		super(t);
	}
	
	public PSOARuntimeException(String msg, Throwable t)
	{
		super(msg, t);
	}
}

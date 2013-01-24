package org.ruleml.psoa2tptp.restful.resources;

public class PSOATransRunException extends RuntimeException
{
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

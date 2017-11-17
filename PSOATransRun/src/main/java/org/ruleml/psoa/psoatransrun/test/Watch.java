package org.ruleml.psoa.psoatransrun.test;

public class Watch {
	private String m_name;
	private long m_startTime, m_totalTime = 0;
	private int m_invocations = 0;
	
	public Watch(String name)
	{
		this(name, false);
	}
	
	public Watch(String name, boolean startWatch)
	{
		m_name = name;
		if (startWatch)
			start();
	}
	
	public void start()
	{
		m_startTime = System.nanoTime();
	}
	
	public void stop()
	{
		long elapsed = System.nanoTime() - m_startTime;
		m_totalTime += elapsed;
		m_invocations++;
		if (m_startTime < 0)
			throw new WatchException("Watch \"" + m_name + "\" has not started.");
		m_startTime = -1;
	}
	
	public void reset()
	{
		m_startTime = -1;
		m_totalTime = 0;
		m_invocations = 0;
	}
	
//	@Override
//	public String toString()
//	{
//		if (m_startTime > 0)
//			throw new WatchException("Watch \"" + m_name + "\" has not stopped.");
//		
//		StringBuilder builder = new StringBuilder();
//		Formatter f = new Formatter(builder);
//		long[] comps = getTimeComponents();
//		f.format(formatString, m_name, m_invocations, comps[0], comps[1], comps[2], comps[3], totalTime() / (double)m_invocations);
//		return builder.toString();
//	}
	
	private class WatchException extends RuntimeException {
		private static final long serialVersionUID = -3916834874458076265L;

		public WatchException(String message)
		{
			super(message);
		}
	}
	
	public long totalMilliSeconds()
	{
		return m_totalTime / 1000000;
	}
	
	public long totalMicroSeconds()
	{
		return m_totalTime / 1000;
	}
	
	public long avgTimeInMS()
	{
		return Math.round(totalMilliSeconds() / (double) m_invocations);
	}
}


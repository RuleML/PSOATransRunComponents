package org.ruleml.psoatransrun.utils;

import java.io.*;

public class IOUtil
{
	private final static File tempDir;
	
	static
	{
		String tempDirPath = System.getProperty("java.io.tmpdir").concat("/PSOATransRun");
		tempDir = new File(tempDirPath);
		tempDir.mkdir();
		tempDir.deleteOnExit();
	}
	
	public static File tmpFile(String prefix)
	{
		return tmpFile(prefix, null);
	}
	
	public static File tmpFile(String prefix, String suffix)
	{
		try
		{
			return File.createTempFile(prefix, suffix, tempDir);
		}
		catch (IOException e)
		{
			throw runtimeIOException(e);
		}
	}
	
	public static void writeStringToFile(File f, String content)
	{
		PrintWriter w = writer(f);
		w.print(content);
		w.close();
	}
	
	public static File tmpFileWithContent(String prefix, String suffix, String content)
	{
		File f = tmpFile(prefix, suffix);
		writeStringToFile(f, content);
		return f;
	}
	
	public static File extractFromResource(ClassLoader loader, String resource)
	{
		File f = new File(tempDir, resource);
		InputStream in = loader.getResourceAsStream(resource);
		
		try
		{
			try
			{
				FileOutputStream out = new FileOutputStream(f, false);
				try
				{
					byte[] buffer = new byte[4096];
					int len;
					
					while ((len = in.read(buffer)) != -1)
					{
						out.write(buffer, 0, len);
					}
					
					f.deleteOnExit();
					return f;
				}
				finally
				{
					out.close();
				}
			}
			finally
			{
				in.close();
			}
		}
		catch (IOException e)
		{
			throw runtimeIOException(e);
		}
	}
		
	public static PrintWriter writer(File f) {
		try
		{
			return new PrintWriter(f);
		}
		catch (IOException e)
		{
			throw runtimeIOException(e);
		}
	}
	
	public static PSOATransRunException runtimeIOException(IOException e)
	{
		return new PSOATransRunException("I/O Error", e);
	}
}

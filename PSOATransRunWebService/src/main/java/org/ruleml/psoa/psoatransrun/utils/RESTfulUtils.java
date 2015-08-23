package org.ruleml.psoa.psoatransrun.utils;

import static java.util.Arrays.asList;
import static org.ruleml.psoa.psoatransrun.utils.Collections.*;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

public class RESTfulUtils {
	public static List<String> deserialize(String output) {
		return asList(output.split("\n"));
	}

	public static String serialize(List<String> sentences) {
		StringBuilder builder = new StringBuilder(sentences.size() * 60);
		for (String s : sentences) {
			builder.append(s).append('\n');
		}
		return builder.toString();
	}
	
	public static String encode(String s) {
		try
		{
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decode(String s){
		if (s == null)
			return null;
		
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(RESTfulUtils.class).error("Failed to UTF-8 encode string.", e);
			return s;
		}
	}
	
	protected static Map<String,String> params(UriInfo info) {
		Map<String,String> params = map();
		for (Entry<String, String> e : flatten(rawParams(info))) {
			params.put(e.getKey().toUpperCase(), e.getValue());
		}
		return params;
	}
	
	private static MultivaluedMap<String,String> rawParams(UriInfo info) {
		return info.getQueryParameters();
	}
	
	private static Set<Entry<String,String>> flatten(MultivaluedMap<String, String> multi) {
		Map<String,String> m = map();
		for (Entry<String,List<String>> e : multi.entrySet()) {
			m.put(e.getKey(),first(e.getValue()));
		}
		return m.entrySet();
	}
	
	protected static String query(UriInfo info){
		return "";
	}
	
	protected static InputStream byteStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
	
	protected static byte[] bytes(String s) {
		return s.getBytes();
	}

	protected static OutputStream out() {
		return new ByteArrayOutputStream();
	}
}

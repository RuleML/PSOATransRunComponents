package org.ruleml.psoa.psoatransrun.utils;

import java.util.*;

public enum Collections {
;

	public static <T> List<T> list() {
		return new ArrayList<T>();
	}
	
	public static <T> List<T> list(Collection<T> c) {
		return new ArrayList<T>(c);
	}
	
	public static <T> Set<T> set() {
		return new HashSet<T>();
	}
	
	public static <K,V> Map<K,V> map() {
		return new HashMap<K, V>();
	}
	
	public static <K,V> boolean has(Map<K,V> m, K k) {
		return m.containsKey(k);
	}
	
	public static <T> T first(Collection<T> coll) {
		try {
			return coll.iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public static <K,V> V get(Map<K,V> m, K k) {
		return m.get(k);
	}
}

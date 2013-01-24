package org.ruleml.psoa2tptp.restful.resources;

import java.util.*;

enum Collections {
;

	protected static <T> List<T> list() {
		return new ArrayList<T>();
	}
	
	protected static <T> List<T> list(Collection<T> c) {
		return new ArrayList<T>(c);
	}
	
	protected static <T> Set<T> set() {
		return new HashSet<T>();
	}
	
	protected static <K,V> Map<K,V> map() {
		return new HashMap<K, V>();
	}
	
	protected static <K,V> boolean has(Map<K,V> m, K k) {
		return m.containsKey(k);
	}
	
	protected static <T> T first(Collection<T> coll) {
		try {
			return coll.iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	protected static <K,V> V get(Map<K,V> m, K k) {
		return m.get(k);
	}
}

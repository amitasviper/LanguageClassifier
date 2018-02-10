package com.mavenhive.query;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Query Object
 * @author Amith
 *
 */
public class Query {
	LinkedHashMap<String, Double> queryWords;
	private String queryName;

	public Query(String queryName, String[] entry) {
		this.queryName = queryName;
		queryWords = new LinkedHashMap<String, Double>();
		for (int i = 1; i < entry.length; i++) {
			queryWords.put(entry[i], Math.pow(2, (entry.length - i)));
		}
	}

	@Override
	public String toString() {
		return queryName;
	}

	public Set<Entry<String, Double>> getEntrySet() {
		return queryWords.entrySet();
	}

}

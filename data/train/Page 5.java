package com.mavenhive.pages;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mavenhive.index.Indexable;

/**
 * Bean to store the pages
 * @author Amith
 *
 */
public class Page implements Indexable {
	private LinkedHashMap<String, Double> keyWords;
	private String pageName;
	private String[] entry;

	public Page(String pageName, String[] entry, Map<String, Double> keyWords) {
		this.pageName = pageName;
		this.entry = entry;
		this.keyWords = (LinkedHashMap<String, Double>) keyWords;
	}
	
	public String[] getText() {
		return entry;
	}

	public Double getKeyValue(String key) {
		if (!keyWords.containsKey(key)) {
			return 0D;
		} else {
			return keyWords.get(key);
		}
	}

	@Override
	public String toString() {
		return pageName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageName == null) ? 0 : pageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (pageName == null) {
			if (other.pageName != null)
				return false;
		} else if (!pageName.equals(other.pageName))
			return false;
		return true;
	}

	public Set<Entry<String, Double>> getIterator() {
		return keyWords.entrySet();
	}

}

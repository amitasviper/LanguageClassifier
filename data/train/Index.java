package com.mavenhive.index;

import java.util.ArrayList;
import java.util.TreeMap;

import com.mavenhive.pages.Page;

public class Index {
	private TreeMap<String, ArrayList<Page>> index;
	private IRank ranker;
	public Index(IRank ranker) {
		this.ranker = ranker;
		index = new TreeMap<String, ArrayList<Page>>();
	}

	public void add(String key, Page value) {
		if (index.get(key) == null) {
			ArrayList<Page> list = new ArrayList<Page>();
			list.add(value);
			index.put(key, list);
		} else {
			ranker.rank(index.get(key), value, key);
		}
	}
	
	public ArrayList<Page> get(String key) {
		return index.get(key);
	}
}

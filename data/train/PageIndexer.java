package com.mavenhive.indexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.mavenhive.index.IRank;
import com.mavenhive.index.Index;
import com.mavenhive.pages.Page;
import com.mavenhive.pages.PageRank;
import com.mavenhive.query.ISearch;
import com.mavenhive.query.Query;

/**
 * Index to index the page objects
 * @author Amith
 *
 */
public class PageIndexer implements IIndexer, ISearch {
	private Index index;

	public PageIndexer(IRank ranker) {
		this.index = new Index(ranker);
	}
	
	public PageIndexer(Index index) {
		this.index = index;
	}

	public void index(Page page) {
		Set<Entry<String, Double>> iterator = page.getIterator();
		for (Entry<String, Double> entry : iterator) {
			String key = entry.getKey();
			index.add(key, page);
		}
	}

	@Override
	public String toString() {
		return "PageIndexer [index=" + index + "]";
	}

	public String search(Query query) {
		String result = query + " :";
		HashMap<Page, PageRank> rankedPages = new HashMap<Page, PageRank>();
		Set<Entry<String, Double>> queryWords = query.getEntrySet();
		for (Entry<String, Double> q : queryWords) {
			double rank = 0;
			ArrayList<Page> pages = index.get(q.getKey());
			if (pages == null) {
				return result;
			}
			for (Page page : pages) {
				rank = page.getKeyValue(q.getKey()) * q.getValue();
				if (rankedPages.get(page) == null)
					rankedPages.put(page, new PageRank(page, rank));
				else {
					rankedPages.get(page).bumpWeight(rank);
				}
			}
		}
		ArrayList<PageRank> values = new ArrayList<PageRank>(rankedPages.values());
		Collections.sort(values, new Comparator<PageRank>() {
			public int compare(PageRank p1, PageRank p2) {
				return p2.getRank().compareTo(p1.getRank());
			}
		});
		for (PageRank pageRank : values) {
			result += pageRank + " ";
		}
		return result;
	}

}

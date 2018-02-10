package com.mavenhive.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.mavenhive.pages.Page;


/**
 * Performs ranking if the pages based on weight and stores all pages 
 * according to weight. 
 * @author Amith
 *
 */
public class SimplePageRanker implements IRank {

	public void rank(ArrayList<Page> pages, Page page, final String key) {
		if (pages.contains(page))
			return;
		pages.add(page);
		Collections.sort(pages, new Comparator<Page>() {
			public int compare(Page p1, Page p2) {
				return p2.getKeyValue(key).compareTo(p1.getKeyValue(key));
			}
		});
	}

}

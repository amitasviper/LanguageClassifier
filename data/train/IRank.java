package com.mavenhive.index;

import java.util.ArrayList;

import com.mavenhive.pages.Page;

public interface IRank {
	/**
	 * Method to rank pages for a particular key.
	 * Pages will contain all the pages ranked in the decided order.
	 */
	void rank(ArrayList<Page> pages, Page page, final String key);
}

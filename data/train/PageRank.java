package com.mavenhive.pages;

/**
 * Bean to associate rank with page
 * @author Amith
 *
 */
public class PageRank {

	private double rank;
	private Page page;

	public PageRank(Page page, double rank) {
		super();
		this.page = page;
		this.rank = rank;
	}

	public Double getRank() {
		return rank;
	}

	public Page getPage() {
		return page;
	}

	@Override
	public String toString() {
		return page.toString();
	}

	public void bumpWeight(double weight) {
		this.rank += weight;
	}
}

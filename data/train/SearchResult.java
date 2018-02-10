package com.saurav;

/**
 * Created by Saurav Kumar on 5/3/2017.
 */
public class SearchResult implements Comparable {

    private Integer weight;

    private String pageTitle;

    @Override
    public int compareTo(Object o) {
        SearchResult searchResult = (SearchResult)o;
        if(this.weight.equals(searchResult.getWeight())) {
            return this.pageTitle.compareTo(searchResult.getPageTitle());
        }
        return this.weight > searchResult.getWeight() ? -1 : 1;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
}

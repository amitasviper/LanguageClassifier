package com.saurav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Saurav Kumar on 5/3/2017.
 */
public class Page {
    private List<Page> childPages = new ArrayList<>();
    private Map<String, Integer> keywords = new HashMap<>();
    private Boolean isDummy = true;
    private String pageTitle;

    public Boolean getIsDummy() {
        return isDummy;
    }

    public Page setIsDummy(Boolean dummy) {
        isDummy = dummy;
        return this;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public List<Page> getChildPages() {
        return childPages;
    }

    public void setChildPages(List<Page> childPages) {
        this.childPages = childPages;
    }

    public Map<String, Integer> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, Integer> keywords) {
        this.keywords = keywords;
    }

}

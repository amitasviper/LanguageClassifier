package com.saurav;

import java.util.*;

/**
 * Created by Saurav Kumar on 5/3/2017.
 */
public class Query {

    Map<String, Integer> keywords = new HashMap<>();

    public Map<String, Integer> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, Integer> keywords) {
        this.keywords = keywords;
    }

    public void execute(List<Page> pages, List<SearchResult> searchResults ){
        for(Page page: pages){
            Integer totalWeight = 0;
            for (Map.Entry<String, Integer> pageKeyWord : page.getKeywords().entrySet()) {
                for (Map.Entry<String, Integer> queryKeyWord : this.getKeywords().entrySet()) {
                    if(queryKeyWord.getKey().equals(pageKeyWord.getKey())){
                        totalWeight += queryKeyWord.getValue() * pageKeyWord.getValue();
                    }
                }
            }
            if(totalWeight > 0){
                SearchResult searchResult = new SearchResult();
                searchResult.setPageTitle(page.getPageTitle());
                searchResult.setWeight(totalWeight);
                searchResults.add(searchResult);
            }
            if(page.getChildPages().size() > 0){
                execute(page.getChildPages(), searchResults);
            }
        }
    }
}

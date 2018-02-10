package com.saurav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static final Integer MAX_WEIGHT = 8;

    public static void main(String[] args) {
        QueryAdapter queryAdapter = new QueryAdapter();
        //Get queries as list
        List<Query> queries = queryAdapter.getQueries("./data/queries.txt");
        int counter = 0;
        for(Query query: queries){
            //Create a synchronizedList so that multiple threads can populate search results parallely
            List<SearchResult> searchResults = Collections.synchronizedList(new ArrayList<SearchResult>());
            //Create two threads because we have two data sources (files) each having 3 pages
            Thread thread1 = new Thread(new SearchWorker(searchResults, query, "./data/pages_1.txt"), "T-1");
            Thread thread2 = new Thread(new SearchWorker(searchResults, query, "./data/pages_2.txt"), "T-2");
            thread1.start();
            thread2.start();
            try {
                //Join the threads so that parent thread can wait for the result
                thread1.join();
                thread2.join();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            //Sort the search results by their weight
            Collections.sort(searchResults);
            //Print results
            System.out.print("Q" + (++counter) + ": ");
            for(SearchResult searchResult: searchResults){
                System.out.print(searchResult.getPageTitle() + " ");
            }
            System.out.println();
        }
    }
}

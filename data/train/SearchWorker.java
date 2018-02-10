package com.saurav;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Saurav Kumar on 5/3/2017.
 */
public class SearchWorker implements Runnable {

    private List<SearchResult> searchResults;
    private static final int MAX_PAGES_TO_PROCESS = 1;
    private Query query;
    private String filePath;
    private ArrayBlockingQueue<Page> blockingQueue = null;


    public SearchWorker(List<SearchResult> searchResults, Query query, String filePath){
        this.searchResults = searchResults;
        this.query = query;
        this.filePath = filePath;
        this.blockingQueue = new ArrayBlockingQueue<>(MAX_PAGES_TO_PROCESS);
    }

    @Override
    public void run() {
        //Create data adapter thread which will read page data from file and it will put the data in the queue
        //until the queue reaches the threshold size.
        Thread adapterThread = new Thread(new PageAdapter(this.blockingQueue, this.filePath), "AT");
        adapterThread.start();
        try {
            List<Page> pages = new ArrayList<>();
            while(true){
                //Read the Page data from queue once the queue reaches the threshold size
                Page page = blockingQueue.take();
                if(page.getIsDummy()){
                    //Reached end of data from file
                    query.execute(pages, this.searchResults);
                    break;
                }
                pages.add(page);
                //To save memory process data in batches
                if(pages.size() == MAX_PAGES_TO_PROCESS){
                    query.execute(pages, this.searchResults);
                    pages.clear();
                }
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

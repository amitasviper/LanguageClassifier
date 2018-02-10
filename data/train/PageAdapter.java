package com.saurav;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static com.saurav.Main.MAX_WEIGHT;

/**
 * Created by Saurav Kumar on 5/4/2017.
 */
public class PageAdapter implements Runnable {

    private BlockingQueue<Page> blockingQueue = null;
    private String filePath;

    public PageAdapter(BlockingQueue<Page> blockingQueue, String filePath){
        this.blockingQueue = blockingQueue;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line =null;
            //Read page data line by line and prpare list of page objects
            while((line=br.readLine())!=null){
                Integer keyWordWeight = MAX_WEIGHT;
                String[] values = line.split(" ");
                Page page = new Page();
                page.setIsDummy(false);
                page.setPageTitle(values[0]);
                for(int i = 1; i < values.length; i++){
                    page.getKeywords().put(values[i], keyWordWeight--);
                }
                blockingQueue.put(page);
            }
            //Put a dummy data in the queue so that the consumer thread can know if it has scanned all the pages
            blockingQueue.put((new Page()).setIsDummy(true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

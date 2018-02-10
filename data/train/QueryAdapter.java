package com.saurav;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static com.saurav.Main.MAX_WEIGHT;

/**
 * Created by Saurav Kumar on 5/4/2017.
 */
public class QueryAdapter {
    public List<Query> getQueries(String filePath){
        List<Query> queries = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            for(String line; (line = br.readLine()) != null; ) {
                String[] values = line.split(" ");
                Query query = new Query();
                Integer keyWordWeight = MAX_WEIGHT;
                for(int i = 1; i < values.length; i++){
                    query.getKeywords().put(values[i], keyWordWeight--);
                }
                query.getKeywords().remove(0);
                queries.add(query);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return queries;
    }
}

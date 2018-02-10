package javaapplication6;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import KeywordSearch.Page;
import KeywordSearch.Query;
import KeywordSearch.Weight;

public class QueryTest 
{
    @Test
        public void testassignPageWeights()
        {
            List<String> pageKeywords=new ArrayList<>(Arrays.asList("ford","car"));
            Page page = new Page();
            page.setKeywords(pageKeywords);
            List<String> queryKeywords=new ArrayList<>(Arrays.asList("car"));
            Query query = new Query();
            query.setKeywords(queryKeywords);      
            final int pageNumber = 2;
            Weight weight = query.assignPageWeights(page, pageNumber);
            assertEquals(pageNumber, weight.getPageNumber());
            assertEquals(56.0, weight.getTotalWeight(), 0.0); 
        }
        
    @Test
        public void testCalculateWeightForValidData()
        {
            List<String> pageKeywords=new ArrayList<>(Arrays.asList("ford","car")); 
            List<String> queryKeywords=new ArrayList<>(Arrays.asList("car"));
            Query query = new Query();
            double totalWeight=query.calculateWeight(queryKeywords,pageKeywords);
            assertEquals(56.0,totalWeight,0.0);
        }
        
    @Test
        public void testCalculateWeightForInvalidData()
        {
            List<String> pageKeywords=new ArrayList<>(Arrays.asList("ford","car")); 
            List<String> queryKeywords=new ArrayList<>(Arrays.asList("toyota"));
            Query query = new Query();
            double totalWeight=query.calculateWeight(queryKeywords,pageKeywords);
            assertEquals(0.0,totalWeight,0.0);
        }  
        
    @Test
        public void testCalculateWeightEmptyData()
        {
            List<String> pageKeywords=new ArrayList<>(Arrays.asList("ford","car")); 
            List<String> queryKeywords=new ArrayList<>(Arrays.asList("")); 
            Query query = new Query();
            double totalWeight=query.calculateWeight(queryKeywords,pageKeywords);
            assertEquals(0.0,totalWeight,0.0);
        }
}

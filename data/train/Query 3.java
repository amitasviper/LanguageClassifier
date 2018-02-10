package KeywordSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Query 
{
    public static final int MAXIMUM_NUMBER_OF_KEYWORDS = 8;
    List<String> keywords=new ArrayList<>();
    
    public List<String> getKeywords()
    {
       return keywords;
    }
    
    public void setKeywords(List<String>keywords)
    {
       this.keywords=keywords;
    }
    
    public double calculateWeight(List<String> queryKeywords, List<String> pageKeywords)
    {
        int product;
        double totalWeight=0.0;
        for(int i=0;i<queryKeywords.size();i++)
        {
            product=0;
                for(int j=0;j<pageKeywords.size();j++)
                {
                   if(queryKeywords.get(i).equalsIgnoreCase(pageKeywords.get(j)))
                   {
                   product=(MAXIMUM_NUMBER_OF_KEYWORDS-i)*(MAXIMUM_NUMBER_OF_KEYWORDS-j);
                   break;
                   }
                }
            totalWeight=totalWeight+product;
        }
        return totalWeight;
    }
    
    public Weight assignPageWeights(Page page,int pageNumber)
    {
       Weight weight=new Weight();
       double totalWeight=0.0;
       totalWeight=calculateWeight(this.getKeywords(), page.getKeywords());
       weight.setTotalWeight(totalWeight);
       weight.setPageNumber(pageNumber);
       return weight;
    }
    
}

package KeywordSearch;

import static KeywordSearch.Search.pageslist;
import static KeywordSearch.Search.querylist;
import static KeywordSearch.Search.weight;
import java.util.*;

public class Weight implements Comparable <Weight>
{
    private double total;
    private int pageNumber;
    
    public double getTotalWeight()
    {
        return total;
    }
    
    public void setTotalWeight(double total)
    {
        this.total=total;
    }
    
    public int getPageNumber()
    {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber)
    {
        this.pageNumber=pageNumber;
    }
      
   @Override
    public int compareTo(Weight o)
    {
        double getWeight=o.getTotalWeight();
        if(getWeight-this.getTotalWeight()==0)
        {
            return 0;
        }
        else if(getWeight-this.getTotalWeight()<0)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
    
    public List<Weight> createWeightList(int i)
    {   
        List<Weight> weightList=new ArrayList<Weight>();
        try
        {
            for(int j=0;j<pageslist.size();j++)
            {
                weight=querylist.get(i).assignPageWeights(pageslist.get(j),j);
                weightList.add(weight);
            }
        }
        catch(NullPointerException e)
        {
            System.out.print(e);
        }
        return weightList;
    }
} 
    


package KeywordSearch;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

public class Search
{
   public static final String PAGE = "P";
   public static final String QUERY = "Q";
   public static final String INPUTFILE = "Input.txt";
  
   public static ArrayList<Page> pageslist = new ArrayList<Page>();
   public static ArrayList<Query> querylist = new ArrayList<Query>();
   public static Weight weight;
    
   public static List<String> retrieveKeywords(String[] keys,List<String> keywordsList)
    {
       for(String key:keys)
       {
            if(!key.equalsIgnoreCase(PAGE)|| !key.equalsIgnoreCase(QUERY))
            {
              keywordsList.add(key);
            }
       }
       return keywordsList;
    }
    
    public static void displaySortedWeights()
    {   
        try
        {
        for(int i=0;i<querylist.size();i++)
        {
            List<Weight> weightList=new Weight().createWeightList(i);
            Collections.sort(weightList);
            display(i,weightList);
        }
        }
        catch(NullPointerException e)
        {
            System.out.print(e);
        }
    }
    
    public static void display(int i,List<Weight> wtobjects)
    {
        System.out.print("Q"+(i+1)+":");
        for(int k=0;k<5 && k<wtobjects.size() && wtobjects.get(k).getTotalWeight()!=0;k++)
        {
            System.out.print("P"+(wtobjects.get(k).getPageNumber()+1)+"\t");
        }
        System.out.println();   
    }
    
    public static void main(String[] args) throws IOException 
    {
        String line;
        try
        {
            BufferedReader in =new BufferedReader(new FileReader(INPUTFILE));
            ArrayList<String> lines = new ArrayList<String>();
            while((line=in.readLine())!=null)
            {
               lines.add(line);
            }
            int numberoflines= lines.size();
            for(int i=0;i<numberoflines;i++)
            {
                try
                {                    
                    List<String> keywordsList = new ArrayList<String>();
                    List<String> keywords= new ArrayList<String>();
                    String[] keys=lines.get(i).split("\\s");
                    switch(keys[0])
                    {
                        case PAGE:
                                Page newPage=new Page();
                                keywords=retrieveKeywords(keys,keywordsList);
                                newPage.setKeywords(keywords);
                                pageslist.add(newPage);
                                break;
                        
                        case QUERY:
                                Query newquery=new Query();
                                keywords=retrieveKeywords(keys,keywordsList);
                                newquery.setKeywords(keywords);
                                querylist.add(newquery);
                                break;
                        default:System.out.println("Invalid input");
                    }
                      
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                   System.out.println(e);
                }
            }
           
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        displaySortedWeights();
    }
}
          
        
    
    

      


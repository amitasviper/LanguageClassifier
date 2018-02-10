package KeywordSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Page 
{
    List<String>keywords=new ArrayList<>();
  
    public List<String> getKeywords()
    {
        return keywords;
    }
    
    public void setKeywords(List<String> keywords)
    {
        this.keywords=keywords;
    }
}

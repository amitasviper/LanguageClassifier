package searchengine;
import java.util.*;

public class SearchEngine {
    private ArrayList page = new ArrayList();
    private ArrayList query = new ArrayList();
    public void setPage(String page_val)
    {
        page.add(page_val);
    }
    public void setQuery(String queries)
    {
        query.add(queries);
    }
       
    
    //This function checks each pages for each query
    
    public int[][] check()
    {
        String[] q,p;
        Scanner st = new Scanner(System.in);
        System.out.println("Enter the value of N: ");
        int N = st.nextInt();
        int s,M=N;
        
        //A Multidimensional array to store the values of calculation
        int[][] res = new int[query.size()][page.size()];    
        
        //Take each query at a time
        for(int i=0;i<query.size();i++)
        {
            //Split each query from their widespaces
            q = query.get(i).toString().split("\\s");
            
            //Take each page at a time
            for(int j=0;j<page.size();j++)
            {
              s=0;   //sum
              N=M;   
              
              //Split each page from widespaces 
              p = page.get(j).toString().split("\\s");
              
              //Now taking single splitted query at a time to compare with single splitted page 
              for(int m=0;m<q.length;m++)
              {                  
                  for(int n=0;n<p.length;n++)
                  {
                      //if nth letter of page matches with mth letter of query
                      if(p[n].toUpperCase().equals(q[m].toUpperCase()))
                      {
                          s+= (M-n)*N;  //For 1st query word N=8 and 2nd N=7 and so on
                      }                 //M-n=8 if query matched with first page word and M-n=7 and so on.
                  }
                 N--;  
              }
              
              res[i][j]=s;
              
            }
        }
        return res;
    }
    
    
    //This function prints the output
    public void printOutput(int[][] res)
    {
        int [][]org_arr= new int[res.length][res[0].length];    //To store the original array
        int [][]ind= new int[res.length][res[0].length];       //To store the index of original array
        for(int i=0;i<org_arr.length;i++)
        {
            for(int j=0;j<org_arr[i].length;j++)
                org_arr[i][j]=res[i][j];
        }
        
        //Array is sorted
        for(int i=0;i<res.length;i++)
        {
            Arrays.sort(res[i]);
        }
       
       
        int cnt;        //counter variable
        
        //Sorted array is checked with unsorted array to track the index postion of each elements
        for(int i=0;i<res.length;i++)
        {
            cnt=0;
            for(int j=res[i].length-1;j>=0;j--)
            {                    
                for(int n=0;n<org_arr[i].length;n++)
                    {
                        
                        if(res[i][j]==org_arr[i][n])
                        {
                            //If zero found on sorted array place -1 in index
                            if(res[i][j]==0)
                            {
                                ind[i][cnt]=-1;
                                cnt++;
                                break;
                            }
                            else
                            {
                                //When the match is found replace the element with -1 in unsorted array
                                //so the duplicacy is eliminated
                                ind[i][cnt]=n;
                                org_arr[i][n]=-1;
                                cnt++;
                                break;
                               
                            }
                        }
                    }
            }
        }
        
        //It prints the array
        for(int i=0;i<ind.length;i++)
        {
            System.out.print("Q"+(i+1)+": ");
            for(int j=0;j<ind[i].length;j++)
            {
                if(ind[i][j]>=0)
                {
                    System.out.print("P"+(ind[i][j]+1)+" ");
                }
            }
            System.out.println();
        }
        
    }
    
    
    //Main Function Starts here
    public static void main(String[] args) {
        String val;
        char response='o';
        Scanner sc = new Scanner(System.in);
        SearchEngine se = new SearchEngine();
        do{
            System.out.println("Enter the values:\n");
            val=sc.nextLine();
            if("P".equals(val.substring(0,1).toUpperCase()))     //if the input is page
                se.setPage(val.substring(2, val.length()));
            else if("Q".equals(val.substring(0,1).toUpperCase()))                                              //if the input is query
                se.setQuery(val.substring(2, val.length()));
            else
                System.out.println("Invalid Input, Please try again");
            System.out.print("Continue? [y/n]: ");
            response = (char) sc.nextLine().charAt(0);;

        }while(response != 'n');
        
        int[][] res = se.check();           //calls the check function to perform the calculations
        
        se.printOutput(res);                //calls the function to print the output
    }
    
}

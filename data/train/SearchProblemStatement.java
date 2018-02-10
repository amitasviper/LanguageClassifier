import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchProblemStatement {

	 static int N=8; //Maximum number of keywords 
	
	public static void main(String[] args) {
		
		
		String[][] pages={{"Amit", "Kumar", "Yadav"}, {"Yadav"}, {"Sanjay", "Singhania", "Ram"}};
		String queries[][]={{"Kumar", "Yadav"}, {"Singhania"}, {"Yadav", "Ram"}};
		
		//Iterating for each query against each page.
		for(int i=0;i<queries.length;i++){
		SearchProblemStatement.calculatePageQueryRelationshipAndPrint(pages, queries[i],i+1);
		}
	}
	
	
	//It calculates the relationship between Page and Query and prints the expected solution.
	public static void calculatePageQueryRelationshipAndPrint(String[][] pages, String queries[],int queryNumber){
		
		Map<String,Integer> result=new HashMap<String,Integer>();//contains mapping of each page with query. 
		int totalWeight = 0,iteratingQuery;
		
		for(int iteratingPages=0;iteratingPages < pages.length;iteratingPages++){
			
			List<String> pagesList=Arrays.asList(pages[iteratingPages]);
				
			for( iteratingQuery=0;iteratingQuery < queries.length;iteratingQuery++){
					String query=queries[iteratingQuery];
					if(pagesList.stream().filter(s -> s.equalsIgnoreCase(query)).findFirst().isPresent())
				totalWeight=totalWeight+((N - pagesList.indexOf(queries[iteratingQuery])) * (N - iteratingQuery));
				
			}
			if(totalWeight != 0 )
			result.put("P"+(iteratingPages+1), new Integer(totalWeight));
			
			//resetting values
			pagesList=null;
			totalWeight=0;
		}
		
		
		//printing the solution
		System.out.print("Q"+queryNumber+": ");
		result.entrySet().stream().sorted((firstItem, secondItem) -> secondItem.getValue().compareTo(firstItem.getValue())).limit(5).forEach(k -> System.out.print(k.getKey() + " "));
		System.out.println();
		}
		
		
	}

Code Review comments for `Gulshan Vasnani`, `DECISION` => `REJECT`
```
Pros:
1. Code Works and gives expected output

Cons:
1. Input is hardcoded
2. Not defined a single class. Not identified the entities like Pages, Query etc
3. Entire solution is a sigle function
4. Not modular. Cannot extend the solution easliy
5. No Readme
6. Not used Git
```





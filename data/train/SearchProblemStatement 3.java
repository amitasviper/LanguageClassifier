import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchProblemStatement {

	 static int N=8; //Maximum number of keywords 
	
	public static void main(String[] args) {
		
		
		String[][] pages={{"Ford","Car","Review"},{"Review","Car"},{"Review","Ford"},{"Toyota","Car"},{"Honda","Car"},{"Car"}};
		String queries[][]={{"Ford"},{"Car"},{"Review"},{"Ford","Review"},{"Ford","Car"},{"cooking","French"}};
		
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





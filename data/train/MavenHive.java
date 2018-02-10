import java.util.*;
import java.io.*;

class Maven{

	/*
	//MERGE SORT [ O(n(logn)) ]: --------**********-----------------
	 ArrayList<Integer> merge(ArrayList<Integer> arr, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        // ArrayList<Integer> L = new ArrayList<Integer>();
        // ArrayList<Integer> R = new ArrayList<Integer>();
        //int R[] = new int [n2];
 
        /*Copy data to temp arrays*/
        // for (int i=0; i<n1; ++i)
            // L.add(arr.get(l + i));
        // for (int j=0; j<n2; ++j)
            // R.add(arr.get(m + 1+ j));
 
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        // int i = 0, j = 0;
 
        // Initial index of merged subarry array
        // int k = l;
        // while (i < n1 && j < n2)
        // {
            // if (L.get(i) <= R.get(j))
            // {
                // arr.set(k, L.get(i));
                // i++;
            // }
            // else
            // {
                // arr.set(k, R.get(j));
                // j++;
            // }
            // k++;
        // }
 
        /* Copy remaining elements of L[] if any */
        // while (i < n1)
        // {
            // arr.set(k, L.get(i));
            // i++;
            // k++;
        // }
 
        /* Copy remaining elements of R[] if any */
        // while (j < n2)
        // {
            // arr.set(k, R.get(j));
            // j++;
            // k++;
        // }

        // return arr;
    // }
 
    // Main function that sorts arr[l..r] using
    // merge()
    // ArrayList<Integer> sort(ArrayList<Integer> arr, int l, int r)
    // {
        // if (l < r)
        // {
            // Find the middle point
            // int m = (l+r)/2;
 
            // Sort first and second halves
            // sort(arr, l, m);
            // sort(arr , m+1, r);
 
            // Merge the sorted halves
            // merge(arr, l, m, r);
        // }
        // return arr;
    // }*/
    // MERGE SORT ends -----------*************-------------

    // IndexSorting begins ----------***********----------
    ArrayList<Integer> indexSorting(ArrayList<Integer> pSum){
    	ArrayList<Integer> pSumIndex = new ArrayList<Integer>(pSum); // may need to be new ArrayList(nfit)
    	ArrayList<Integer> pSum1 = new ArrayList<Integer>(pSum);
		Collections.sort(pSum1);
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		// int[] indexes = new int[pSum.size()];
		for (int n = pSum1.size()-1; n >= 0 ; n--){

    		indexes.add(pSumIndex.indexOf(pSum1.get(n)));
    		pSumIndex.set(pSumIndex.indexOf(pSum1.get(n)), -1);

		}
		return indexes;
    }


	int getIndex(String str, String substring)
	{
  		return Arrays.asList(str.split("\\s+")).indexOf(substring);
	}

	// -------****** CALCULATION FUNCTION [ O(n^3) ] ******--------
	ArrayList<ArrayList<String>> calcStrength(ArrayList<String> p, ArrayList<String> q, int n)
	{
		
		
		ArrayList<ArrayList<String>> qPages = new ArrayList<ArrayList<String>>();

		for(int i=0; i<q.size(); i++){
			ArrayList<String> eachQPage = new ArrayList<String>();
			ArrayList<Integer> pSum = new ArrayList<Integer>();
			ArrayList<Integer> pSumIndex = new ArrayList<Integer>();
			List<String> eachQuery = Arrays.asList((q.get(i)).split("\\s+"));
			// System.out.println("---Each Query---");
			// for(int t=0; t<eachQuery.size(); t++){
				// System.out.print(eachQuery.get(t)+" ");
			// }
			// System.out.println();

			for(int k=0; k<p.size(); k++){
				int sum = 0;
				
				for(int j=1; j<eachQuery.size(); j++){
					int index = getIndex(p.get(k), eachQuery.get(j));
					if(index>0){
						sum += (n-(index-1))*(n-(j-1));
					}
				}
				pSum.add(sum);
			}
			// System.out.println("---non sorted pSum---");
			// for(int t=0; t<pSum.size(); t++){
				// System.out.print(pSum.get(t)+" ");
			// }
			// System.out.println();
			//call sorting function
			/*pSum = sort(pSum, 0, pSum.size()-1);
			System.out.println("---sorted pSum---");
			for(int t=0; t<pSum.size(); t++){
				System.out.print(pSum.get(t)+" ");
			}
			System.out.println();
			*/

			pSumIndex = indexSorting(pSum);
			// System.out.println("---sorted pSumIndex---");
			// for(int t=0; t<pSumIndex.size(); t++){
				// System.out.print(pSumIndex.get(t)+" ");
			// }
			// System.out.println();

			for(int l=0; l<pSumIndex.size(); l++){
				if(pSum.get(pSumIndex.get(l))>0){
					eachQPage.add("P"+(pSumIndex.get(l)));
				}
				else{
					continue;
				}
			}
			// System.out.println("---Each Queue Page---");
			// for(int t=0; t<eachQPage.size(); t++){
				// System.out.print(eachQPage.get(t)+" ");
			// }
			// System.out.println();
			qPages.add(eachQPage);
		}

		return qPages;
	}

	void printResult(ArrayList<ArrayList<String>> res){

		for(int i=0; i<res.size(); i++){
			System.out.print("Q"+(i+1)+": ");
			for(int j=0; j<(res.get(i)).size(); j++){
				System.out.print(res.get(i).get(j)+" ");
			}
			System.out.println();
		}

	}
	

}

public class MavenHive{

	public static void main(String args[]){

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter no. of web pages and queries(i.e l): ");
		int l = sc.nextInt();
		System.out.println("Enter the value of n (no. of keywords): ");
		int n = sc.nextInt();

		ArrayList<String> pages = new ArrayList<String>();
		ArrayList<String> queries = new ArrayList<String>();
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();

		System.out.println("Enter l web pages, each having atmost n keywords followed by P");
		for(int i=0; i<=l; i++){
			pages.add(sc.nextLine());
		}
		System.out.println("Enter l queries, each having atmost n keywords followed by Q");
		for(int i=0; i<l; i++){
			queries.add(sc.nextLine());
		}

		// System.out.println(pages.size());
		// System.out.println(queries.size());

		// List<String> w = Arrays.asList((queries.get(0)).split("\\s+"));
		// for(int i=0; i<w.size(); i++){
			// System.out.println(w.get(i));
		// }

		// System.out.println("---Pages---");
		 // for(int i=0; i<=l; i++){
			// System.out.println(pages.get(i));
		// }
		// System.out.println("---Queries---");
		// for(int i=0; i<l; i++){
			// System.out.println(queries.get(i));
		// }

		Maven m = new Maven();
		res = m.calcStrength(pages, queries, n);
		
		//Print result:
		System.out.println();
		m.printResult(res);
	}

}


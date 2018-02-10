//package com.PageSelect;


import java.util.*;
public class SearchEngine {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		LinkedList<HashMap<String,Integer>> webpages = new LinkedList<>();
		LinkedList<LinkedList<String>> queries = new LinkedList<>();
		int maxLength = 0;
		while(sc.hasNextLine()){
			String s = sc.nextLine();
			String[] data = s.split(" ");
			if(data[0].equals("P")){
				int count = 0;
				boolean zeroInd = true;
				HashMap<String,Integer> webp = new HashMap<>();
				for(String key:data){
					if(!zeroInd){
						webp.put(key.toLowerCase(),count++);
					}
					else
						zeroInd = false;
				}
				webpages.add(webp);
				maxLength = (maxLength<webp.size())?webp.size():maxLength;
			}
			else if(s.equalsIgnoreCase("end")){
				break;
			}
			else{
				int count = 0;
				LinkedList<String> query = new LinkedList<>();
				for(String key:data){
					if(count++ != 0){
						query.add(key.toLowerCase());
					}
				}
				queries.add(query);
				maxLength = (maxLength<query.size())?query.size():maxLength;
			}
		}
		sc.close();
		Iterator<LinkedList<String>> queriesItr = queries.iterator();
		int count = 0;
		while(queriesItr.hasNext()){
			queryRes(++count,queriesItr.next(),webpages,maxLength*2);
		}

	}
	
	private class PageValue{
		int value;
		String page;
		PageValue(int v,String p){
			value = v;
			page = p;
		}
	}
	
	public static void queryRes(int qNo,LinkedList<String> query,LinkedList<HashMap<String,Integer>> webpages,int maxLength){
		ArrayList<SearchEngine.PageValue> list = new ArrayList<>();
		SearchEngine sol = new SearchEngine();
		Iterator<HashMap<String,Integer>> itr = webpages.iterator();
		int pageNo = 1;
		while(itr.hasNext()){
			int pageValue = webPageValue(query,itr.next(),maxLength);
			if(pageValue != 0)
			{
				PageValue pv = sol.new PageValue(pageValue,"P"+pageNo);
				list.add(pv);
			}
			pageNo++;
		}
		
		Collections.sort(list, new Comparator<SearchEngine.PageValue>() {
	        public int compare(SearchEngine.PageValue pv1, SearchEngine.PageValue pv2) {
	            return pv2.value - pv1.value;
	        }
	    });
		
		System.out.print("Q"+qNo+": ");
		int resNum = 1;
		for (SearchEngine.PageValue pvobj : list) {
			if(resNum++ == 6){
				break;
	        }
	        System.out.print( pvobj.page+" ");
	    }
		System.out.println();
	}
	
	public static int webPageValue(LinkedList<String> query,HashMap<String,Integer> webpages,int maxLength){
		int resval = 0;
		int keyVal = maxLength;
		Iterator<String> itr = query.iterator();
		while(itr.hasNext()){
			String key = itr.next();
			if(webpages.containsKey(key)){
				resval += keyVal*(maxLength-webpages.get(key));
			}
			else{resval=0;}
			keyVal--;
		}
		return resval;	
	}
}

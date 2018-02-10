import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by saumya on 4/29/2017.
 */

// Class for Webpages can add parent for nested page
class Page {
    int id;
    String[] keywords;

    public Page(int id, String[] keywords) {
        this.id = id;
        this.keywords = keywords;
    }
}

// List item containing page and value of weight of particular keyword in the given page
class keywordPageMapListItem {
    Page page;
    int weight;

    public keywordPageMapListItem(Page page, int weight) {
        this.page = page;
        this.weight = weight;
    }
}

// Class to search page by relevance
public class Relevent_Page_Search {

    // Map containing keywords and list of keywordPageMapListItem(pages containing keyword  and weight of keyword in given page)
    HashMap<String, ArrayList<keywordPageMapListItem>> keywordPageMap = new HashMap<String, ArrayList<keywordPageMapListItem>>();

// Method to sort the map containg pages and  weight (for particular query)  wrt total weight of page and page number if weights are equal
    Map<Page, Integer> sortByValue(Map<Page, Integer> map) {
        Set<Entry<Page, Integer>> set = map.entrySet();
        List<Entry<Page, Integer>> list = new ArrayList<Entry<Page, Integer>>(set);
        Collections.sort(list, new Comparator<Map.Entry<Page, Integer>>() {
            public int compare(Map.Entry<Page, Integer> o1, Map.Entry<Page, Integer> o2) {
                if (o1.getValue() > o2.getValue())
                    return -1;
                else if (o1.getValue() == o2.getValue()) {
                    if (o1.getKey().id > o2.getKey().id)
                        return 1;
                    else
                        return -1;
                } else {
                    return 1;
                }
            }
        });

        Map<Page, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Page, Integer> entry : list) {

            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }


    // Method Printing all the pages in sorted ( by weight ) order for given query
    // queryKeywordWeight :  Maximum weigh tin query
    // Page_num : Number od pages to be displayed
    void getReleventPages(String[] QueryKeywords, int queryKeywordWeight, int page_num) {
        HashMap<Page, Integer> pageweight = new HashMap<Page, Integer>();
        for (int i = 0; i < QueryKeywords.length; i++) {

            if (keywordPageMap.get(QueryKeywords[i]) != null) {
                for (keywordPageMapListItem listitem : keywordPageMap.get(QueryKeywords[i])) {
                    if (pageweight.get(listitem.page) != null) {
                        pageweight.put(listitem.page, pageweight.get(listitem.page) + (listitem.weight * queryKeywordWeight));
                    } else {
                        pageweight.put(listitem.page, listitem.weight * queryKeywordWeight);
                    }
                }
            }
            queryKeywordWeight--;
        }
        PrintResult(sortByValue(pageweight), page_num);
    }

    // this method add new page in  keywordPageMap for respective keywords
    void AddNewPageWithKeywords(Page page, int weight) {
        for (int i = 0; i < page.keywords.length; i++) {

            String str = page.keywords[i];
            if (keywordPageMap.get(str) == null)
                keywordPageMap.put(str, new ArrayList<keywordPageMapListItem>());
            keywordPageMap.get(str).add(new keywordPageMapListItem(page, weight));
            weight--;
        }
    }


    void PrintResult(Map<Page, Integer> pageMap, int pagenum) {
        Set<Page> pageSet = pageMap.keySet();
        int i = 0;
        for (Page page : pageSet) {
            if (i >= pagenum)
                break;
            System.out.print(" P" + page.id);
            i++;

        }

        System.out.println();
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Relevent_Page_Search ob = new Relevent_Page_Search();
        int weight = 8;
        int page_num = 1;
        int query_num = 1;
        int pageNumToBeDisplayed = 5;
        while (true) {
            String str = br.readLine();
            if (str == null)
                break;
            if (str.isEmpty())
                break;
            String[] s;
            if (str.charAt(0) == 'P') {
                str = str.substring(2);
                s = str.split(" ");
                Page p = new Page(page_num, s);
                page_num++;
                ob.AddNewPageWithKeywords(p, weight);
            } else if (str.charAt(0) == 'Q') {
                str = str.substring(2);
                s = str.split(" ");
                System.out.print("Q" + query_num + ":");
                query_num++;
                ob.getReleventPages(s, weight, pageNumToBeDisplayed);
            }


        }
    }

}

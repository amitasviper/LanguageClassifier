package com.mavenhive;

import java.util.Map;
import java.util.Scanner;

import com.mavenhive.index.SimplePageRanker;
import com.mavenhive.indexer.PageIndexer;
import com.mavenhive.pages.ExponentialPageTokeniser;
import com.mavenhive.pages.ITokensier;
import com.mavenhive.pages.Page;
import com.mavenhive.query.Query;

public class Main {

	private static int pageIndex = 0;
	private static int queryIndex = 0;

	public static void main(String[] args) {
		System.out.println("Search Application started successfully");
		System.out.println("Press CTRL C to terminate the application");

		Scanner in = new Scanner(System.in);
		try {
			PageIndexer indexer = new PageIndexer(new SimplePageRanker());

			ITokensier pageTokeniser = new ExponentialPageTokeniser();
			while (in.hasNext()) {
				String line = in.nextLine();
				if (line.startsWith("P") || line.startsWith("p")) {
					indexer.index(getPage(line, pageTokeniser));
				} else if (line.startsWith("Q") || line.startsWith("q")) {
					Query query = getQuery(line);
					String result = indexer.search(query);
					System.out.println(result);
				} else {
					System.out.println("Invalid input");
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static Query getQuery(String line) {
		String[] entry = line.trim().split(" ");
		return new Query("Q" + ++queryIndex, entry);
	}

	public static Page getPage(String line, ITokensier pageTokeniser) {
		String[] entry = line.trim().split(" ");
		Map<String, Double> keyWords = pageTokeniser.tokeniseAndWeigh(entry);
		return new Page("P" + ++pageIndex, entry, keyWords);
	}
}

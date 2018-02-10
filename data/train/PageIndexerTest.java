package com.mavenhive.indexer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.mavenhive.index.Index;
import com.mavenhive.index.SimplePageRanker;
import com.mavenhive.pages.ExponentialPageTokeniser;
import com.mavenhive.pages.ITokensier;
import com.mavenhive.pages.Page;
import com.mavenhive.query.Query;

public class PageIndexerTest {
	
	PageIndexer indexer;
	static Page page1, page2;
	
	
	@BeforeClass
	public static void setup() {
		String[] line1 = "P car review".split(" ");
		String[] line2 = "P Ford review".split(" ");
		ITokensier tokensier = new ExponentialPageTokeniser();
		page1 = new Page("P1",line1, tokensier.tokeniseAndWeigh(line1));
		page2 = new Page("P2",line2, tokensier.tokeniseAndWeigh(line2));
	}
	
	
	
	@Test
	public void TestIndex() {
		Index mockIndex = Mockito.mock(Index.class);
		indexer = new PageIndexer(mockIndex);
		indexer.index(page1);
		Mockito.verify(mockIndex).add("car", page1);
		Mockito.verify(mockIndex).add("review", page1);
	}
	
	@Test 
	public void TestSearch() {
		indexer = new PageIndexer(new SimplePageRanker());
		indexer.index(page1);
		String result = indexer.search(new Query("Q1","Q car".split(" ")));
		Assert.assertEquals("Result doestn't match", "Q1 :P1 ", result);
	}
	
	@Test 
	public void TestMultiplePage() {
		indexer = new PageIndexer(new SimplePageRanker());
		indexer.index(page1);
		indexer.index(page2);
		String result = indexer.search(new Query("Q2","Q car".split(" ")));
		Assert.assertEquals("Result doestn't match", "Q2 :P1 ", result);
		result = indexer.search(new Query("Q3", "Q Ford car".split(" ")));
		Assert.assertEquals("Result doestn't match", "Q3 :P2 P1 ", result);
	}
	
	@Test 
	public void TestMultiplePageQueryWithNoResults() {
		indexer = new PageIndexer(new SimplePageRanker());
		indexer.index(page1);
		indexer.index(page2);
		String result = indexer.search(new Query("Q4","Q Amith".split(" ")));
		Assert.assertEquals("Result doestn't match", "Q4 :", result);
	}
}

package com.mavenhive.pages;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExponentialPageTokeniser implements ITokensier {
	
	public Map<String, Double> tokeniseAndWeigh(String[] entry) {
		LinkedHashMap<String, Double> keyWords = new LinkedHashMap<String, Double>();
		for (int i = 1; i < entry.length; i++) {
			keyWords.put(entry[i], Math.pow(2, (entry.length - i)));
		}
		return keyWords;
	}
}

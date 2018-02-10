package com.mavenhive.pages;

import java.util.Map;

public interface ITokensier {
	public Map<String, Double> tokeniseAndWeigh(String[] entry);
}

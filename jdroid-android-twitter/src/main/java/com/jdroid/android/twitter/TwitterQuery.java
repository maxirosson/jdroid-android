package com.jdroid.android.twitter;

public class TwitterQuery {

	private String query;
	private Integer maxItemsPerRequest;
	private String languageCode;

	public TwitterQuery(String query, Integer maxItemsPerRequest, String languageCode) {
		this.query = query;
		this.maxItemsPerRequest = maxItemsPerRequest;
		this.languageCode = languageCode;
	}

	public String getQuery() {
		return query;
	}

	public Integer getMaxItemsPerRequest() {
		return maxItemsPerRequest;
	}

	public String getLanguageCode() {
		return languageCode;
	}

}

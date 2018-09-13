package com.jdroid.android.http;

import com.jdroid.java.http.HttpServiceFactory;

public class HttpConfiguration {

	private static HttpServiceFactory httpServiceFactory;

	public static HttpServiceFactory getHttpServiceFactory() {
		return httpServiceFactory;
	}

	public static void setHttpServiceFactory(HttpServiceFactory httpServiceFactory) {
		HttpConfiguration.httpServiceFactory = httpServiceFactory;
	}

}

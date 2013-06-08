package com.jdroid.android.http;

import org.apache.http.client.HttpClient;
import android.net.http.AndroidHttpClient;
import com.jdroid.java.http.apache.HttpClientFactory;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidHttpClientFactory implements HttpClientFactory {
	
	private final static String DEFAULT_USER_AGENT = "android";
	
	/**
	 * @see com.jdroid.java.http.apache.HttpClientFactory#createHttpClient()
	 */
	@Override
	public HttpClient createHttpClient() {
		return AndroidHttpClient.newInstance(DEFAULT_USER_AGENT);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.HttpClientFactory#createHttpClient(java.lang.Boolean, java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public HttpClient createHttpClient(Boolean ssl, Integer timeout, String userAgent) {
		return AndroidHttpClient.newInstance(userAgent);
	}
	
}

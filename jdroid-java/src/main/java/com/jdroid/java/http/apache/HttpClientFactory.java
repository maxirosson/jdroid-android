package com.jdroid.java.http.apache;

import org.apache.http.client.HttpClient;

/**
 * Factory interface to create {@link HttpClient}s.
 * 
 * @author Maxi Rosson
 */
public interface HttpClientFactory {
	
	/**
	 * Creates a {@link HttpClient} and sets a default timeout and user agent for it.
	 * 
	 * @return {@link HttpClient} The created client.
	 */
	public HttpClient createHttpClient();
	
	/**
	 * Creates a {@link HttpClient} and sets a timeout for it.
	 * 
	 * @param ssl Whether the connection needs ssl or not
	 * @param timeout The connection timeout in milliseconds. If null a default timeout of 10 seconds will be used.
	 * @param userAgent The user agent
	 * 
	 * @return {@link HttpClient} The created client.
	 */
	public HttpClient createHttpClient(Boolean ssl, Integer timeout, String userAgent);
}

package com.jdroid.java.http;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import com.jdroid.java.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DefaultHttpClientFactory implements HttpClientFactory {
	
	// 10 seconds
	private static final int DEFAULT_TIMEOUT = 10000;
	// 60 seconds
	private static final int DEFAULT_SO_TIMEOUT = 60000;
	
	private static HttpClientFactory INSTANCE;
	
	public static HttpClientFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultHttpClientFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.java.http.HttpClientFactory#createHttpClient()
	 */
	@Override
	public DefaultHttpClient createHttpClient() {
		return createHttpClient(false, null, null);
	}
	
	/**
	 * @see com.jdroid.java.http.HttpClientFactory#createHttpClient(java.lang.Boolean, java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public DefaultHttpClient createHttpClient(Boolean ssl, Integer timeout, String userAgent) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
			timeout != null ? timeout : DEFAULT_TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
		if (StringUtils.isNotBlank(userAgent)) {
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
		}
		return client;
	}
}
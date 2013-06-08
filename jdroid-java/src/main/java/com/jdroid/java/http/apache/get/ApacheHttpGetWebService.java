package com.jdroid.java.http.apache.get;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.apache.ApacheHttpWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

public class ApacheHttpGetWebService extends ApacheHttpWebService {
	
	public ApacheHttpGetWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return HttpGet.METHOD_NAME;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#createHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String protocol) {
		return new HttpGet(protocol + "://" + getBaseURL() + getUrlSegments() + makeStringParameters());
	}
}

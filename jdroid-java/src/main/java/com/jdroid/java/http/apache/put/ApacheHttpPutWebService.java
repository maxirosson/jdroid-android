package com.jdroid.java.http.apache.put;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

public class ApacheHttpPutWebService extends ApacheHttpEntityEnclosingWebService {
	
	public ApacheHttpPutWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return HttpPut.METHOD_NAME;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPut(uri);
	}
}

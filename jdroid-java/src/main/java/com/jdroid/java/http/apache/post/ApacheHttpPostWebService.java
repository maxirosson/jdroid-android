package com.jdroid.java.http.apache.post;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

public class ApacheHttpPostWebService extends ApacheHttpEntityEnclosingWebService {
	
	public ApacheHttpPostWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return HttpPost.METHOD_NAME;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPost(uri);
	}
}

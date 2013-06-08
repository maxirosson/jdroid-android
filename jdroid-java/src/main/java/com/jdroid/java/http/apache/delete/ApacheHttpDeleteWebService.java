package com.jdroid.java.http.apache.delete;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.apache.ApacheHttpWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

public class ApacheHttpDeleteWebService extends ApacheHttpWebService {
	
	public ApacheHttpDeleteWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return HttpDelete.METHOD_NAME;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#createHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String protocol) {
		return new HttpDelete(protocol + "://" + getBaseURL() + getUrlSegments() + makeStringParameters());
	}
}

package com.jdroid.java.http.delete;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import com.jdroid.java.http.HttpClientFactory;
import com.jdroid.java.http.HttpWebService;
import com.jdroid.java.http.HttpWebServiceProcessor;

public class HttpDeleteWebService extends HttpWebService {
	
	public HttpDeleteWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.HttpWebService#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return HttpDelete.METHOD_NAME;
	}
	
	/**
	 * @see com.jdroid.java.http.HttpWebService#createHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String protocol) {
		return new HttpDelete(protocol + "://" + getBaseURL() + getUrlSegments() + makeStringParameters());
	}
}

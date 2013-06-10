package com.jdroid.java.api;

import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.apache.DefaultHttpClientFactory;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.http.apache.delete.ApacheHttpDeleteWebService;
import com.jdroid.java.http.apache.get.ApacheHttpGetWebService;
import com.jdroid.java.http.apache.post.ApacheFormHttpPostWebService;
import com.jdroid.java.http.apache.post.ApacheHttpPostWebService;
import com.jdroid.java.http.apache.put.ApacheHttpPutWebService;
import com.jdroid.java.http.apache.put.ApacheMultipartHttpPutWebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractApacheApiService extends AbstractApiService {
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newGetService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected WebService newGetServiceImpl(String baseURL, HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheHttpGetWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newPostService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected EntityEnclosingWebService newPostServiceImpl(String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheHttpPostWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newPutService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected EntityEnclosingWebService newPutServiceImpl(String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheHttpPutWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newMultipartPutService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected MultipartWebService newMultipartPutServiceImpl(String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheMultipartHttpPutWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newDeleteService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected WebService newDeleteServiceImpl(String baseURL, HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheHttpDeleteWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newFormPostService(java.lang.String,
	 *      com.jdroid.java.http.HttpWebServiceProcessor[])
	 */
	@Override
	protected EntityEnclosingWebService newFormPostServiceImpl(String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		return new ApacheFormHttpPostWebService(getHttpClientFactoryInstance(), baseURL,
				toArray(getHttpWebServiceProcessors()));
	}
	
	protected HttpClientFactory getHttpClientFactoryInstance() {
		// TODO See how the AndroidHttpClient works
		// return AndroidHttpClientFactory.get();
		return DefaultHttpClientFactory.get();
	}
}

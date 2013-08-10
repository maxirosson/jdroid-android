package com.jdroid.java.http.apache;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.EntityEnclosingWebService;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class ApacheHttpEntityEnclosingWebService extends ApacheHttpWebService implements
		EntityEnclosingWebService {
	
	private HttpEntity entity;
	
	public ApacheHttpEntityEnclosingWebService(HttpClientFactory httpClientFactory, Server server,
			List<Object> urlSegments, HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#createHttpUriRequest()
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String url) {
		// New HttpEntityEnclosingRequestBase for send request.
		HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = createHttpEntityEnclosingRequestBase(url);
		
		// set body for request.
		addEntity(httpEntityEnclosingRequestBase);
		
		return httpEntityEnclosingRequestBase;
	}
	
	protected abstract HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri);
	
	protected void addEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
		httpEntityEnclosingRequestBase.setEntity(entity);
	}
	
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	
	/**
	 * @see com.jdroid.java.http.post.EntityEnclosingWebService#setEntity(java.lang.String)
	 */
	@Override
	public void setEntity(String content) {
		try {
			entity = new StringEntity(content, HTTP.UTF_8);
			ApacheHttpWebService.LOGGER.debug("Entity: " + content);
		} catch (UnsupportedEncodingException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public HttpEntity getEntity() {
		return entity;
	}
}

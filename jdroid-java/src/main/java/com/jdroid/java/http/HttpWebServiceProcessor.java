package com.jdroid.java.http;


/**
 * 
 * @author Maxi Rosson
 */
public interface HttpWebServiceProcessor {
	
	public void beforeExecute(WebService webService);
	
	public void afterExecute(WebService webService, HttpResponseWrapper httpResponse);
	
}
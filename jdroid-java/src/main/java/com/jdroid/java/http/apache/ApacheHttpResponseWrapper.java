package com.jdroid.java.http.apache;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import com.jdroid.java.http.HttpResponseWrapper;

/**
 * 
 * @author Maxi Rosson
 */
public class ApacheHttpResponseWrapper extends HttpResponseWrapper {
	
	private HttpResponse httpResponse;
	
	public ApacheHttpResponseWrapper(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getStatusReason()
	 */
	@Override
	public String getStatusReason() {
		return httpResponse.getStatusLine().getReasonPhrase();
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		Header[] headerstatusCode = httpResponse.getHeaders(name);
		return headerstatusCode.length > 0 ? headerstatusCode[0].getValue() : null;
	}
	
}

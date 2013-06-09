package com.jdroid.java.http;

import java.util.Collection;
import com.jdroid.java.parser.Parser;

public interface WebService {
	
	public static final String HTTPS_PROTOCOL = "https";
	public static final String HTTP_PROTOCOL = "http";
	
	public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
	public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
	public static final String GZIP_ENCODING = "gzip";
	public static final String ACCEPT_HEADER = "accept";
	public static final String CONTENT_TYPE_HEADER = "content-type";
	
	public static final String QUESTION_MARK = "?";
	public static final String EQUALS = "=";
	public static final String AMPERSAND = "&";
	
	/**
	 * @param <T>
	 * @param parser
	 * @return WebServiceResponse
	 */
	public <T> T execute(Parser parser);
	
	/**
	 * @param <T>
	 * @return WebServiceResponse
	 */
	public <T> T execute();
	
	/**
	 * @param name The header name.
	 * @param value The header value.
	 */
	public void addHeader(String name, String value);
	
	/**
	 * @param name The parameter name.
	 * @param value The parameter value.
	 */
	public void addQueryParameter(String name, Object value);
	
	/**
	 * @param name The parameter name.
	 * @param values The parameter values.
	 */
	public void addQueryParameter(String name, Collection<?> values);
	
	/**
	 * @param segment The segment name
	 */
	public void addUrlSegment(Object segment);
	
	/**
	 * @param connectionTimeout The connection timeout in milliseconds.
	 */
	public void setConnectionTimeout(Integer connectionTimeout);
	
	/**
	 * @param userAgent The user agent
	 */
	public void setUserAgent(String userAgent);
	
	/**
	 * @param ssl
	 */
	public void setSsl(Boolean ssl);
	
}

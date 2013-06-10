package com.jdroid.java.http.apache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.WebService;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.EncodingUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

public abstract class ApacheHttpWebService implements WebService {
	
	protected static final Logger LOGGER = LoggerUtils.getLogger(ApacheHttpWebService.class);
	
	private Boolean ssl = false;
	
	/** Connection timeout in milliseconds */
	private Integer connectionTimeout;
	private String userAgent;
	
	/** Query Parameter values of the request. */
	private List<NameValuePair> queryParameters = Lists.newArrayList();
	
	private List<String> urlSegments = Lists.newArrayList();
	
	/** Base URL of the request to execute */
	private String baseURL;
	
	/** Header values of the request. */
	private List<NameValuePair> headers = Lists.newArrayList();
	
	private List<Cookie> cookies = Lists.newArrayList();
	
	private InputStream inputStream;
	
	private List<HttpWebServiceProcessor> httpWebServiceProcessors;
	
	private HttpClientFactory httpClientFactory;
	
	/**
	 * @param httpClientFactory the httpClientFactory
	 * @param httpWebServiceProcessors
	 * @param baseURL The Base URL of the request to execute
	 */
	public ApacheHttpWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		this.httpClientFactory = httpClientFactory;
		this.baseURL = baseURL;
		this.httpWebServiceProcessors = httpWebServiceProcessors != null ? Lists.newArrayList(httpWebServiceProcessors)
				: null;
	}
	
	public abstract String getMethodName();
	
	/**
	 * @see com.jdroid.java.http.WebService#execute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> T execute() {
		return (T)execute(null);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#execute(com.jdroid.java.parser.Parser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(Parser parser) {
		HttpClient client = null;
		try {
			
			if (httpWebServiceProcessors != null) {
				for (HttpWebServiceProcessor each : httpWebServiceProcessors) {
					each.beforeExecute(this);
				}
			}
			
			// make client for http.
			client = httpClientFactory.createHttpClient(ssl, connectionTimeout, userAgent);
			
			// Add Cookies
			addCookies(client);
			
			// make request.
			HttpUriRequest request = createHttpUriRequest(ssl ? HTTPS_PROTOCOL : HTTP_PROTOCOL);
			
			// Log request
			LOGGER.debug(getMethodName() + ": " + request.getRequestLine().getUri());
			if (!queryParameters.isEmpty()) {
				LOGGER.debug("Query Parameters: " + queryParameters.toString());
			}
			if (!headers.isEmpty()) {
				LOGGER.debug("Headers: " + headers.toString());
			}
			
			// Add Headers
			addHeaders(request);
			
			// execute request.
			HttpResponse httpResponse = client.execute(request);
			
			if (httpWebServiceProcessors != null) {
				HttpResponseWrapper httpResponseWrapper = new ApacheHttpResponseWrapper(httpResponse);
				for (HttpWebServiceProcessor each : httpWebServiceProcessors) {
					each.afterExecute(this, httpResponseWrapper);
				}
			}
			
			// obtain input stream.
			inputStream = httpResponse.getEntity() != null ? httpResponse.getEntity().getContent() : null;
			Header contentEncoding = httpResponse.getFirstHeader(CONTENT_ENCODING_HEADER);
			if ((contentEncoding != null) && contentEncoding.getValue().equalsIgnoreCase(GZIP_ENCODING)) {
				inputStream = new GZIPInputStream(inputStream);
			}
			
			// parse and return response.
			return (T)((parser != null) && (inputStream != null) ? parser.parse(inputStream) : null);
		} catch (ClientProtocolException e) {
			throw new UnexpectedException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		} finally {
			FileUtils.safeClose(inputStream);
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}
	
	protected String makeStringParameters() {
		StringBuilder params = new StringBuilder();
		boolean isFirst = true;
		
		for (NameValuePair pair : getQueryParameters()) {
			if (isFirst) {
				params.append(QUESTION_MARK);
				isFirst = false;
			} else {
				params.append(AMPERSAND);
			}
			params.append(pair.getName());
			params.append(EQUALS);
			params.append(pair.getValue());
		}
		
		return params.toString();
	}
	
	protected String getUrlSegments() {
		return urlSegments.isEmpty() ? StringUtils.EMPTY : StringUtils.SLASH
				+ StringUtils.join(urlSegments, StringUtils.SLASH);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addUrlSegment(java.lang.Object)
	 */
	@Override
	public void addUrlSegment(Object segment) {
		String segmentString = segment.toString();
		if (StringUtils.isNotEmpty(segmentString)) {
			urlSegments.add(EncodingUtils.encodeURL(segmentString));
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		if (value != null) {
			headers.add(new BasicNameValuePair(name, value));
		}
	}
	
	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addQueryParameter(String name, Object value) {
		if (value != null) {
			queryParameters.add(new BasicNameValuePair(name, EncodingUtils.encodeURL(value.toString())));
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		addQueryParameter(name, StringUtils.join(values));
	}
	
	protected void addHeaders(HttpUriRequest httpUriRequest) {
		for (NameValuePair pair : headers) {
			httpUriRequest.addHeader(pair.getName(), pair.getValue());
		}
	}
	
	protected void addCookies(HttpClient client) {
		if (client instanceof DefaultHttpClient) {
			for (Cookie cookie : cookies) {
				DefaultHttpClient.class.cast(client).getCookieStore().addCookie(cookie);
			}
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addHttpWebServiceProcessor(com.jdroid.java.http.HttpWebServiceProcessor)
	 */
	@Override
	public void addHttpWebServiceProcessor(HttpWebServiceProcessor httpWebServiceProcessor) {
		httpWebServiceProcessors.add(httpWebServiceProcessor);
	}
	
	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " - " + baseURL;
	}
	
	/**
	 * @return the parameters
	 */
	protected List<NameValuePair> getQueryParameters() {
		return queryParameters;
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	/**
	 * Create the {@link HttpUriRequest} to send.
	 * 
	 * @param protocol
	 */
	protected abstract HttpUriRequest createHttpUriRequest(String protocol);
	
	/**
	 * @see com.jdroid.java.http.WebService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}
	
}

package com.jdroid.java.http.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * Mocked {@link WebService} and {@link EntityEnclosingWebService} implementation that returns mocked responses
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractMockWebService implements MultipartWebService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractMockWebService.class);
	
	private static final String MOCK_SEPARATOR = "_";
	private static final String SUFFIX_SEPARATOR = "-";
	private static final String URL_SEPARATOR = "/";
	
	private Object[] urlSegments;
	private Map<String, String> parameters = Maps.newHashMap();
	private Map<String, String> headers = Maps.newHashMap();
	private String entityContent;
	
	public AbstractMockWebService(Object... urlSegments) {
		this.urlSegments = urlSegments;
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#execute(com.jdroid.java.parser.Parser)
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public <T> T execute(Parser parser) {
		
		StringBuilder sb = new StringBuilder(getMocksBasePath());
		if (urlSegments != null) {
			for (int i = 0; i < urlSegments.length; i++) {
				sb.append(urlSegments[i].toString().replaceAll(URL_SEPARATOR, MOCK_SEPARATOR));
				sb.append(MOCK_SEPARATOR);
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String suffix = getSuffix(sb.toString());
		if (StringUtils.isNotBlank(suffix)) {
			sb.append(SUFFIX_SEPARATOR);
			sb.append(suffix);
		}
		
		sb.append(getMocksExtension());
		String filePath = sb.toString();
		
		LOGGER.warn("Request: " + filePath);
		if (!parameters.isEmpty()) {
			LOGGER.warn("Parameters: " + parameters.toString());
		}
		if (!headers.isEmpty()) {
			LOGGER.warn("Headers: " + headers.toString());
		}
		
		if (StringUtils.isNotBlank(entityContent)) {
			LOGGER.warn("HTTP Entity Body: " + entityContent);
		}
		
		Integer httpMockSleep = getHttpMockSleepDuration(urlSegments);
		if ((httpMockSleep != null) && (httpMockSleep > 0)) {
			ExecutorUtils.sleep(httpMockSleep);
		}
		
		simulateCrash();
		
		if (parser != null) {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
			if (inputStream == null) {
				throw new UnexpectedException("The mocked file wasn't found");
			}
			// Parse the stream
			T t = (T)(parser.parse(inputStream));
			FileUtils.safeClose(inputStream);
			return t;
		} else {
			return null;
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#execute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute() {
		return (T)execute(null);
	}
	
	protected void simulateCrash() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		if (value != null) {
			headers.put(name, value.toString());
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addQueryParameter(String name, Object value) {
		if (value != null) {
			parameters.put(name, value.toString());
		}
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		addQueryParameter(name, StringUtils.join(values));
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addPart(java.lang.String, java.io.ByteArrayInputStream,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addPart(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void addPart(String name, Object value, String mimeType) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addJsonPart(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addJsonPart(String name, Object value) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addUrlSegment(java.lang.Object)
	 */
	@Override
	public void addUrlSegment(Object segment) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.post.EntityEnclosingWebService#setEntity(java.lang.String)
	 */
	@Override
	public void setEntity(String entityContent) {
		this.entityContent = entityContent;
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		// Do Nothing
	}
	
	/**
	 * @return The time to sleep (in seconds) to simulate the execution of the request
	 */
	protected abstract Integer getHttpMockSleepDuration(Object... urlSegments);
	
	/**
	 * @return The mocks base path
	 */
	protected abstract String getMocksBasePath();
	
	/**
	 * @return The mocks extension
	 */
	protected abstract String getMocksExtension();
	
	/**
	 * @return The suffix to add to the mock file
	 */
	protected String getSuffix(String path) {
		return null;
	}
	
}

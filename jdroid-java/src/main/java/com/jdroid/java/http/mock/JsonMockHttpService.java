package com.jdroid.java.http.mock;

public abstract class JsonMockHttpService extends AbstractMockHttpService {
	
	private static final String MOCKS_BASE_PATH = "mocks/json/";
	private static final String MOCKS_EXTENSION = ".json";
	
	public JsonMockHttpService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see AbstractMockHttpService#getMocksBasePath()
	 */
	@Override
	protected String getMocksBasePath() {
		return MOCKS_BASE_PATH;
	}
	
	/**
	 * @see AbstractMockHttpService#getMocksExtension()
	 */
	@Override
	protected String getMocksExtension() {
		return MOCKS_EXTENSION;
	}
	
}

package com.jdroid.java.http.mock;

public abstract class XmlMockHttpService extends AbstractMockHttpService {
	
	private static final String MOCKS_BASE_PATH = "mocks/xml/";
	private static final String MOCKS_EXTENSION = ".xml";
	
	public XmlMockHttpService(Object... urlSegments) {
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

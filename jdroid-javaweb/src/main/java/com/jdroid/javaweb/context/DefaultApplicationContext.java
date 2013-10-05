package com.jdroid.javaweb.context;

import com.jdroid.java.utils.StringUtils;

/**
 * The {@link DefaultApplicationContext}
 */
public class DefaultApplicationContext {
	
	private String appName;
	private String appURL;
	private String appVersion;
	
	private String googleServerApiKey;
	private Boolean httpMockEnabled;
	private Integer httpMockSleepDuration;
	
	/**
	 * @param applicationUrl the application URL
	 */
	public void setApplicationURL(String applicationUrl) {
		if (StringUtils.isNotEmpty(applicationUrl)) {
			int pos = applicationUrl.indexOf(getAppName());
			if (pos != -1) {
				applicationUrl = applicationUrl.substring(0, pos + getAppName().length());
				appURL = applicationUrl;
			}
		}
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * @return The application URL
	 */
	public String getAppURL() {
		return appURL;
	}
	
	/**
	 * @param appURL the appURL to set
	 */
	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}
	
	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}
	
	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public void setHttpMockSleepDuration(Integer httpMockSleepDuration) {
		this.httpMockSleepDuration = httpMockSleepDuration;
	}
	
	/**
	 * @param httpMockEnabled the httpMockEnabled to set
	 */
	public void setHttpMockEnabled(Boolean httpMockEnabled) {
		this.httpMockEnabled = httpMockEnabled;
	}
	
	public Boolean isHttpMockEnabled() {
		return httpMockEnabled;
	}
	
	public Integer getHttpMockSleepDuration() {
		return httpMockSleepDuration;
	}
	
	public String getGoogleServerApiKey() {
		return googleServerApiKey;
	}
	
	public void setGoogleServerApiKey(String googleServerApiKey) {
		this.googleServerApiKey = googleServerApiKey;
	}
}

package com.jdroid.android.context;

import java.util.Set;
import com.jdroid.java.utils.PropertiesUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DefaultApplicationContext {
	
	private static final String PROPERTIES_RESOURCE_NAME = "settings.properties";
	private static final String LOCAL_PROPERTIES_RESOURCE_NAME = "settings.local.properties";
	
	private String googleProjectId;
	private String facebookAppId;
	private Boolean devSettings;
	private Boolean productionEnvironment;
	private Boolean isFreeApp;
	private Boolean adsEnabled;
	private String adUnitId;
	private Set<String> testDevicesIds;
	private Boolean cookieRepositoryEnabled = false;
	private Boolean analyticsEnabled;
	private String analyticsTrackingId;
	
	public DefaultApplicationContext() {
		PropertiesUtils.loadProperties(LOCAL_PROPERTIES_RESOURCE_NAME);
		PropertiesUtils.loadProperties(PROPERTIES_RESOURCE_NAME);
		
		googleProjectId = PropertiesUtils.getStringProperty("google.projectId");
		facebookAppId = PropertiesUtils.getStringProperty("facebook.app.id");
		devSettings = PropertiesUtils.getBooleanProperty("dev.settings");
		productionEnvironment = PropertiesUtils.getBooleanProperty("production.environment");
		isFreeApp = PropertiesUtils.getBooleanProperty("free.app");
		adsEnabled = PropertiesUtils.getBooleanProperty("ads.enabled", false);
		adUnitId = PropertiesUtils.getStringProperty("ads.adUnitId");
		testDevicesIds = PropertiesUtils.getStringSetProperty("ads.tests.devices.ids");
		analyticsEnabled = PropertiesUtils.getBooleanProperty("analytics.enabled", false);
		analyticsTrackingId = PropertiesUtils.getStringProperty("analytics.trackingId");
	}
	
	/**
	 * @return The Google project ID acquired from the API console
	 */
	public String getGoogleProjectId() {
		return googleProjectId;
	}
	
	/**
	 * @return The registered Facebook app ID that is used to identify this application for Facebook.
	 */
	public String getFacebookAppId() {
		return facebookAppId;
	}
	
	/**
	 * @return Whether the application should display the development settings
	 */
	public Boolean displayDevSettings() {
		return devSettings;
	}
	
	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return productionEnvironment;
	}
	
	/**
	 * @return Whether the application is free or not
	 */
	public Boolean isFreeApp() {
		return isFreeApp;
	}
	
	/**
	 * @return Whether the application has ads enabled or not
	 */
	public Boolean areAdsEnabled() {
		return adsEnabled;
	}
	
	public Boolean isCookieRepositoryEnabled() {
		return cookieRepositoryEnabled;
	}
	
	/**
	 * @return The MD5-hashed ID of the devices that should display mocked ads
	 */
	public Set<String> getTestDevicesIds() {
		return testDevicesIds;
	}
	
	/**
	 * @return The AdMob Publisher ID
	 */
	public String getAdUnitId() {
		return adUnitId;
	}
	
	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public Boolean isAnalyticsEnabled() {
		return analyticsEnabled;
	}
	
	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getAnalyticsTrackingId() {
		return analyticsTrackingId;
	}
}

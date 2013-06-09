package com.jdroid.android.context;

import java.util.Set;
import android.preference.PreferenceManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.PropertiesUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DefaultApplicationContext {
	
	private static final String PROPERTIES_RESOURCE_NAME = "settings.properties";
	private static final String LOCAL_PROPERTIES_RESOURCE_NAME = "settings.local.properties";
	
	private String environmentName;
	private String googleProjectId;
	private String facebookAppId;
	private Boolean debugSettings;
	private Boolean isFreeApp;
	private Boolean adsEnabled;
	private String adUnitId;
	private Set<String> testDevicesIds;
	private Boolean googleAnalyticsEnabled;
	private String googleAnalyticsTrackingId;
	private Boolean crittercismEnabled;
	private String crittercismAppId;
	private Boolean crittercismPremium;
	
	public DefaultApplicationContext() {
		PropertiesUtils.loadProperties(LOCAL_PROPERTIES_RESOURCE_NAME);
		PropertiesUtils.loadProperties(PROPERTIES_RESOURCE_NAME);
		
		environmentName = PropertiesUtils.getStringProperty("environment.name");
		googleProjectId = PropertiesUtils.getStringProperty("google.projectId");
		facebookAppId = PropertiesUtils.getStringProperty("facebook.app.id");
		debugSettings = PropertiesUtils.getBooleanProperty("debug.settings");
		isFreeApp = PropertiesUtils.getBooleanProperty("free.app");
		adsEnabled = PropertiesUtils.getBooleanProperty("ads.enabled", false);
		adUnitId = PropertiesUtils.getStringProperty("ads.adUnitId");
		testDevicesIds = PropertiesUtils.getStringSetProperty("ads.tests.devices.ids");
		googleAnalyticsEnabled = PropertiesUtils.getBooleanProperty("google.analytics.enabled", false);
		googleAnalyticsTrackingId = PropertiesUtils.getStringProperty("google.analytics.trackingId");
		crittercismEnabled = PropertiesUtils.getBooleanProperty("crittercism.enabled", false);
		crittercismAppId = PropertiesUtils.getStringProperty("crittercism.appId");
		crittercismPremium = PropertiesUtils.getBooleanProperty("crittercism.premium", false);
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
	 * @return Whether the application should display the debug settings
	 */
	public Boolean displayDebugSettings() {
		return debugSettings;
	}
	
	public String getEnvironmentName() {
		return environmentName;
	}
	
	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return environmentName.equals("PROD");
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
	public Boolean isGoogleAnalyticsEnabled() {
		return googleAnalyticsEnabled;
	}
	
	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getGoogleAnalyticsTrackingId() {
		return googleAnalyticsTrackingId;
	}
	
	public Boolean isHttpMockEnabled() {
		return !isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					"httpMockEnabled", false);
	}
	
	public Integer getHttpMockSleepDuration() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean("httpMockSleep",
			false) ? 10 : null;
	}
	
	public String getCrashType() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString("crashType", null);
	}
	
	public String getHttpMockCrashType() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString("httpMockCrashType",
			null);
	}
	
	public Boolean isCrittercismEnabled() {
		return crittercismEnabled;
	}
	
	public String getCrittercismAppId() {
		return crittercismAppId;
	}
	
	public Boolean isCrittercismPremium() {
		return crittercismPremium;
	}
}

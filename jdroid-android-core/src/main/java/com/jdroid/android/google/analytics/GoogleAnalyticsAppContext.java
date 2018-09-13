package com.jdroid.android.google.analytics;

import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;

public class GoogleAnalyticsAppContext {

	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public static Boolean isGoogleAnalyticsEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("GOOGLE_ANALYTICS_ENABLED", true) && !FirebaseTestLab.isRunningInstrumentedTests();
	}

	/**
	 * @return The Google Analytics Tracking ID
	 */
	public static String getGoogleAnalyticsTrackingId() {
		return BuildConfigUtils.getBuildConfigValue("GOOGLE_ANALYTICS_TRACKING_ID", null);
	}

}

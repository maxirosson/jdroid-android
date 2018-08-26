package com.jdroid.android.google.analytics;

public class GoogleAnalyticsFactory {

	private static GoogleAnalyticsHelper googleAnalyticsHelper = new GoogleAnalyticsHelper();

	public static GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		return googleAnalyticsHelper;
	}

	public static void setGoogleAnalyticsHelper(GoogleAnalyticsHelper googleAnalyticsHelper) {
		GoogleAnalyticsFactory.googleAnalyticsHelper = googleAnalyticsHelper;
	}
}

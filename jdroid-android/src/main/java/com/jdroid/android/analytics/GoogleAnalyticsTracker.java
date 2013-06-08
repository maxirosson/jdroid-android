package com.jdroid.android.analytics;

import android.app.Activity;
import com.google.analytics.tracking.android.EasyTracker;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public class GoogleAnalyticsTracker extends DefaultAnalyticsTracker {
	
	private static final GoogleAnalyticsTracker INSTANCE = new GoogleAnalyticsTracker();
	
	public static GoogleAnalyticsTracker get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAndroidApplicationContext().isGoogleAnalyticsEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.DefaultAnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(Activity activity) {
		EasyTracker.getInstance().activityStart(activity);
	}
	
	/**
	 * @see com.jdroid.android.analytics.DefaultAnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		EasyTracker.getInstance().activityStop(activity);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(ConnectionException connectionException) {
		// TODO Implement this
	}
	
	public void trackEvent(String category, String action, String label, Integer value) {
		trackEvent(category, action, label, value.longValue());
	}
	
	public void trackEvent(String category, String action, String label, Long value) {
		EasyTracker.getTracker().trackEvent(category, action, label, value);
	}
	
	public void trackEvent(String category, String action, String label) {
		trackEvent(category, action, label, (Long)null);
	}
}

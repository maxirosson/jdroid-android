package com.jdroid.android.analytics;

import android.app.Activity;
import com.google.analytics.tracking.android.EasyTracker;
import com.jdroid.android.AbstractApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class AnalyticsTracker {
	
	public static void activityStop(Activity activity) {
		if (AbstractApplication.get().getAndroidApplicationContext().isAnalyticsEnabled()) {
			EasyTracker.getInstance().activityStop(activity);
		}
	}
	
	public static void activityStart(Activity activity) {
		if (AbstractApplication.get().getAndroidApplicationContext().isAnalyticsEnabled()) {
			EasyTracker.getInstance().activityStart(activity);
		}
	}
	
	public static void trackEvent(String category, String action, String label, Integer value) {
		trackEvent(category, action, label, value.longValue());
	}
	
	public static void trackEvent(String category, String action, String label, Long value) {
		if (AbstractApplication.get().getAndroidApplicationContext().isAnalyticsEnabled()) {
			EasyTracker.getTracker().trackEvent(category, action, label, value);
		}
	}
	
	public static void trackEvent(String category, String action, String label) {
		trackEvent(category, action, label, (Long)null);
	}
}

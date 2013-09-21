package com.jdroid.android.analytics;

import android.app.Activity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.jdroid.android.AbstractApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class GoogleAnalyticsTracker extends DefaultAnalyticsTracker {
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAndroidApplicationContext().isGoogleAnalyticsEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#init()
	 */
	@Override
	public void init() {
		GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(AbstractApplication.get());
		
		if (AbstractApplication.get().getAndroidApplicationContext().isGoogleAnalyticsDebugEnabled()) {
			googleAnalytics.getLogger().setLogLevel(LogLevel.VERBOSE);
		}
		googleAnalytics.getTracker(AbstractApplication.get().getAndroidApplicationContext().getGoogleAnalyticsTrackingId());
	}
	
	/**
	 * @see com.jdroid.android.analytics.DefaultAnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(Activity activity) {
		EasyTracker.getInstance(activity).activityStart(activity);
	}
	
	/**
	 * @see com.jdroid.android.analytics.DefaultAnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		EasyTracker.getInstance(activity).activityStop(activity);
	}
}

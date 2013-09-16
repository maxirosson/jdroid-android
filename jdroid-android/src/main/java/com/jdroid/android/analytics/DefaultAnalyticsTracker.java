package com.jdroid.android.analytics;

import android.app.Activity;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultAnalyticsTracker implements AnalyticsTracker {
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackAppInstallation()
	 */
	@Override
	public void trackAppInstallation() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(Activity activity) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(ConnectionException connectionException) {
		// Do Nothing
	}
	
}
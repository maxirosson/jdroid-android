package com.jdroid.android.analytics;

import android.app.Activity;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public interface AnalyticsTracker {
	
	public Boolean isEnabled();
	
	public void trackAppInstallation();
	
	public void onActivityStart(Activity activity);
	
	public void onActivityStop(Activity activity);
	
	public void trackConnectionException(ConnectionException connectionException);
}

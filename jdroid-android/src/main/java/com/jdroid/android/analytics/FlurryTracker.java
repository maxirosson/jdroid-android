package com.jdroid.android.analytics;

import java.util.Map;
import android.app.Activity;
import android.util.Log;
import com.flurry.android.FlurryAgent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public class FlurryTracker implements AnalyticsTracker {
	
	public static final String APP_INSTALL_SENT = "appInstallSent";
	private static final String CONNECTION_EXCEPTION = "connectionException";
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAndroidApplicationContext().isFlurryEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(Activity activity) {
		DefaultApplicationContext appContext = AbstractApplication.get().getAndroidApplicationContext();
		if (appContext.isFlurryDebugEnabled()) {
			FlurryAgent.setLogEnabled(true);
			FlurryAgent.setLogLevel(Log.VERBOSE);
		} else {
			FlurryAgent.setLogEnabled(false);
		}
		
		FlurryAgent.setCaptureUncaughtExceptions(false);
		FlurryAgent.setVersionName(AndroidUtils.getVersionName());
		FlurryAgent.onStartSession(activity, appContext.getFlurryApiKey());
		FlurryAgent.setUserId(AbstractApplication.get().getInstallationId());
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		FlurryAgent.onEndSession(activity);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackAppInstallation()
	 */
	@Override
	public void trackAppInstallation() {
		Boolean appLoadSent = SharedPreferencesUtils.loadPreferenceAsBoolean(APP_INSTALL_SENT, false);
		if (!appLoadSent) {
			Map<String, String> params = Maps.newHashMap();
			params.put("installationSource", "GooglePlay");
			Boolean installedOnSdCard = AndroidUtils.isInstalledOnSdCard();
			params.put("installedOnSdCard", installedOnSdCard.toString());
			params.put("availableInternalDataSize", getDataRange(AndroidUtils.getAvailableInternalDataSize()));
			params.put("totalInternalDataSize", getDataRange(AndroidUtils.getTotalInternalDataSize()));
			params.put("apiLevel", AndroidUtils.getApiLevel().toString());
			params.put("screenSize", AndroidUtils.getScreenSize());
			params.put("screenDensity", AndroidUtils.getScreenDensity());
			params.put("deviceName", AndroidUtils.getDeviceName());
			trackEvent("appInstall", params);
			SharedPreferencesUtils.savePreference(APP_INSTALL_SENT, true);
		}
	}
	
	protected void trackEvent(String eventId, Map<String, String> params) {
		if ((params != null) && !params.isEmpty()) {
			FlurryAgent.logEvent(eventId, params);
		} else {
			FlurryAgent.logEvent(eventId);
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(ConnectionException connectionException) {
		Map<String, String> params = Maps.newHashMap();
		params.put("apiLevel", AndroidUtils.getApiLevel().toString());
		params.put("deviceName", AndroidUtils.getDeviceName());
		params.put("httpImplementation", "Apache");
		params.put("errorMessage", connectionException.getMessage());
		
		trackEvent(CONNECTION_EXCEPTION, params);
	}
	
	protected String getDataRange(long dataSize) {
		int number = 2;
		int power = 1;
		while (dataSize > Math.pow(number, power)) {
			power++;
		}
		StringBuilder builder = new StringBuilder();
		builder.append((int)Math.pow(number, power - 1));
		builder.append('-');
		builder.append((int)Math.pow(number, power));
		
		return builder.toString();
	}
	
}

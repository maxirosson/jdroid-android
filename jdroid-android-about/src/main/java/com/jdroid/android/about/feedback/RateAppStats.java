package com.jdroid.android.about.feedback;

import android.support.annotation.WorkerThread;

import com.jdroid.android.about.AboutRemoteConfigParameter;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.date.DateUtils;

public class RateAppStats {

	private static final String RATE_APP_STATS = "rateAppStats";
	private static final String LAST_RESPONSE_TIMESTAMP = "lastResponseTimestamp";
	private static final String ENJOYING = "enjoying";
	private static final String GIVE_FEEDBACK = "giveFeedback";
	private static final String RATE_ON_GOOGLE_PLAY = "rateOnGooglePlay";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	public static void setEnjoyingApp(Boolean enjoying) {
		getSharedPreferencesHelper().savePreferenceAsync(ENJOYING, enjoying);
	}

	@WorkerThread
	public static Boolean getEnjoyingApp() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(ENJOYING);
	}

	public static void setGiveFeedback(Boolean feedback) {
		getSharedPreferencesHelper().savePreferenceAsync(GIVE_FEEDBACK, feedback);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis());
	}

	@WorkerThread
	public static Boolean getGiveFeedback() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(GIVE_FEEDBACK);
	}

	public static void setRateOnGooglePlay(Boolean rate) {
		getSharedPreferencesHelper().savePreferenceAsync(RATE_ON_GOOGLE_PLAY, rate);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis());
	}

	@WorkerThread
	public static Boolean getRateOnGooglePlay() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(RATE_ON_GOOGLE_PLAY);
	}

	@WorkerThread
	public static Long getLastResponseTimestamp() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(LAST_RESPONSE_TIMESTAMP, 0L);
	}

	@WorkerThread
	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(RATE_APP_STATS);
		}
		return sharedPreferencesHelper;
	}

	// FIXME Find a way to call this method from UI thread
	//@WorkerThread
	public static Boolean displayRateAppView() {
		Boolean alreadyRated = getRateOnGooglePlay();
		Boolean enoughDaysSinceLastResponse = DateUtils.millisecondsToDays(RateAppStats.getLastResponseTimestamp()) >= AbstractApplication.get().getRemoteConfigLoader().getLong(AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_LAST_RESPONSE);
		Boolean enoughDaysSinceFirstAppLoad = DateUtils.millisecondsToDays(UsageStats.getFirstAppLoadTimestamp()) >= AbstractApplication.get().getRemoteConfigLoader().getLong(AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_FIRST_APP_LOAD);
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= AbstractApplication.get().getRemoteConfigLoader().getLong(AboutRemoteConfigParameter.RATE_APP_MIN_APP_LOADS);
		Boolean enoughDaysSinceLastCrash = DateUtils.millisecondsToDays(UsageStats.getLastCrashTimestamp()) >= AbstractApplication.get().getRemoteConfigLoader().getLong(AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_LAST_CRASH);
		return (alreadyRated == null || !alreadyRated) && enoughDaysSinceLastResponse && enoughDaysSinceFirstAppLoad && enoughAppLoads && enoughDaysSinceLastCrash;
	}
}

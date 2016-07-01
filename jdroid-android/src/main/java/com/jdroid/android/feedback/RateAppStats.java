package com.jdroid.android.feedback;

import com.jdroid.android.context.UsageStats;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.date.DateUtils;

public class RateAppStats {

	private RateAppStats(){}

	private static final String RATE_APP_STATS = "rateAppStats";
	private static final String LAST_RESPONSE_TIMESTAMP = "lastResponseTimestamp";
	private static final String ENJOYING = "enjoying";
	private static final String GIVE_FEEDBACK = "giveFeedback";
	private static final String RATE_ON_GOOGLE_PLAY = "rateOnGooglePlay";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	public static void setEnjoyingApp(Boolean enjoying) {
		getSharedPreferencesHelper().savePreferenceAsync(ENJOYING, enjoying);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis());
	}

	public static Boolean getEnjoyingApp() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(ENJOYING);
	}

	public static void setGiveFeedback(Boolean feedback) {
		getSharedPreferencesHelper().savePreferenceAsync(GIVE_FEEDBACK, feedback);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis());
	}

	public static Boolean getGiveFeedback() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(GIVE_FEEDBACK);
	}

	public static void setRateOnGooglePlay(Boolean rate) {
		getSharedPreferencesHelper().savePreferenceAsync(RATE_ON_GOOGLE_PLAY, rate);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis());
	}

	public static Boolean getRateOnGooglePlay() {
		return getSharedPreferencesHelper().loadPreferenceAsBoolean(RATE_ON_GOOGLE_PLAY);
	}

	public static Long getLastResponseTimestamp() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(LAST_RESPONSE_TIMESTAMP, 0L);
	}

	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(RATE_APP_STATS);
		}
		return sharedPreferencesHelper;
	}

	public static Boolean displayRateAppView() {
		Boolean alreadyRated = getRateOnGooglePlay();
		Boolean enoughDaysSinceLastResponse =  DateUtils.millisecondsToDays(RateAppStats.getLastResponseTimestamp()) >= 90;
		Boolean enoughDaysSinceFirstAppLoad = DateUtils.millisecondsToDays(UsageStats.getFirstAppLoadTimestamp()) >= 7;
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= 10;
		Boolean enoughDaysSinceLastCrash = DateUtils.millisecondsToDays(UsageStats.getLastCrashTimestamp()) >= 21;
		return (alreadyRated == null || !alreadyRated) && enoughDaysSinceLastResponse && enoughDaysSinceFirstAppLoad && enoughAppLoads && enoughDaysSinceLastCrash;
	}
}

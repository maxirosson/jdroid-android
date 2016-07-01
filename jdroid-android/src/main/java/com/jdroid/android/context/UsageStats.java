package com.jdroid.android.context;

import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.date.DateUtils;

public class UsageStats {

	private UsageStats(){}

	private static final String USAGE_STATS = "usageStats";
	private static final String APP_LOADS = "appLoads";
	private static final String FIRST_APP_LOAD_TIMESTAMP = "firstAppLoadTimestamp";
	private static final String LAST_CRASH_TIMESTAMP = "lastCrashTimestamp";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	private static Long lastStopTime = DateUtils.nowMillis();

	public static void incrementAppLoad() {
		Long appLoads = getSharedPreferencesHelper().loadPreferenceAsLong(APP_LOADS, 0L);
		getSharedPreferencesHelper().savePreferenceAsync(APP_LOADS, appLoads + 1);
	}

	public static Long getAppLoads() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(APP_LOADS, 0L);
	}

	public static Long getLastStopTime() {
		return lastStopTime;
	}

	public static void setLastStopTime() {
		lastStopTime = DateUtils.nowMillis();
	}

	public static void setLastCrashTimestamp() {
		getSharedPreferencesHelper().savePreferenceAsync(LAST_CRASH_TIMESTAMP, DateUtils.nowMillis());
	}

	public static Long getLastCrashTimestamp() {
		return getSharedPreferencesHelper().loadPreferenceAsLong(LAST_CRASH_TIMESTAMP, 0L);
	}

	public static Long getFirstAppLoadTimestamp() {
		Long firstAppLoadTimestamp = getSharedPreferencesHelper().loadPreferenceAsLong(FIRST_APP_LOAD_TIMESTAMP);
		if (firstAppLoadTimestamp == null) {
			firstAppLoadTimestamp = DateUtils.nowMillis();
			getSharedPreferencesHelper().savePreferenceAsync(FIRST_APP_LOAD_TIMESTAMP, firstAppLoadTimestamp);
		}
		return firstAppLoadTimestamp;
	}

	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	public static void simulateHeavyUsage() {
		reset();
		getSharedPreferencesHelper().savePreferenceAsync(APP_LOADS, 100L);
		getSharedPreferencesHelper().savePreferenceAsync(FIRST_APP_LOAD_TIMESTAMP, 0L);
		getSharedPreferencesHelper().savePreferenceAsync(LAST_CRASH_TIMESTAMP, 0L);
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(USAGE_STATS);
		}
		return sharedPreferencesHelper;
	}
}

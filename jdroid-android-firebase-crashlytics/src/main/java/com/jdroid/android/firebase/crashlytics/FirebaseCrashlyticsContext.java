package com.jdroid.android.firebase.crashlytics;

import com.jdroid.android.context.BuildConfigUtils;

public class FirebaseCrashlyticsContext {

	public static Boolean isFirebaseCrashlyticsEnabled() {
		return BuildConfigUtils.getBuildConfigValue("FIREBASE_CRASHLYTICS_ENABLED", true);
	}
}

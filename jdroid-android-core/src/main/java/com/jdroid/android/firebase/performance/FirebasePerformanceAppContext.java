package com.jdroid.android.firebase.performance;

import com.jdroid.android.context.BuildConfigUtils;

public class FirebasePerformanceAppContext {

	public static Boolean isFirebasePerformanceEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true);
	}

}

package com.jdroid.android.firebase.performance;

import com.jdroid.android.context.BuildConfigUtils;

public class FirebasePerformanceAppContext {

	public static Boolean isFirebasePerformanceEnabled() {
		// TODO Change this default value to false, when the jdroid gradle plugin changes the default to false
		return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true);
	}

}

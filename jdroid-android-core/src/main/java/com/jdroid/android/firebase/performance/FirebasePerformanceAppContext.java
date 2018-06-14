package com.jdroid.android.firebase.performance;

import com.jdroid.android.context.AbstractAppContext;

public class FirebasePerformanceAppContext extends AbstractAppContext {

	public Boolean isFirebasePerformanceEnabled() {
		// TODO Change this default value to false, when the jdroid gradle plugin changes the default to false
		return getBuildConfigBoolean("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true);
	}

}

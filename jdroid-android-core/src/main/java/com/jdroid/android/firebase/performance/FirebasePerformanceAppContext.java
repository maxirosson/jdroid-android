package com.jdroid.android.firebase.performance;

import com.jdroid.android.context.AbstractAppContext;

public class FirebasePerformanceAppContext extends AbstractAppContext {

	public Boolean isFirebasePerformanceEnabled() {
		return getBuildConfigValue("FIREBASE_PERFORMANCE_MONITORING_ENABLED");
	}

}

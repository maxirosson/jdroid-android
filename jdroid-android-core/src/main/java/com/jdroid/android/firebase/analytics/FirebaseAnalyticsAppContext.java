package com.jdroid.android.firebase.analytics;

import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;

public class FirebaseAnalyticsAppContext {

	/**
	 * @return Whether the application has Firebase Analytics enabled or not
	 */
	public static Boolean isFirebaseAnalyticsEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_ANALYTICS_ENABLED", false) && !FirebaseTestLab.isRunningInstrumentedTests();
	}
}

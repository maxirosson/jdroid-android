package com.jdroid.android.firebase.remoteconfig;

import android.app.Activity;

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;

public class FirebaseRemoteConfigTracker extends AbstractCoreAnalyticsTracker {

	@Override
	public void onFirstActivityCreate(Activity activity) {
		if (AbstractApplication.get().getRemoteConfigLoader() instanceof FirebaseRemoteConfigLoader) {
			FirebaseRemoteConfigLoader.get().init();
		}
	}
}

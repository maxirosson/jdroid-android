package com.jdroid.android.firebase.remoteconfig;

import android.app.Activity;

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;

public class FirebaseRemoteConfigTracker extends AbstractCoreAnalyticsTracker {

	@Override
	public void onFirstActivityCreate(Activity activity) {
		FirebaseRemoteConfigLoader.get().init();
	}
}

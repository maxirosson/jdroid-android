package com.jdroid.android.firebase.crashlytics;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebaseCrashlyticsAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseCrashlyticsTracker());
	}

	@NonNull
	@Override
	public Integer getInitOrder() {
		return 1;
	}
}

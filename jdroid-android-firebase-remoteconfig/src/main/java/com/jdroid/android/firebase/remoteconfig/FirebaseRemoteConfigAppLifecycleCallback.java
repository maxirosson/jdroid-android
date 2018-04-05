package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebaseRemoteConfigAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		
		AbstractApplication.get().setRemoteConfigLoader(new FirebaseRemoteConfigHelper());
		
		AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseRemoteConfigTracker());
	}
	
	@NonNull
	@Override
	public Integer getInitOrder() {
		return 2;
	}
}

package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmContext;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

import androidx.annotation.NonNull;

public class FirebaseRemoteConfigAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().setRemoteConfigLoader(new FirebaseRemoteConfigLoader());
		AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseRemoteConfigTracker());
		FcmContext.addFcmEventsListener(new PropagateUpdatesFcmEventsListener());
		FcmContext.addFcmMessage(new FirebaseRemoteConfigFetchFcmMessage());
	}

	@NonNull
	@Override
	public Integer getInitOrder() {
		return 2;
	}
}

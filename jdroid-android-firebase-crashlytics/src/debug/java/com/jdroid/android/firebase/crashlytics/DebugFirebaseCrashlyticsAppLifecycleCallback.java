package com.jdroid.android.firebase.crashlytics;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

import io.fabric.sdk.android.Fabric;

public class DebugFirebaseCrashlyticsAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		Fabric.Builder fabricBuilder = new Fabric.Builder(context);
		fabricBuilder.kits(new Crashlytics());
		fabricBuilder.debuggable(true);
		Fabric.with(fabricBuilder.build());
	}
	
	@NonNull
	@Override
	public Integer getInitOrder() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public Boolean isEnabled() {
		return FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled();
	}
}

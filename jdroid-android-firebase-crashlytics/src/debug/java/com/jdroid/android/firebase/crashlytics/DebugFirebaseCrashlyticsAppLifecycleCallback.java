package com.jdroid.android.firebase.crashlytics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.debug.info.DebugInfoAppender;
import com.jdroid.android.debug.info.DebugInfoHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.collections.Lists;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class DebugFirebaseCrashlyticsAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		Fabric.Builder fabricBuilder = new Fabric.Builder(context);
		fabricBuilder.kits(new Crashlytics());
		fabricBuilder.debuggable(true);
		Fabric.with(fabricBuilder.build());
	}
	
	@Override
	public void onCreate(Context context) {
		DebugInfoHelper.addDebugInfoAppender(new DebugInfoAppender() {
			@Override
			public List<Pair<String, Object>> getDebugInfoProperties() {
				List<Pair<String, Object>> properties = Lists.newArrayList();
				properties.add(new Pair<String, Object>("Firebase Crashlytics Enabled", FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled()));
				return properties;
			}
		});
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

package com.jdroid.android.firebase.performance;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebasePerformanceAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().registerActivityLifecycleCallbacks(FirebasePerformanceLifecycleCallbacks.get());
	}
}

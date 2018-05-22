package com.jdroid.android.firebase.performance;

import android.content.Context;

import com.google.firebase.perf.FirebasePerformance;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebasePerformanceAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		FirebasePerformanceAppContext firebasePerformanceAppContext = new FirebasePerformanceAppContext();
		FirebasePerformance.getInstance().setPerformanceCollectionEnabled(firebasePerformanceAppContext.isFirebasePerformanceEnabled());
		if (firebasePerformanceAppContext.isFirebasePerformanceEnabled()) {
			AbstractApplication.get().registerActivityLifecycleCallbacks(FirebasePerformanceLifecycleCallbacks.get());
		}
	}
}

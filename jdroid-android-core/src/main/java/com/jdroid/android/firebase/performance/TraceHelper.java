package com.jdroid.android.firebase.performance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.utils.DeviceUtils;

public class TraceHelper {
	
	public static @Nullable Trace startTrace(@NonNull String name) {
		Trace trace = newTrace(name);
		if (trace != null) {
			trace.start();
		}
		return trace;
	}
	
	public static @Nullable Trace startTrace(@NonNull Class<?> clazz) {
		Trace trace = newTrace(clazz);
		if (trace != null) {
			trace.start();
		}
		return trace;
	}
	
	public static @Nullable Trace newTrace(@NonNull String name) {
		Trace trace = null;
		if (FirebasePerformanceAppContext.isFirebasePerformanceEnabled()) {
			trace = FirebasePerformance.getInstance().newTrace(name);
			init(trace);
		}
		return trace;
	}
	
	public static @Nullable Trace newTrace(@NonNull Class<?> clazz) {
		return newTrace(clazz.getSimpleName());
	}
	
	private static void init(@NonNull Trace trace) {
		// Attribute key must start with letter, must only contain alphanumeric characters and underscore and must not start with "firebase_", "google_" and "ga_"
		trace.putAttribute("DeviceYearClass", DeviceUtils.getDeviceYearClass().toString());
	}
}

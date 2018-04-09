package com.jdroid.android.firebase.performance;

import android.support.annotation.NonNull;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.utils.DeviceUtils;

public class TraceHelper {
	
	public static Trace startTrace(@NonNull String name) {
		Trace trace = newTrace(name);
		trace.start();
		return trace;
	}
	
	public static Trace startTrace(@NonNull Class<?> clazz) {
		Trace trace = newTrace(clazz);
		trace.start();
		return trace;
	}
	
	public static Trace newTrace(@NonNull String name) {
		Trace trace = FirebasePerformance.getInstance().newTrace(name);
		init(trace);
		return trace;
	}
	
	public static Trace newTrace(@NonNull Class<?> clazz) {
		return newTrace(clazz.getSimpleName());
	}
	
	private static void init(Trace trace) {
		// Attribute key must start with letter, must only contain alphanumeric characters and underscore and must not start with "firebase_", "google_" and "ga_"
		trace.putAttribute("DeviceYearClass", DeviceUtils.getDeviceYearClass().toString());
	}
}

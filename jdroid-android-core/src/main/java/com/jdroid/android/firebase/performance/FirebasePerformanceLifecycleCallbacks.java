package com.jdroid.android.firebase.performance;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.firebase.perf.metrics.Trace;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class FirebasePerformanceLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

	private static final FirebasePerformanceLifecycleCallbacks INSTANCE = new FirebasePerformanceLifecycleCallbacks();

	public static FirebasePerformanceLifecycleCallbacks get() {
		return INSTANCE;
	}

	private final Map<Activity, Trace> traces = Maps.newHashMap();

	private FirebasePerformanceLifecycleCallbacks() {
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle bundle) {

	}

	@Override
	public void onActivityStarted(Activity activity) {
		String name = activity.getClass().getSimpleName();
		Trace trace = TraceHelper.startTrace(name);
		if (trace != null) {
			traces.put(activity, trace);
		}
	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {
		Trace trace = traces.remove(activity);
		if (trace != null) {
			trace.stop();
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}

	@Nullable
	public Trace getTrace(Activity activity) {
		return traces.get(activity);
	}
}

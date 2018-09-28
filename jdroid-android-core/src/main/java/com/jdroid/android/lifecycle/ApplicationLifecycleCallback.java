package com.jdroid.android.lifecycle;

import android.content.Context;
import android.content.res.Configuration;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

/**
 * Callback for monitoring application lifecycle events. These callbacks are invoked on the main
 * thread, so any long operations or violating the strict mode policies should be avoided.
 */
public abstract class ApplicationLifecycleCallback implements Comparable<ApplicationLifecycleCallback> {

	@MainThread
	public void attachBaseContext(Context base) {
		// Do nothing
	}

	@MainThread
	public void onProviderInit(Context context) {
		// Do nothing
	}

	@MainThread
	public void onCreate(Context context) {
		// Do nothing
	}

	@MainThread
	public void onConfigurationChanged(Context context, Configuration newConfig) {
		// Do nothing
	}

	@MainThread
	public void onLowMemory(Context context) {
		// Do nothing
	}

	@MainThread
	public void onLocaleChanged(Context context) {
		// Do nothing
	}

	/*
	 * The order in which the application listener should be invoked, relative to other application listeners.
	 * When there are dependencies among callbacks, setting this attribute for each of them ensures that they are created in the order required by those dependencies.
	 * The value is a simple integer, with higher numbers being invoked first. Zero is the default value and negative numbers are accepted.
	 */
	@NonNull
	public Integer getInitOrder() {
		return 0;
	}

	@Override
	public int compareTo(@NonNull ApplicationLifecycleCallback o) {
		return o.getInitOrder().compareTo(getInitOrder());
	}

	public Boolean isEnabled() {
		return true;
	}
}

package com.jdroid.android.application;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;


public class AppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().onProviderInit();
	}

	@Override
	public void onLocaleChanged(Context context) {
		AbstractApplication.get().onLocaleChanged();
	}

	@NonNull
	@Override
	public Integer getInitOrder() {
		return Integer.MAX_VALUE;
	}
}

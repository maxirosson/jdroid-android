package com.jdroid.android.shortcuts;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class AppShortcutsAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().addCoreAnalyticsTracker(new AppShortcutsTracker());
	}
	
	@Override
	public void onLocaleChanged(Context context) {
		AppShortcutsHelper.registerDynamicShortcuts();
	}
	
	@Override
	public Boolean isEnabled() {
		return AppShortcutsHelper.isAppShortcutsAvailable();
	}
}

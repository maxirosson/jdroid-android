package com.jdroid.android.shortcuts;

import android.app.Activity;

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;

public class AppShortcutsTracker extends AbstractCoreAnalyticsTracker {

	@Override
	public void onFirstActivityCreate(Activity activity) {
		AppShortcutsHelper.registerDynamicShortcuts();
	}
}

package com.jdroid.android.shortcuts.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;

public class FirebaseAppShortcutsAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppShortcutsAnalyticsTracker {
	
	@Override
	public void trackPinShortcut(String shortcutName) {
		Bundle params = new Bundle();
		params.putString(FirebaseAnalytics.Param.ITEM_ID, shortcutName);
		getFirebaseAnalyticsHelper().sendEvent("pin_shortcut", params);
	}
}

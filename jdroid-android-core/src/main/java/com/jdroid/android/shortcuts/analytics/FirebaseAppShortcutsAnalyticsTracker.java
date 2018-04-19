package com.jdroid.android.shortcuts.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class FirebaseAppShortcutsAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppShortcutsAnalyticsTracker {
	
	@Override
	public void trackPinShortcut(String shortcutName) {
		Map<String, String> params = Maps.newHashMap();
		params.put(FirebaseAnalytics.Param.ITEM_ID, shortcutName);
		getFirebaseAnalyticsHelper().sendEvent("pin_shortcut", params);
	}
}

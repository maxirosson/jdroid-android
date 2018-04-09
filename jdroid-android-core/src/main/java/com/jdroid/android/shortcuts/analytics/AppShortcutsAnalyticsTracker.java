package com.jdroid.android.shortcuts.analytics;

import com.jdroid.java.analytics.AnalyticsTracker;

public interface AppShortcutsAnalyticsTracker extends AnalyticsTracker {

	public void trackPinShortcut(String shortcutName);

}

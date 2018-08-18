package com.jdroid.android.shortcuts.analytics;

import com.jdroid.java.analytics.AnalyticsSender;

import java.util.List;

public class AppShortcutsAnalyticsSender extends AnalyticsSender<AppShortcutsAnalyticsTracker> implements AppShortcutsAnalyticsTracker {

	public AppShortcutsAnalyticsSender(List<AppShortcutsAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackPinShortcut(final String shortcutName) {
		execute(new TrackingCommand() {

			@Override
			protected void track(AppShortcutsAnalyticsTracker tracker) {
				tracker.trackPinShortcut(shortcutName);
			}
		});
	}
}

package com.jdroid.android.shortcuts.analytics

import com.jdroid.java.analytics.AnalyticsSender

class AppShortcutsAnalyticsSender(trackers: List<AppShortcutsAnalyticsTracker>) :
    AnalyticsSender<AppShortcutsAnalyticsTracker>(trackers), AppShortcutsAnalyticsTracker {

    override fun trackPinShortcut(shortcutName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: AppShortcutsAnalyticsTracker) {
                tracker.trackPinShortcut(shortcutName)
            }
        })
    }
}

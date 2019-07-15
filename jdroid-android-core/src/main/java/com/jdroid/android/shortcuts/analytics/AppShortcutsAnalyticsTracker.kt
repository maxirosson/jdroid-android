package com.jdroid.android.shortcuts.analytics

import com.jdroid.java.analytics.AnalyticsTracker

interface AppShortcutsAnalyticsTracker : AnalyticsTracker {

    fun trackPinShortcut(shortcutName: String)
}

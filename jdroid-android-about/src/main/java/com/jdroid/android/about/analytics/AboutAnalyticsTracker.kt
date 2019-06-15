package com.jdroid.android.about.analytics

import com.jdroid.java.analytics.AnalyticsTracker

interface AboutAnalyticsTracker : AnalyticsTracker {

    fun trackAboutLibraryOpen(libraryKey: String)

    fun trackContactUs()
}

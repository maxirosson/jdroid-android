package com.jdroid.android.about.analytics

import com.jdroid.java.analytics.AnalyticsSender

class AboutAnalyticsSender(trackers: List<AboutAnalyticsTracker>) : AnalyticsSender<AboutAnalyticsTracker>(trackers),
    AboutAnalyticsTracker {

    override fun trackAboutLibraryOpen(libraryKey: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: AboutAnalyticsTracker) {
                tracker.trackAboutLibraryOpen(libraryKey)
            }
        })
    }

    override fun trackContactUs() {
        execute(object : TrackingCommand() {

            override fun track(tracker: AboutAnalyticsTracker) {
                tracker.trackContactUs()
            }
        })
    }
}

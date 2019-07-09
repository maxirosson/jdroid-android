package com.jdroid.android.sample.analytics

import com.jdroid.android.sample.firebase.analytics.FirebaseAppAnalyticsTracker
import com.jdroid.java.analytics.AnalyticsSender
import com.jdroid.java.collections.Lists

object AppAnalyticsSender : AnalyticsSender<AppAnalyticsTracker>(Lists.newArrayList(FirebaseAppAnalyticsTracker())), AppAnalyticsTracker {

    override fun trackExampleEvent() {
        execute(object : TrackingCommand() {
            override fun track(tracker: AppAnalyticsTracker) {
                tracker.trackExampleEvent()
            }
        })
    }

    override fun trackExampleTransaction() {
        execute(object : TrackingCommand() {

            override fun track(tracker: AppAnalyticsTracker) {
                tracker.trackExampleTransaction()
            }
        })
    }

    override fun trackExampleTiming() {
        execute(object : TrackingCommand() {

            override fun track(tracker: AppAnalyticsTracker) {
                tracker.trackExampleTiming()
            }
        })
    }
}

package com.jdroid.android.firebase.admob.analytics

import com.jdroid.java.analytics.AnalyticsSender

class AdMobAnalyticsSender(trackers: List<AdMobAnalyticsTracker>) : AnalyticsSender<AdMobAnalyticsTracker>(trackers),
    AdMobAnalyticsTracker {

    override fun trackRemoveAdsBannerClicked() {
        execute(object : TrackingCommand() {

            override fun track(tracker: AdMobAnalyticsTracker) {
                tracker.trackRemoveAdsBannerClicked()
            }
        })
    }
}

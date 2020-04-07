package com.jdroid.android.google.inappbilling.analytics

import com.jdroid.android.google.inappbilling.client.Product
import com.jdroid.java.analytics.AnalyticsSender

class InAppBillingAnalyticsSender(trackers: List<InAppBillingAnalyticsTracker>) :
    AnalyticsSender<InAppBillingAnalyticsTracker>(trackers), InAppBillingAnalyticsTracker {

    override fun trackInAppBillingPurchaseTry(product: Product) {
        execute(object : TrackingCommand() {

            override fun track(tracker: InAppBillingAnalyticsTracker) {
                tracker.trackInAppBillingPurchaseTry(product)
            }
        })
    }

    override fun trackInAppBillingPurchase(product: Product) {
        execute(object : TrackingCommand() {

            override fun track(tracker: InAppBillingAnalyticsTracker) {
                tracker.trackInAppBillingPurchase(product)
            }
        })
    }
}

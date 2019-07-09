package com.jdroid.android.google.inappbilling.analytics

import com.jdroid.android.google.inappbilling.client.Product
import com.jdroid.java.analytics.AnalyticsTracker

interface InAppBillingAnalyticsTracker : AnalyticsTracker {

    fun trackInAppBillingPurchaseTry(product: Product)

    fun trackInAppBillingPurchase(product: Product)
}

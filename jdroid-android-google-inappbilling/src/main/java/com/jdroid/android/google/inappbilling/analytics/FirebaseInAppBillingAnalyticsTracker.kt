package com.jdroid.android.google.inappbilling.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams
import com.jdroid.android.google.inappbilling.client.Product

class FirebaseInAppBillingAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), InAppBillingAnalyticsTracker {

    override fun trackInAppBillingPurchaseTry(product: Product) {
        val params = FirebaseAnalyticsParams()
        params.put(FirebaseAnalytics.Param.ITEM_ID, product.id)
        getFirebaseAnalyticsFacade().sendEvent("in_app_purchase_try", params)
    }

    override fun trackInAppBillingPurchase(product: Product) {
        // Tracked automatically by Firebase
    }
}

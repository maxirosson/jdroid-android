package com.jdroid.android.about.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams

class FirebaseAboutAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), AboutAnalyticsTracker {

    override fun trackAboutLibraryOpen(libraryKey: String) {
        val params = FirebaseAnalyticsParams()
        params.put(FirebaseAnalytics.Param.ITEM_ID, libraryKey)
        getFirebaseAnalyticsFacade().sendEvent("open_library", params)
    }

    override fun trackContactUs() {
        getFirebaseAnalyticsFacade().sendEvent("contact_us")
    }
}

package com.jdroid.android.firebase.admob.analytics

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker

class FirebaseAdMobAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), AdMobAnalyticsTracker {

    override fun trackRemoveAdsBannerClicked() {
        getFirebaseAnalyticsFacade().sendEvent("removed_ads")
    }
}

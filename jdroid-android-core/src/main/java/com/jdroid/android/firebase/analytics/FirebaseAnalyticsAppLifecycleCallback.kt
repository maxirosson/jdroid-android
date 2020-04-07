package com.jdroid.android.firebase.analytics

import android.content.Context
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FirebaseAnalyticsAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        AbstractApplication.get().addCoreAnalyticsTracker(FirebaseCoreAnalyticsTracker())
    }

    override fun isEnabled(): Boolean {
        return FirebaseAnalyticsHelper.isFirebaseAnalyticsEnabled()
    }
}

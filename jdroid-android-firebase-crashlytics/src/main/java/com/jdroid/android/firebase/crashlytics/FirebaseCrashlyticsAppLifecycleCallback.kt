package com.jdroid.android.firebase.crashlytics

import android.content.Context

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FirebaseCrashlyticsAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        AbstractApplication.get().addCoreAnalyticsTracker(FirebaseCrashlyticsTracker())
    }

    override fun getInitOrder(): Int {
        return 1
    }
}

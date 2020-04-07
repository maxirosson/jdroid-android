package com.jdroid.android.firebase.remoteconfig

import android.app.Activity

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker
import com.jdroid.android.application.AbstractApplication

class FirebaseRemoteConfigTracker : AbstractCoreAnalyticsTracker() {

    override fun onFirstActivityCreate(activity: Activity) {
        if (AbstractApplication.get().remoteConfigLoader is FirebaseRemoteConfigLoader) {
            FirebaseRemoteConfigLoader.get().fetch()
        }
    }

    override fun onActivityResume(activity: Activity) {
        super.onActivityResume(activity)

        // TODO We should fetch here only if it is stale
    }
}

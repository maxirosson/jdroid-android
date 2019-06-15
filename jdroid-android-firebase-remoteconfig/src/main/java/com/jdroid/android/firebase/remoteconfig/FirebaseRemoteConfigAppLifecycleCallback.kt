package com.jdroid.android.firebase.remoteconfig

import android.content.Context

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.fcm.FcmContext
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FirebaseRemoteConfigAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        AbstractApplication.get().remoteConfigLoader = FirebaseRemoteConfigLoader()
        AbstractApplication.get().addCoreAnalyticsTracker(FirebaseRemoteConfigTracker())
        FcmContext.addFcmEventsListener(PropagateUpdatesFcmEventsListener())
        FcmContext.addFcmMessage(FirebaseRemoteConfigFetchFcmMessage())
    }

    override fun getInitOrder(): Int {
        return 2
    }
}

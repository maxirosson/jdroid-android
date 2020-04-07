package com.jdroid.android.firebase.crashlytics

import com.jdroid.android.context.BuildConfigUtils

object FirebaseCrashlyticsContext {

    fun isFirebaseCrashlyticsEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigValue<Boolean>("FIREBASE_CRASHLYTICS_ENABLED", true)
    }
}

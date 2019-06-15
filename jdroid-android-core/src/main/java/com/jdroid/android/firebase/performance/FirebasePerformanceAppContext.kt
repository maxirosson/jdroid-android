package com.jdroid.android.firebase.performance

import com.jdroid.android.context.BuildConfigUtils

object FirebasePerformanceAppContext {

    fun isFirebasePerformanceEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true)
    }
}

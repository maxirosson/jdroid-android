package com.jdroid.android.firebase.analytics

import com.jdroid.android.context.BuildConfigUtils
import com.jdroid.android.firebase.testlab.FirebaseTestLab

object FirebaseAnalyticsHelper {
    /**
     * @return Whether the application has Firebase Analytics enabled or not
     */
    fun isFirebaseAnalyticsEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_ANALYTICS_ENABLED", true)!! && !FirebaseTestLab.isRunningInstrumentedTests
    }
}
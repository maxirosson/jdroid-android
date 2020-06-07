package com.jdroid.android.firebase.crashlytics

import android.content.Context
import androidx.core.util.Pair
import com.jdroid.android.debug.info.DebugInfoAppender
import com.jdroid.android.debug.info.DebugInfoHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class DebugFirebaseCrashlyticsAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onCreate(context: Context) {
        DebugInfoHelper.addDebugInfoAppender(object : DebugInfoAppender {
            override fun getDebugInfoProperties(): List<Pair<String, Any>> {
                val properties = mutableListOf<Pair<String, Any>>()
                properties.add(
                    Pair(
                        "Firebase Crashlytics Enabled",
                        FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled()
                    )
                )
                return properties
            }
        })
    }

    override fun getInitOrder(): Int {
        return Integer.MAX_VALUE
    }

    override fun isEnabled(): Boolean {
        return FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled()
    }
}

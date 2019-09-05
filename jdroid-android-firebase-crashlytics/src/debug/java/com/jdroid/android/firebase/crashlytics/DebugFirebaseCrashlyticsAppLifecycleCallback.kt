package com.jdroid.android.firebase.crashlytics

import android.content.Context
import androidx.core.util.Pair
import com.crashlytics.android.Crashlytics
import com.jdroid.android.debug.info.DebugInfoAppender
import com.jdroid.android.debug.info.DebugInfoHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import io.fabric.sdk.android.Fabric

class DebugFirebaseCrashlyticsAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        val fabricBuilder = Fabric.Builder(context)
        fabricBuilder.kits(Crashlytics())
        fabricBuilder.debuggable(true)
        Fabric.with(fabricBuilder.build())
    }

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

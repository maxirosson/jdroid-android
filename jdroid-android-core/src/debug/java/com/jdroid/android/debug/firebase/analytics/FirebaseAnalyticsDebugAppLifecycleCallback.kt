package com.jdroid.android.debug.firebase.analytics

import android.content.Context
import androidx.core.util.Pair

import com.jdroid.android.debug.info.DebugInfoAppender
import com.jdroid.android.debug.info.DebugInfoHelper
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFactory
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.java.collections.Lists

class FirebaseAnalyticsDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onCreate(context: Context) {
        DebugInfoHelper.addDebugInfoAppender(object : DebugInfoAppender {
            override fun getDebugInfoProperties(): List<Pair<String, Any>> {
                val properties = Lists.newArrayList<Pair<String, Any>>()
                properties.add(
                    Pair(
                        "Firebase Analytics Enabled",
                        FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper().isFirebaseAnalyticsEnabled
                    )
                )
                return properties
            }
        })
    }
}

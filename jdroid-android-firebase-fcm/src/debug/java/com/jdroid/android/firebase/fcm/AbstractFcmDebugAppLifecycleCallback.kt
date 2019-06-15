package com.jdroid.android.firebase.fcm

import android.content.Context
import androidx.core.util.Pair
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.debug.info.DebugInfoAppender
import com.jdroid.android.debug.info.DebugInfoHelper
import com.jdroid.android.firebase.fcm.instanceid.InstanceIdHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.java.collections.Lists

abstract class AbstractFcmDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        DebugSettingsHelper.addPreferencesAppender(FcmDebugPrefsAppender())
    }

    override fun onCreate(context: Context) {
        DebugInfoHelper.addDebugInfoAppender(object : DebugInfoAppender {
            override fun getDebugInfoProperties(): List<Pair<String, Any>> {
                val properties = Lists.newArrayList<Pair<String, Any>>()
                properties.add(Pair("Instance ID", InstanceIdHelper.getInstanceId()))
                return properties
            }
        })
        // Load properties on a worker thread
        AppExecutors.getDiskIOExecutor().execute { InstanceIdHelper.getInstanceId() }
    }
}

package com.jdroid.android.leakcanary

import android.app.Application
import android.content.Context

import com.jdroid.android.context.BuildConfigUtils
import com.squareup.leakcanary.LeakCanary

object LeakCanaryHelper {

    fun init(context: Context) {
        LeakCanary.install(context.applicationContext as Application)
    }

    fun isLeakCanaryEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigBoolean("LEAK_CANARY_ENABLED", false)
    }
}

package com.jdroid.android.leakcanary

import android.content.Context

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class LeakCanaryAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onCreate(context: Context) {
        LeakCanaryHelper.init(context)
    }

    override fun getInitOrder(): Int {
        return Integer.MAX_VALUE - 1
    }

    override fun isEnabled(): Boolean {
        return LeakCanaryHelper.isLeakCanaryEnabled()
    }
}

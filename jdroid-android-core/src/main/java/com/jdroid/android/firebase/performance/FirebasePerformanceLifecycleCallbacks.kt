package com.jdroid.android.firebase.performance

import android.app.Activity
import android.app.Application
import android.os.Bundle

import com.google.firebase.perf.metrics.Trace
import com.jdroid.java.collections.Maps

object FirebasePerformanceLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    private val traces = Maps.newHashMap<Activity, Trace>()

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        val name = activity.javaClass.simpleName
        val trace = TraceHelper.startTrace(name)
        if (trace != null) {
            traces[activity] = trace
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        val trace = traces.remove(activity)
        trace?.stop()
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    fun getTrace(activity: Activity): Trace? {
        return traces[activity]
    }
}

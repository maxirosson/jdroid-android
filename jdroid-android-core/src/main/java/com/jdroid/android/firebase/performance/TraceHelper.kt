package com.jdroid.android.firebase.performance

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.jdroid.android.utils.DeviceUtils

object TraceHelper {

    fun startTrace(name: String): Trace? {
        val trace = newTrace(name)
        trace?.start()
        return trace
    }

    fun startTrace(clazz: Class<*>): Trace? {
        val trace = newTrace(clazz)
        trace?.start()
        return trace
    }

    fun newTrace(name: String): Trace? {
        var trace: Trace? = null
        if (FirebasePerformanceAppContext.isFirebasePerformanceEnabled()) {
            trace = FirebasePerformance.getInstance().newTrace(name)
            init(trace)
        }
        return trace
    }

    fun newTrace(clazz: Class<*>): Trace? {
        return newTrace(clazz.simpleName)
    }

    private fun init(trace: Trace) {
        // Attribute key must start with letter, must only contain alphanumeric characters and underscore and must not start with "firebase_", "google_" and "ga_"
        trace.putAttribute("DeviceYearClass", DeviceUtils.getDeviceYearClass().toString())
    }
}

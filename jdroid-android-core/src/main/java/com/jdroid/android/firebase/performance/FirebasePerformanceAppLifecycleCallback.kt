package com.jdroid.android.firebase.performance

import android.content.Context

import com.google.firebase.perf.FirebasePerformance
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FirebasePerformanceAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = FirebasePerformanceAppContext.isFirebasePerformanceEnabled()
        if (FirebasePerformanceAppContext.isFirebasePerformanceEnabled()) {
            AbstractApplication.get().registerActivityLifecycleCallbacks(FirebasePerformanceLifecycleCallbacks.get())
        }
    }
}

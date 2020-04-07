package com.jdroid.android.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {

    private var numStarted: Int = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        numStarted++
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        numStarted--
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    fun isInBackground(): Boolean? {
        // http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
        return numStarted == 0
    }
}

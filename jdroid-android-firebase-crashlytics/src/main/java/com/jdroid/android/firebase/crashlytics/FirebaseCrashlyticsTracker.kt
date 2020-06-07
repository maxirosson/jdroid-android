package com.jdroid.android.firebase.crashlytics

import android.app.Activity
import android.os.Bundle

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker
import com.jdroid.android.exception.DefaultExceptionHandler
import com.jdroid.android.uri.ReferrerUtils

class FirebaseCrashlyticsTracker : AbstractCoreAnalyticsTracker() {

    override fun trackHandledException(throwable: Throwable, tags: List<String>) {
        if (areTagsEnabled()) {
            DefaultExceptionHandler.addTags(throwable, tags)
        }
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    protected fun areTagsEnabled(): Boolean {
        return false
    }

    override fun trackErrorLog(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun trackErrorCustomKey(key: String, value: Any) {
        if (value is Boolean) {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } else if (value is Double) {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } else if (value is Float) {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } else if (value is Int) {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } else {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value.toString())
        }
    }

    override fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?) {
        FirebaseCrashlytics.getInstance().log(activity.javaClass.simpleName + " created. SavedInstanceState " + if (savedInstanceState != null) "not null" else "null")
    }

    override fun onActivityStart(activity: Activity, referrer: String?, data: Any?) {
        val messageBuilder = StringBuilder()
        messageBuilder.append(activity.javaClass.simpleName)
        messageBuilder.append(" started")
        if (!ReferrerUtils.isUndefined(referrer)) {
            messageBuilder.append(". Referrer: ")
            messageBuilder.append(referrer)
        }
        FirebaseCrashlytics.getInstance().log(messageBuilder.toString())
    }

    override fun onActivityResume(activity: Activity) {
        FirebaseCrashlytics.getInstance().log("${activity.javaClass.simpleName} resumed")
    }

    override fun onActivityPause(activity: Activity) {
        FirebaseCrashlytics.getInstance().log("${activity.javaClass.simpleName} paused")
    }

    override fun onActivityStop(activity: Activity) {
        FirebaseCrashlytics.getInstance().log("${activity.javaClass.simpleName} stopped")
    }

    override fun onActivityDestroy(activity: Activity) {
        FirebaseCrashlytics.getInstance().log("${activity.javaClass.simpleName} destroyed")
    }

    override fun onFragmentStart(screenViewName: String) {
        FirebaseCrashlytics.getInstance().log("$screenViewName started")
    }

    override fun isEnabled(): Boolean {
        return FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled()
    }
}

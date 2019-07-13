package com.jdroid.android.firebase.crashlytics

import android.app.Activity
import android.os.Bundle

import com.crashlytics.android.Crashlytics
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker
import com.jdroid.android.exception.DefaultExceptionHandler
import com.jdroid.android.uri.ReferrerUtils

class FirebaseCrashlyticsTracker : AbstractCoreAnalyticsTracker() {

    override fun trackHandledException(throwable: Throwable, tags: List<String>) {
        if (areTagsEnabled()) {
            DefaultExceptionHandler.addTags(throwable, tags)
        }
        Crashlytics.logException(throwable)
    }

    protected fun areTagsEnabled(): Boolean {
        return false
    }

    override fun trackErrorLog(message: String) {
        Crashlytics.log(message)
    }

    override fun trackErrorCustomKey(key: String, value: Any) {
        if (value is Boolean) {
            Crashlytics.setBool(key, value)
        } else if (value is Double) {
            Crashlytics.setDouble(key, value)
        } else if (value is Float) {
            Crashlytics.setFloat(key, value)
        } else if (value is Int) {
            Crashlytics.setInt(key, value)
        } else {
            Crashlytics.setString(key, value.toString())
        }
    }

    override fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?) {
        Crashlytics.log(activity.javaClass.simpleName + " created. SavedInstanceState " + if (savedInstanceState != null) "not null" else "null")
    }

    override fun onActivityStart(activity: Activity, referrer: String?, data: Any?) {
        val messageBuilder = StringBuilder()
        messageBuilder.append(activity.javaClass.simpleName)
        messageBuilder.append(" started")
        if (!ReferrerUtils.isUndefined(referrer)) {
            messageBuilder.append(". Referrer: ")
            messageBuilder.append(referrer)
        }
        Crashlytics.log(messageBuilder.toString())
    }

    override fun onActivityResume(activity: Activity) {
        Crashlytics.log("${activity.javaClass.simpleName} resumed")
    }

    override fun onActivityPause(activity: Activity) {
        Crashlytics.log("${activity.javaClass.simpleName} paused")
    }

    override fun onActivityStop(activity: Activity) {
        Crashlytics.log("${activity.javaClass.simpleName} stopped")
    }

    override fun onActivityDestroy(activity: Activity) {
        Crashlytics.log("${activity.javaClass.simpleName} destroyed")
    }

    override fun onFragmentStart(screenViewName: String) {
        Crashlytics.log("$screenViewName started")
    }

    override fun isEnabled(): Boolean {
        return FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled()
    }
}
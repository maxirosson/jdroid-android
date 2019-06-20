package com.jdroid.android.analytics

import android.app.Activity
import android.net.Uri
import android.os.Bundle

import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.jdroid.android.social.SocialAction
import com.jdroid.java.analytics.AnalyticsTracker

interface CoreAnalyticsTracker : AnalyticsTracker {

    // Error handling

    fun trackHandledException(throwable: Throwable, tags: List<String>)

    fun trackErrorLog(message: String)

    fun trackErrorCustomKey(key: String, value: Any)

    // Activity/fragment life cycle

    fun onFirstActivityCreate(activity: Activity)

    fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?)

    fun onActivityStart(activity: Activity, referrer: String?, data: Any?)

    fun onActivityResume(activity: Activity)

    fun onActivityPause(activity: Activity)

    fun onActivityStop(activity: Activity)

    fun onActivityDestroy(activity: Activity)

    fun onFragmentStart(screenViewName: String)

    // Notifications

    fun trackNotificationDisplayed(notificationName: String)

    fun trackNotificationOpened(notificationName: String)

    // Feedback

    fun trackEnjoyingApp(enjoying: Boolean)

    fun trackRateOnGooglePlay(rate: Boolean)

    fun trackGiveFeedback(feedback: Boolean)

    // Widgets

    fun trackWidgetAdded(widgetName: String)

    fun trackWidgetRemoved(widgetName: String)

    // Split Install

    fun trackSplitInstallStatus(moduleName: String, state: SplitInstallSessionState)

    fun trackSplitInstallDeferredUninstall(moduleName: String)

    // More

    fun trackUriOpened(screenName: String, uri: Uri, referrer: String)

    fun trackSocialInteraction(network: String?, socialAction: SocialAction, socialTarget: String)
}

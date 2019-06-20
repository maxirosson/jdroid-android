package com.jdroid.android.analytics

import android.app.Activity
import android.net.Uri
import android.os.Bundle

import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.social.SocialAction
import java.util.concurrent.Executor

abstract class AbstractCoreAnalyticsTracker : CoreAnalyticsTracker {

    override fun getExecutor(): Executor {
        return AppExecutors.getNetworkIOExecutor()
    }

    override fun trackErrorLog(message: String) {
        // Do Nothing
    }

    override fun trackErrorCustomKey(key: String, value: Any) {
        // Do Nothing
    }

    override fun onFirstActivityCreate(activity: Activity) {
        // Do nothing
    }

    override fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?) {
        // Do nothing
    }

    override fun onActivityStart(activity: Activity, referrer: String?, data: Any?) {
        // Do Nothing
    }

    override fun onActivityResume(activity: Activity) {
        // Do Nothing
    }

    override fun onActivityPause(activity: Activity) {
        // Do Nothing
    }

    override fun onActivityStop(activity: Activity) {
        // Do Nothing
    }

    override fun onActivityDestroy(activity: Activity) {
        // Do Nothing
    }

    override fun onFragmentStart(screenViewName: String) {
        // Do Nothing
    }

    override fun trackHandledException(throwable: Throwable, tags: List<String>) {
        // Do Nothing
    }

    override fun trackUriOpened(screenName: String, uri: Uri, referrer: String) {
        // Do Nothing
    }

    override fun trackSocialInteraction(network: String?, socialAction: SocialAction, socialTarget: String) {
        // Do Nothing
    }

    override fun trackNotificationDisplayed(notificationName: String) {
        // Do Nothing
    }

    override fun trackNotificationOpened(notificationName: String) {
        // Do Nothing
    }

    override fun trackEnjoyingApp(enjoying: Boolean) {
        // Do Nothing
    }

    override fun trackRateOnGooglePlay(rate: Boolean) {
        // Do Nothing
    }

    override fun trackGiveFeedback(feedback: Boolean) {
        // Do Nothing
    }

    // Widgets

    override fun trackWidgetAdded(widgetName: String) {
        // Do Nothing
    }

    override fun trackWidgetRemoved(widgetName: String) {
        // Do Nothing
    }

    // Split Install

    override fun trackSplitInstallStatus(moduleName: String, state: SplitInstallSessionState) {
        // Do Nothing
    }

    override fun trackSplitInstallDeferredUninstall(moduleName: String) {
        // Do Nothing
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
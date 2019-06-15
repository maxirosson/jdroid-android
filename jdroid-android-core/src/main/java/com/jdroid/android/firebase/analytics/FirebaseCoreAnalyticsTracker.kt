package com.jdroid.android.firebase.analytics

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jdroid.android.analytics.CoreAnalyticsTracker
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.social.SocialAction
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.DeviceUtils
import com.jdroid.android.utils.ScreenUtils

class FirebaseCoreAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), CoreAnalyticsTracker {

    companion object {
        private const val INSTALLATION_SOURCE_USER_PROPERTY = "INSTALLATION_SOURCE"
        private const val DEVICE_YEAR_CLASS_USER_PROPERTY = "DEVICE_YEAR_CLASS"
        private const val SCREEN_WIDTH = "SCREEN_WIDTH"
        private const val SCREEN_HEIGHT = "SCREEN_HEIGHT"
        private const val SCREEN_DENSITY = "SCREEN_DENSITY"
        private const val SCREEN_DENSITY_DPI = "SCREEN_DENSITY_DPI"
        private const val INSTALLER_PACKAGE_NAME = "INSTALLER_PACKAGE_NAME"
    }

    private var firstTrackingSent: Boolean = false

    override fun trackHandledException(throwable: Throwable, tags: List<String>) {
        // Do nothing
    }

    override fun trackErrorLog(message: String) {
        // Do nothing
    }

    override fun trackErrorCustomKey(key: String, value: Any) {
        // Do nothing
    }

    override fun onFirstActivityCreate(activity: Activity) {
        // Do nothing
    }

    override fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?) {
        // Do nothing
    }

    override fun onActivityStart(activity: Activity, referrer: String?, data: Any?) {
        if ((!firstTrackingSent)) {
            getFirebaseAnalyticsFacade().setUserProperty(
                DEVICE_YEAR_CLASS_USER_PROPERTY,
                DeviceUtils.getDeviceYearClass().toString()
            )
            getFirebaseAnalyticsFacade().setUserProperty(SCREEN_WIDTH, ScreenUtils.getScreenWidthDp()!!.toString())
            getFirebaseAnalyticsFacade().setUserProperty(SCREEN_HEIGHT, ScreenUtils.getScreenHeightDp()!!.toString())
            getFirebaseAnalyticsFacade().setUserProperty(SCREEN_DENSITY, ScreenUtils.getScreenDensity())
            getFirebaseAnalyticsFacade().setUserProperty(SCREEN_DENSITY_DPI, ScreenUtils.getDensityDpi()!!.toString())
            getFirebaseAnalyticsFacade().setUserProperty(
                INSTALLATION_SOURCE_USER_PROPERTY,
                AbstractApplication.get().installationSource
            )
            getFirebaseAnalyticsFacade().setUserProperty(INSTALLER_PACKAGE_NAME, AppUtils.getSafeInstallerPackageName())
            firstTrackingSent = true
        }
    }

    override fun onActivityResume(activity: Activity) {
        // Do nothing
    }

    override fun onActivityPause(activity: Activity) {
        // Do nothing
    }

    override fun onActivityStop(activity: Activity) {
        // Do nothing
    }

    override fun onActivityDestroy(activity: Activity) {
        // Do nothing
    }

    override fun onFragmentStart(screenViewName: String) {
        // Do nothing
    }

    override fun trackNotificationDisplayed(notificationName: String) {
        val params = FirebaseAnalyticsParams()
        params.put("notification_name", notificationName)
        getFirebaseAnalyticsFacade().sendEvent("display_notification", params)
    }

    override fun trackNotificationOpened(notificationName: String) {
        val params = FirebaseAnalyticsParams()
        params.put("notification_name", notificationName)
        getFirebaseAnalyticsFacade().sendEvent("open_notification", params)
    }

    override fun trackEnjoyingApp(enjoying: Boolean) {
        val params = FirebaseAnalyticsParams()
        params.put("enjoying", enjoying)
        getFirebaseAnalyticsFacade().sendEvent("enjoying_app", params)
    }

    override fun trackRateOnGooglePlay(rate: Boolean) {
        val params = FirebaseAnalyticsParams()
        params.put("rate", rate)
        getFirebaseAnalyticsFacade().sendEvent("rate_on_google_play", params)
    }

    override fun trackGiveFeedback(feedback: Boolean) {
        val params = FirebaseAnalyticsParams()
        params.put("feedback", feedback)
        getFirebaseAnalyticsFacade().sendEvent("give_feedback", params)
    }

    override fun trackWidgetAdded(widgetName: String) {
        val params = FirebaseAnalyticsParams()
        params.put("widget_name", widgetName)
        getFirebaseAnalyticsFacade().sendEvent("add_widget", params)
    }

    override fun trackWidgetRemoved(widgetName: String) {
        val params = FirebaseAnalyticsParams()
        params.put("widget_name", widgetName)
        getFirebaseAnalyticsFacade().sendEvent("remove_widget", params)
    }

    override fun trackUriOpened(screenName: String, uri: Uri, referrer: String) {
        val params = FirebaseAnalyticsParams()
        params.put("screen_name", screenName)
        params.put("referrer", referrer)
        getFirebaseAnalyticsFacade().sendEvent("open_uri", params)
    }

    override fun trackSocialInteraction(network: String?, socialAction: SocialAction, socialTarget: String) {
        val params = FirebaseAnalyticsParams()
        if (network != null) {
            params.put("network", network)
        }
        params.put("social_target", socialTarget)
        getFirebaseAnalyticsFacade().sendEvent(socialAction.getName(), params)
    }

    // Split Install

    override fun trackSplitInstallStatus(moduleName: String, state: SplitInstallSessionState) {
        val params = FirebaseAnalyticsParams()
        params.put("module_name", moduleName)
        var evenName: String? = null
        when (state.status()) {
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                evenName = "split_install_requires_user_confirmation"
            }
            SplitInstallSessionStatus.DOWNLOADED -> {
                evenName = "split_install_downloaded"
            }
            SplitInstallSessionStatus.INSTALLED -> {
                evenName = "split_install_installed"
            }
            SplitInstallSessionStatus.FAILED -> {
                params.put("error", state.errorCode())
                evenName = "split_install_failed"
            }
            SplitInstallSessionStatus.CANCELED -> {
                evenName = "split_install_canceled"
            }
        }
        if (evenName != null) {
            getFirebaseAnalyticsFacade().sendEvent(evenName, params)
        }
    }

    override fun trackSplitInstallUninstalled(moduleName: String) {
        val params = FirebaseAnalyticsParams()
        params.put("module_name", moduleName)
        getFirebaseAnalyticsFacade().sendEvent("split_install_uninstalled", params)
    }
}

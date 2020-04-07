package com.jdroid.android.analytics

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.jdroid.android.social.SocialAction
import com.jdroid.java.analytics.AnalyticsSender

class CoreAnalyticsSender<T : CoreAnalyticsTracker> : AnalyticsSender<T>, CoreAnalyticsTracker {

    @SafeVarargs
    constructor(vararg trackers: T) : super(*trackers) {
    }

    constructor(trackers: List<T>) : super(trackers) {}

    override fun trackErrorLog(message: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackErrorLog(message)
            }
        })
    }

    override fun trackErrorCustomKey(key: String, value: Any) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackErrorCustomKey(key, value)
            }
        })
    }

    override fun onFirstActivityCreate(activity: Activity) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onFirstActivityCreate(activity)
            }
        })
    }

    override fun onActivityCreate(activity: Activity, savedInstanceState: Bundle?) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onActivityCreate(activity, savedInstanceState)
            }
        })
    }

    override fun onActivityStart(
        activity: Activity,
        referrer: String?,
        data: Any?
    ) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onActivityStart(activity, referrer, data)
            }
        })
    }

    override fun onActivityResume(activity: Activity) {
        execute(object : TrackingCommand() {
            override fun track(tracker: T) {
                tracker.onActivityResume(activity)
            }
        })
    }

    override fun onActivityPause(activity: Activity) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onActivityPause(activity)
            }
        })
    }

    override fun onActivityStop(activity: Activity) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onActivityStop(activity)
            }
        })
    }

    override fun onActivityDestroy(activity: Activity) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onActivityDestroy(activity)
            }
        })
    }

    override fun onFragmentStart(screenViewName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.onFragmentStart(screenViewName)
            }
        })
    }

    override fun trackHandledException(throwable: Throwable, tags: List<String>) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackHandledException(throwable, tags)
            }
        }, false)
    }

    override fun trackUriOpened(screenName: String, uri: Uri, referrer: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackUriOpened(screenName, uri, referrer)
            }
        })
    }

    override fun trackSocialInteraction(
        network: String?,
        socialAction: SocialAction,
        socialTarget: String
    ) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackSocialInteraction(network, socialAction, socialTarget)
            }
        })
    }

    override fun trackNotificationDisplayed(notificationName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackNotificationDisplayed(notificationName)
            }
        })
    }

    override fun trackNotificationOpened(notificationName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackNotificationOpened(notificationName)
            }
        })
    }

    override fun trackEnjoyingApp(enjoying: Boolean) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackEnjoyingApp(enjoying)
            }
        })
    }

    override fun trackRateOnGooglePlay(rate: Boolean) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackRateOnGooglePlay(rate)
            }
        })
    }

    override fun trackGiveFeedback(feedback: Boolean) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackGiveFeedback(feedback)
            }
        })
    }

    // Widgets

    override fun trackWidgetAdded(widgetName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackWidgetAdded(widgetName)
            }
        })
    }

    override fun trackWidgetRemoved(widgetName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackWidgetRemoved(widgetName)
            }
        })
    }

    // Split Install

    override fun trackSplitInstallStatus(moduleName: String, state: SplitInstallSessionState) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackSplitInstallStatus(moduleName, state)
            }
        })
    }

    override fun trackSplitInstallDeferredUninstall(moduleName: String) {
        execute(object : TrackingCommand() {

            override fun track(tracker: T) {
                tracker.trackSplitInstallDeferredUninstall(moduleName)
            }
        })
    }
}

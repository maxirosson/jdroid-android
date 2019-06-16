package com.jdroid.android.debug

import android.app.Activity
import androidx.core.util.Pair

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.debug.appenders.ExceptionHandlingDebugPrefsAppender
import com.jdroid.android.debug.appenders.HttpCacheDebugPrefsAppender
import com.jdroid.android.debug.appenders.HttpMocksDebugPrefsAppender
import com.jdroid.android.debug.appenders.InfoDebugPrefsAppender
import com.jdroid.android.debug.appenders.NavDrawerDebugPrefsAppender
import com.jdroid.android.debug.appenders.NotificationsDebugPrefsAppender
import com.jdroid.android.debug.appenders.ServersDebugPrefsAppender
import com.jdroid.android.debug.appenders.UriMapperPrefsAppender
import com.jdroid.android.debug.appenders.UsageStatsDebugPrefsAppender
import com.jdroid.java.collections.Lists
import com.jdroid.java.collections.Maps
import com.jdroid.java.http.Server
import com.jdroid.java.remoteconfig.RemoteConfigParameter

open class DebugContext {

    private val customDebugInfoProperties = Lists.newArrayList<Pair<String, Any>>()

    fun launchActivityDebugSettingsActivity(activity: Activity) {
        ActivityLauncher.startActivity(activity, DebugSettingsActivity::class.java)
    }

    fun getCustomDebugInfoProperties(): List<Pair<String, Any>> {
        return customDebugInfoProperties
    }

    fun addCustomDebugInfoProperty(pair: Pair<String, Any>) {
        customDebugInfoProperties.add(pair)
    }

    open fun createServersDebugPrefsAppender(): ServersDebugPrefsAppender {
        return ServersDebugPrefsAppender(getServersMap())
    }

    open fun getServersMap(): Map<Class<out Server>, List<Server>> {
        return Maps.newHashMap()
    }

    open fun createExceptionHandlingDebugPrefsAppender(): ExceptionHandlingDebugPrefsAppender {
        return ExceptionHandlingDebugPrefsAppender()
    }

    open fun createHttpCacheDebugPrefsAppender(): HttpCacheDebugPrefsAppender {
        return HttpCacheDebugPrefsAppender()
    }

    open fun createNavDrawerDebugPrefsAppender(): NavDrawerDebugPrefsAppender {
        return NavDrawerDebugPrefsAppender()
    }

    open fun createHttpMocksDebugPrefsAppender(): HttpMocksDebugPrefsAppender {
        return HttpMocksDebugPrefsAppender()
    }

    open fun createInfoDebugPrefsAppender(): InfoDebugPrefsAppender {
        return InfoDebugPrefsAppender()
    }

    open fun createUsageStatsDebugPrefsAppender(): UsageStatsDebugPrefsAppender {
        return UsageStatsDebugPrefsAppender()
    }

    open fun createUriMapperPrefsAppender(): UriMapperPrefsAppender {
        return UriMapperPrefsAppender()
    }

    open fun createNotificationsDebugPrefsAppender(): NotificationsDebugPrefsAppender {
        return NotificationsDebugPrefsAppender()
    }

    open fun getCustomPreferencesAppenders(): List<PreferencesAppender> {
        return Lists.newArrayList()
    }

    open fun getUrlsToTest(): List<String> {
        return Lists.newArrayList()
    }

    open fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        return Lists.newArrayList()
    }
}

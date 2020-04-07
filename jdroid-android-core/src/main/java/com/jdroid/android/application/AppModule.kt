package com.jdroid.android.application

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment

import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.java.analytics.AnalyticsSender
import com.jdroid.java.analytics.AnalyticsTracker
import com.jdroid.java.remoteconfig.RemoteConfigParameter

interface AppModule {

    @MainThread
    fun onGooglePlayServicesUpdated()

    @MainThread
    fun createActivityDelegate(abstractFragmentActivity: AbstractFragmentActivity): ActivityDelegate?

    @MainThread
    fun createFragmentDelegate(fragment: Fragment): FragmentDelegate?

    fun getRemoteConfigParameters(): List<RemoteConfigParameter>

    // Module Analytics

    fun createModuleAnalyticsSender(analyticsTrackers: List<AnalyticsTracker>): AnalyticsSender<out AnalyticsTracker>

    fun createModuleAnalyticsTrackers(): List<AnalyticsTracker>

    fun getModuleAnalyticsSender(): AnalyticsSender<out AnalyticsTracker>
}

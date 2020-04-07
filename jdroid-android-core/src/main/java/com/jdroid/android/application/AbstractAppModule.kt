package com.jdroid.android.application

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.java.analytics.AnalyticsSender
import com.jdroid.java.analytics.AnalyticsTracker
import com.jdroid.java.remoteconfig.RemoteConfigParameter

abstract class AbstractAppModule : AppModule {

    private var analyticsSender: AnalyticsSender<out AnalyticsTracker>? = null

    override fun createModuleAnalyticsSender(analyticsTrackers: List<AnalyticsTracker>): AnalyticsSender<out AnalyticsTracker> {
        return AnalyticsSender(analyticsTrackers)
    }

    override fun createModuleAnalyticsTrackers(): List<AnalyticsTracker> {
        return listOf()
    }

    @Synchronized
    override fun getModuleAnalyticsSender(): AnalyticsSender<out AnalyticsTracker> {
        if (analyticsSender == null) {
            analyticsSender = createModuleAnalyticsSender(createModuleAnalyticsTrackers())
        }
        return analyticsSender!!
    }

    @MainThread
    override fun onGooglePlayServicesUpdated() {
        // Do Nothing
    }

    @MainThread
    override fun createActivityDelegate(abstractFragmentActivity: AbstractFragmentActivity): ActivityDelegate? {
        return null
    }

    @MainThread
    override fun createFragmentDelegate(fragment: Fragment): FragmentDelegate? {
        return null
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        return listOf()
    }
}

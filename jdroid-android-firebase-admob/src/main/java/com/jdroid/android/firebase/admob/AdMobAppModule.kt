package com.jdroid.android.firebase.admob

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.application.AbstractAppModule
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.admob.analytics.AdMobAnalyticsSender
import com.jdroid.android.firebase.admob.analytics.AdMobAnalyticsTracker
import com.jdroid.android.firebase.admob.analytics.FirebaseAdMobAnalyticsTracker
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.java.analytics.AnalyticsSender
import com.jdroid.java.analytics.AnalyticsTracker
import com.jdroid.java.remoteconfig.RemoteConfigParameter

class AdMobAppModule : AbstractAppModule() {

    override fun createActivityDelegate(abstractFragmentActivity: AbstractFragmentActivity): ActivityDelegate? {
        return AdMobActivityDelegate(abstractFragmentActivity)
    }

    override fun createFragmentDelegate(fragment: Fragment): FragmentDelegate? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    override fun createModuleAnalyticsSender(analyticsTrackers: List<AnalyticsTracker>): AnalyticsSender<out AnalyticsTracker> {
        return AdMobAnalyticsSender(analyticsTrackers as List<AdMobAnalyticsTracker>)
    }

    override fun createModuleAnalyticsTrackers(): List<AnalyticsTracker> {
        return listOf<AnalyticsTracker>(FirebaseAdMobAnalyticsTracker())
    }

    override fun getModuleAnalyticsSender(): AdMobAnalyticsSender {
        return super.getModuleAnalyticsSender() as AdMobAnalyticsSender
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        return listOf<RemoteConfigParameter>(*AdMobRemoteConfigParameter.values())
    }

    companion object {

        val MODULE_NAME: String = AdMobAppModule::class.java.name

        fun get(): AdMobAppModule {
            return AbstractApplication.get().getAppModule(MODULE_NAME) as AdMobAppModule
        }

        var adMobAppContext = AdMobAppContext()
    }
}
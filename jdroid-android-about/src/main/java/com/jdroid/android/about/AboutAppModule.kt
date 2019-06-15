package com.jdroid.android.about

import com.jdroid.android.about.analytics.AboutAnalyticsSender
import com.jdroid.android.about.analytics.AboutAnalyticsTracker
import com.jdroid.android.about.analytics.FirebaseAboutAnalyticsTracker
import com.jdroid.android.application.AbstractAppModule
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.analytics.AnalyticsSender
import com.jdroid.java.analytics.AnalyticsTracker
import com.jdroid.java.collections.Lists
import com.jdroid.java.remoteconfig.RemoteConfigParameter
import java.util.Arrays

open class AboutAppModule : AbstractAppModule() {

    companion object {

        val MODULE_NAME: String = AboutAppModule::class.java.name

        fun get(): AboutAppModule {
            return AbstractApplication.get().getAppModule(MODULE_NAME) as AboutAppModule
        }
    }

    val aboutContext: AboutContext

    init {
        aboutContext = createAboutContext()
    }

    protected open fun createAboutContext(): AboutContext {
        return AboutContext()
    }

    override fun createModuleAnalyticsSender(analyticsTrackers: List<AnalyticsTracker>): AnalyticsSender<out AnalyticsTracker> {
        return AboutAnalyticsSender(analyticsTrackers as List<AboutAnalyticsTracker>)
    }

    override fun createModuleAnalyticsTrackers(): List<AnalyticsTracker> {
        return Lists.newArrayList(FirebaseAboutAnalyticsTracker())
    }

    override fun getModuleAnalyticsSender(): AboutAnalyticsSender {
        return super.getModuleAnalyticsSender() as AboutAnalyticsSender
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        return Arrays.asList<RemoteConfigParameter>(*AboutRemoteConfigParameter.values())
    }
}
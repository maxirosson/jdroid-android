package com.jdroid.android.firebase.admob

import com.jdroid.java.remoteconfig.RemoteConfigParameter

import java.util.concurrent.TimeUnit

enum class AdMobRemoteConfigParameter private constructor(private val defaultValue: Any? = null) :
    RemoteConfigParameter {

    ADS_ENABLED(false),
    MIN_APP_LOADS_TO_DISPLAY_ADS(5L),
    MIN_DAYS_TO_DISPLAY_ADS(7L),
    MIN_SECONDS_BETWEEN_INTERSTITIALS(TimeUnit.MINUTES.toSeconds(5)),
    DEFAULT_AD_UNIT_ID;

    override fun getKey(): String {
        return name
    }

    override fun getDefaultValue(): Any {
        return AdMobAppModule.getAdMobAppContext().getBuildConfigValue(name, defaultValue)!!
    }
}

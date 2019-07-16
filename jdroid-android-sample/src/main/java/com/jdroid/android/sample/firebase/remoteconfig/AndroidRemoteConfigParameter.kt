package com.jdroid.android.sample.firebase.remoteconfig

import com.jdroid.java.remoteconfig.RemoteConfigParameter

enum class AndroidRemoteConfigParameter private constructor(private val defaultValue: Any?) : RemoteConfigParameter {

    SAMPLE_CONFIG_1("defaultConfigValue1"),
    SAMPLE_CONFIG_2("default"),
    SAMPLE_CONFIG_3(null),
    INTERSTITIAL_ENABLED(true),
    PRIVACY_POLICY_URL("https://google.com");

    override fun getKey(): String {
        return name
    }

    override fun getDefaultValue(): Any? {
        return defaultValue
    }
}

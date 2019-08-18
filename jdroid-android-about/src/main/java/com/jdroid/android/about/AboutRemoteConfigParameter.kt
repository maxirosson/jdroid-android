package com.jdroid.android.about

import com.jdroid.java.remoteconfig.RemoteConfigParameter

enum class AboutRemoteConfigParameter constructor(private val defaultValue: Any? = null) : RemoteConfigParameter {

    RATE_APP_MIN_DAYS_SINCE_LAST_RESPONSE(90),
    RATE_APP_MIN_DAYS_SINCE_FIRST_APP_LOAD(7),
    RATE_APP_MIN_APP_LOADS(10),
    RATE_APP_MIN_DAYS_SINCE_LAST_CRASH(21);

    override fun getKey(): String {
        return name
    }

    override fun getDefaultValue(): Any? {
        return AboutAppModule.get().aboutContext.getBuildConfigValue<Any>(name, defaultValue)
    }
}

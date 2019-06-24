package com.jdroid.android.context

abstract class AbstractAppContext {

    fun <T> getBuildConfigValue(property: String): T {
        return BuildConfigUtils.getBuildConfigValue(property)
    }

    fun <T> getBuildConfigValue(property: String, defaultValue: Any?): T? {
        return BuildConfigUtils.getBuildConfigValue<T>(property, defaultValue)
    }

    fun getBuildConfigBoolean(property: String, defaultValue: Boolean?): Boolean? {
        return BuildConfigUtils.getBuildConfigValue<Boolean>(property, defaultValue)
    }
}

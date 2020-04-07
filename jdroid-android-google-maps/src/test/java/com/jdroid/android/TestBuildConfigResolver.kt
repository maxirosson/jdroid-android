package com.jdroid.android

import com.jdroid.android.context.BuildConfigResolver

class TestBuildConfigResolver : BuildConfigResolver() {

    override fun <T> getBuildConfigValue(property: String, defaultValue: Any?): T? {
        return (if (props.containsKey(property)) props[property] else defaultValue) as T
    }

    companion object {

        private val props = mutableMapOf<String, Any>()

        init {
            props["BUILD_TYPE"] = "test"
        }
    }
}

package com.jdroid.android.sample

import com.jdroid.android.context.BuildConfigResolver

class TestBuildConfigResolver : BuildConfigResolver() {

    companion object {

        private val props = mutableMapOf<String, Any>()

        init {
            props["BUILD_TYPE"] = "test"
        }
    }

    override fun <T> getBuildConfigValue(property: String, defaultValue: Any?): T? {
        return (if (props.containsKey(property)) props[property] else defaultValue) as T
    }
}

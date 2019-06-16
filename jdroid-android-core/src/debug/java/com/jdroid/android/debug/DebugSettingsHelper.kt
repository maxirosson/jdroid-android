package com.jdroid.android.debug

import com.jdroid.java.collections.Lists

object DebugSettingsHelper {

    private val preferencesAppenders = Lists.newArrayList<PreferencesAppender>()

    fun getPreferencesAppenders(): List<PreferencesAppender> {
        return preferencesAppenders
    }

    fun addPreferencesAppender(preferencesAppender: PreferencesAppender) {
        preferencesAppenders.add(preferencesAppender)
    }
}

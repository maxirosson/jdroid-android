package com.jdroid.android.debug

object DebugSettingsHelper {

    private val preferencesAppenders = mutableListOf<PreferencesAppender>()

    fun getPreferencesAppenders(): List<PreferencesAppender> {
        return preferencesAppenders
    }

    fun addPreferencesAppender(preferencesAppender: PreferencesAppender) {
        preferencesAppenders.add(preferencesAppender)
    }
}

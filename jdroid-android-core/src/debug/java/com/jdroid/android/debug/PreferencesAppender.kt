package com.jdroid.android.debug

import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

abstract class PreferencesAppender : Serializable {

    abstract fun getNameResId(): Int

    abstract fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup)

    open fun isEnabled(): Boolean {
        return true
    }

    open fun getRequiredPermissions(): List<String> {
        return listOf()
    }

    open fun getSharedPreferencesName(): String? {
        return null
    }
}

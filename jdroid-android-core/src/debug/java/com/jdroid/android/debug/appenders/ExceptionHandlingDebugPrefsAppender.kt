package com.jdroid.android.debug.appenders

import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.debug.crash.CrashGenerator
import com.jdroid.android.debug.crash.ExceptionType
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.utils.IdGenerator

class ExceptionHandlingDebugPrefsAppender : PreferencesAppender() {

    companion object {
        private const val UI_THREAD_KEY = "uiThread"
        private const val CRASH_TYPE_KEY = "crashType"
    }

    override fun getNameResId(): Int {
        return R.string.jdroid_exceptionHandlingSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {

        val preference = ListPreference(activity)
        preference.key = CRASH_TYPE_KEY
        preference.setTitle(R.string.jdroid_exceptionType)
        preference.setDialogTitle(R.string.jdroid_exceptionType)
        preference.setSummary(R.string.jdroid_exceptionType)
        val entries = mutableListOf<CharSequence>()
        for (each in ExceptionType.values()) {
            entries.add(each.name)
        }
        preference.entries = entries.toTypedArray()
        preference.entryValues = entries.toTypedArray()
        preferenceGroup.addPreference(preference)

        val checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = UI_THREAD_KEY
        checkBoxPreference.setTitle(R.string.jdroid_uiThread)
        checkBoxPreference.setSummary(R.string.jdroid_uiThread)
        preferenceGroup.addPreference(checkBoxPreference)

        val crashPreference = Preference(activity)
        crashPreference.setTitle(R.string.jdroid_crash)
        crashPreference.setSummary(R.string.jdroid_crash)
        crashPreference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                val uiThread = SharedPreferencesHelper.get().loadPreferenceAsBoolean(UI_THREAD_KEY, false)
                val exceptionType = ExceptionType.valueOf(
                    SharedPreferencesHelper.get().loadPreference(
                        CRASH_TYPE_KEY,
                        ExceptionType.UNEXPECTED_EXCEPTION.name
                    )
                )
                CrashGenerator.crash(exceptionType, !uiThread)
                return true
            }
        }
        preferenceGroup.addPreference(crashPreference)

        val errorLogPreference = Preference(activity)
        errorLogPreference.setTitle(R.string.jdroid_trackErrorLog)
        errorLogPreference.setSummary(R.string.jdroid_trackErrorLog)
        errorLogPreference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                AbstractApplication.get()
                    .coreAnalyticsSender.trackErrorLog("Sample message " + IdGenerator.getIntId())
                return true
            }
        }
        preferenceGroup.addPreference(errorLogPreference)

        val customKeyPreference = Preference(activity)
        customKeyPreference.setTitle(R.string.jdroid_trackErrorCustomKey)
        customKeyPreference.setSummary(R.string.jdroid_trackErrorCustomKey)
        customKeyPreference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                AbstractApplication.get().coreAnalyticsSender.trackErrorCustomKey("Sample key", IdGenerator.getIntId())
                return true
            }
        }
        preferenceGroup.addPreference(customKeyPreference)
    }
}

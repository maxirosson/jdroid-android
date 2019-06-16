package com.jdroid.android.debug.appenders

import android.app.Activity
import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.debug.crash.ExceptionType
import com.jdroid.android.debug.http.HttpDebugConfiguration
import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService
import com.jdroid.java.collections.Lists

class HttpMocksDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_httpMocksSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        var checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = HttpDebugConfiguration.HTTP_MOCK_ENABLED
        checkBoxPreference.setTitle(R.string.jdroid_httpMockEnabledTitle)
        checkBoxPreference.setSummary(R.string.jdroid_httpMockEnabledDescription)
        preferenceGroup.addPreference(checkBoxPreference)

        checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = HttpDebugConfiguration.HTTP_MOCK_SLEEP
        checkBoxPreference.setTitle(R.string.jdroid_httpMockSleepTitle)
        checkBoxPreference.setSummary(R.string.jdroid_httpMockSleepDescription)
        // FIXME this is not working
        // checkBoxPreference.setDependency(AppContext.HTTP_MOCK_ENABLED);
        preferenceGroup.addPreference(checkBoxPreference)

        val preference = ListPreference(activity)
        preference.key = AndroidJsonMockHttpService.HTTP_MOCK_CRASH_TYPE
        preference.setTitle(R.string.jdroid_httpMockCrashType)
        preference.setDialogTitle(R.string.jdroid_httpMockCrashType)
        preference.setSummary(R.string.jdroid_httpMockCrashTypeDescription)
        val entries = Lists.newArrayList<CharSequence>()
        entries.add("None")
        for (each in ExceptionType.values()) {
            entries.add(each.name)
        }
        preference.entries = entries.toTypedArray()
        preference.entryValues = entries.toTypedArray()
        // FIXME this is not working
        // preference.setDependency(AppContext.HTTP_MOCK_ENABLED);
        preferenceGroup.addPreference(preference)

        onInitPreferenceCategory(activity, preferenceGroup)
    }

    protected fun onInitPreferenceCategory(activity: Activity, preferenceGroup: PreferenceGroup) {
        // Do nothing
    }
}

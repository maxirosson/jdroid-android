package com.jdroid.android.debug.appenders

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.context.UsageStats
import com.jdroid.android.debug.PreferencesAppender

class UsageStatsDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_usageStats
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        var preference = Preference(activity)
        preference.setTitle(R.string.jdroid_reset)
        preference.setSummary(R.string.jdroid_reset)
        preference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                UsageStats.reset()
                return true
            }
        }
        preferenceGroup.addPreference(preference)

        preference = Preference(activity)
        preference.setTitle(R.string.jdroid_simulateHeavyUsage)
        preference.setSummary(R.string.jdroid_simulateHeavyUsage)
        preference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                UsageStats.simulateHeavyUsage()
                return true
            }
        }
        preferenceGroup.addPreference(preference)
    }
}

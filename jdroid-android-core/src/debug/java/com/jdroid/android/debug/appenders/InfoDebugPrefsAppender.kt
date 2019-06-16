package com.jdroid.android.debug.appenders

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.debug.info.DebugInfoActivity

class InfoDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_debugInfoCategory
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        val crashPreference = Preference(activity)
        crashPreference.setTitle(R.string.jdroid_debugInfo)
        crashPreference.setSummary(R.string.jdroid_debugInfo)
        crashPreference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                ActivityLauncher.startActivity(activity, DebugInfoActivity::class.java)
                return true
            }
        }
        preferenceGroup.addPreference(crashPreference)
    }
}

package com.jdroid.android.debug.appenders

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.PreferencesAppender

class HttpCacheDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_httpCacheSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        val preference = Preference(activity)
        preference.setTitle(R.string.jdroid_clearHttpCache)
        preference.setSummary(R.string.jdroid_clearHttpCache)
        preference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                AbstractApplication.get().cacheManager!!.cleanFileSystemCache()
                return true
            }
        }
        preferenceGroup.addPreference(preference)
    }
}

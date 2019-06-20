package com.jdroid.android.debug.appenders

import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.java.collections.Lists
import com.jdroid.java.http.Server

class ServersDebugPrefsAppender(val serversMap: Map<Class<out Server>, List<Server>>?) : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_serversSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        for ((key, value) in serversMap!!) {
            val preference = ListPreference(activity)
            preference.key = key.simpleName
            preference.title = key.simpleName
            preference.dialogTitle = key.simpleName
            preference.summary = key.simpleName

            val entries = Lists.newArrayList<CharSequence>()
            for (each in value) {
                entries.add(each.getServerName())
            }
            preference.entries = entries.toTypedArray()
            preference.entryValues = entries.toTypedArray()
            preference.onPreferenceChangeListener = object : OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
                    for (each in value) {
                        if (each.getServerName() == newValue) {
                            onServerPreferenceChange(each)
                            break
                        }
                    }
                    return true
                }
            }
            preferenceGroup.addPreference(preference)
        }
    }

    protected fun onServerPreferenceChange(each: Server) {
        // Do nothing
    }

    override fun isEnabled(): Boolean {
        return serversMap != null && serversMap.isNotEmpty()
    }
}

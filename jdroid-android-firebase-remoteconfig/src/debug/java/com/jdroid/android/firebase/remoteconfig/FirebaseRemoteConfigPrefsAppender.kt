package com.jdroid.android.firebase.remoteconfig

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.debug.PreferencesAppender

class FirebaseRemoteConfigPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_firebaseRemoteConfigSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        val crashPreference = Preference(activity)
        crashPreference.setTitle(R.string.jdroid_firebaseRemoteConfigSettings)
        crashPreference.setSummary(R.string.jdroid_firebaseRemoteConfigSettings)
        crashPreference.onPreferenceClickListener = OnPreferenceClickListener {
            ActivityLauncher.startActivity(activity, FirebaseRemoteConfigActivity::class.java)
            true
        }
        preferenceGroup.addPreference(crashPreference)
    }
}

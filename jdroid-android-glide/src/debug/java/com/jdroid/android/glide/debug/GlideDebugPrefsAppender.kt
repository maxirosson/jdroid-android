package com.jdroid.android.glide.debug

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.glide.GlideHelper
import com.jdroid.android.glide.R

class GlideDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_glideSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        var preference = Preference(activity)
        preference.setTitle(R.string.jdroid_clearImagesDiskCache)
        preference.setSummary(R.string.jdroid_clearImagesDiskCache)
        preference.onPreferenceClickListener = OnPreferenceClickListener {
            GlideHelper.clearDiskCache()
            true
        }
        preferenceGroup.addPreference(preference)

        preference = Preference(activity)
        preference.setTitle(R.string.jdroid_clearImagesMemoryCache)
        preference.setSummary(R.string.jdroid_clearImagesMemoryCache)
        preference.onPreferenceClickListener = OnPreferenceClickListener {
            GlideHelper.clearMemory()
            true
        }
        preferenceGroup.addPreference(preference)
    }
}

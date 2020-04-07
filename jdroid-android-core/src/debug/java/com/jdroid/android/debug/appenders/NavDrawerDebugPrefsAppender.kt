package com.jdroid.android.debug.appenders

import android.preference.CheckBoxPreference
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.navdrawer.NavDrawer

class NavDrawerDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_navDrawerSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        val checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = NavDrawer.NAV_DRAWER_MANUALLY_USED
        checkBoxPreference.setTitle(R.string.jdroid_navDrawerManuallyUsedTitle)
        checkBoxPreference.setSummary(R.string.jdroid_navDrawerManuallyUsedDescription)
        preferenceGroup.addPreference(checkBoxPreference)
    }
}

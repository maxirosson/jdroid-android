package com.jdroid.android.firebase.admob

import android.preference.CheckBoxPreference
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.debug.PreferencesAppender

class AdsDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_adsSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        var checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = AdMobRemoteConfigParameter.ADS_ENABLED.getKey()
        checkBoxPreference.setTitle(R.string.jdroid_adsEnabledTitle)
        checkBoxPreference.setSummary(R.string.jdroid_adsEnabledDescription)
        preferenceGroup.addPreference(checkBoxPreference)

        checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = AdMobAppContext.TEST_AD_UNIT_ID_ENABLED
        checkBoxPreference.setTitle(R.string.jdroid_testAdUnitEnabledTitle)
        checkBoxPreference.setSummary(R.string.jdroid_testAdUnitEnabledDescription)
        preferenceGroup.addPreference(checkBoxPreference)
    }
}

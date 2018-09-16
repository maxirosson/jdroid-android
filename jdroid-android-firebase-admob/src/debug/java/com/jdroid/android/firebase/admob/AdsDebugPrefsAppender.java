package com.jdroid.android.firebase.admob;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.PreferencesAppender;

public class AdsDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_adsSettings;
	}

	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(AdMobRemoteConfigParameter.ADS_ENABLED.getKey());
		checkBoxPreference.setTitle(R.string.jdroid_adsEnabledTitle);
		checkBoxPreference.setSummary(R.string.jdroid_adsEnabledDescription);
		preferenceGroup.addPreference(checkBoxPreference);

		checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(AdMobAppContext.TEST_AD_UNIT_ID_ENABLED);
		checkBoxPreference.setTitle(R.string.jdroid_testAdUnitEnabledTitle);
		checkBoxPreference.setSummary(R.string.jdroid_testAdUnitEnabledDescription);
		preferenceGroup.addPreference(checkBoxPreference);
	}
}

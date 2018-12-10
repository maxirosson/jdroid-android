package com.jdroid.android.debug.appenders;

import android.preference.CheckBoxPreference;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.navdrawer.NavDrawer;

import androidx.appcompat.app.AppCompatActivity;

public class NavDrawerDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_navDrawerSettings;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(NavDrawer.NAV_DRAWER_MANUALLY_USED);
		checkBoxPreference.setTitle(R.string.jdroid_navDrawerManuallyUsedTitle);
		checkBoxPreference.setSummary(R.string.jdroid_navDrawerManuallyUsedDescription);
		preferenceGroup.addPreference(checkBoxPreference);
	}
}

package com.jdroid.android.firebase.remoteconfig;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.debug.PreferencesAppender;

public class FirebaseRemoteConfigPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_firebaseRemoteConfigSettings;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {
		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.jdroid_firebaseRemoteConfigSettings);
		crashPreference.setSummary(R.string.jdroid_firebaseRemoteConfigSettings);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ActivityLauncher.startActivity(activity, FirebaseRemoteConfigActivity.class);
				return true;
			}
		});
		preferenceGroup.addPreference(crashPreference);
	}
}

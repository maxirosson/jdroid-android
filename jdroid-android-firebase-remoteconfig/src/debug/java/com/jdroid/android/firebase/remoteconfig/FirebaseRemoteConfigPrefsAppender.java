package com.jdroid.android.firebase.remoteconfig;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.debug.PreferencesAppender;

import androidx.appcompat.app.AppCompatActivity;

public class FirebaseRemoteConfigPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_firebaseRemoteConfigSettings;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.jdroid_firebaseRemoteConfigSettings);
		crashPreference.setSummary(R.string.jdroid_firebaseRemoteConfigSettings);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ActivityLauncher.INSTANCE.startActivity(activity, FirebaseRemoteConfigActivity.class);
				return true;
			}
		});
		preferenceGroup.addPreference(crashPreference);
	}
}

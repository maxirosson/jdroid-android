package com.jdroid.android.about;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.firebase.invites.AppInviteStats;

import androidx.appcompat.app.AppCompatActivity;

public class AppInviteDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_debugAppInvite;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_reset);
		preference.setSummary(R.string.jdroid_reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AppInviteStats.reset();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}

package com.jdroid.android.about;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.about.feedback.RateAppStats;
import com.jdroid.android.debug.PreferencesAppender;

import androidx.appcompat.app.AppCompatActivity;

public class RateAppDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_rateApp;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_reset);
		preference.setSummary(R.string.jdroid_reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				RateAppStats.INSTANCE.reset();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}

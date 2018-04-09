package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.debug.PreferencesAppender;

public class UsageStatsDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_usageStats;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_reset);
		preference.setSummary(R.string.jdroid_reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				UsageStats.reset();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);

		preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_simulateHeavyUsage);
		preference.setSummary(R.string.jdroid_simulateHeavyUsage);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				UsageStats.simulateHeavyUsage();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}

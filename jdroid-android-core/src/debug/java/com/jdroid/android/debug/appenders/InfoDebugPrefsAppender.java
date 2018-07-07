package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.debug.info.DebugInfoActivity;
import com.jdroid.android.debug.PreferencesAppender;

public class InfoDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_debugInfoCategory;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {
		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.jdroid_debugInfo);
		crashPreference.setSummary(R.string.jdroid_debugInfo);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ActivityLauncher.startActivity(activity, DebugInfoActivity.class);
				return true;
			}
		});
		preferenceGroup.addPreference(crashPreference);
	}
}

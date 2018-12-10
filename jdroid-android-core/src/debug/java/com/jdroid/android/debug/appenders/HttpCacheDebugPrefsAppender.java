package com.jdroid.android.debug.appenders;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import androidx.appcompat.app.AppCompatActivity;

public class HttpCacheDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_httpCacheSettings;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_clearHttpCache);
		preference.setSummary(R.string.jdroid_clearHttpCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getCacheManager().cleanFileSystemCache();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}

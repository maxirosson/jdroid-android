package com.jdroid.android.glide.debug;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.glide.GlideHelper;
import com.jdroid.android.glide.R;

import androidx.appcompat.app.AppCompatActivity;

public class GlideDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_glideSettings;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_clearImagesDiskCache);
		preference.setSummary(R.string.jdroid_clearImagesDiskCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				GlideHelper.clearDiskCache();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);

		preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_clearImagesMemoryCache);
		preference.setSummary(R.string.jdroid_clearImagesMemoryCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				GlideHelper.clearMemory();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}

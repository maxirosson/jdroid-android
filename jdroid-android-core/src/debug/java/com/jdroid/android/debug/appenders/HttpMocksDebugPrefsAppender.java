package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.debug.crash.ExceptionType;
import com.jdroid.android.debug.http.HttpDebugConfiguration;
import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class HttpMocksDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_httpMocksSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(HttpDebugConfiguration.HTTP_MOCK_ENABLED);
		checkBoxPreference.setTitle(R.string.jdroid_httpMockEnabledTitle);
		checkBoxPreference.setSummary(R.string.jdroid_httpMockEnabledDescription);
		preferenceGroup.addPreference(checkBoxPreference);
		
		checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(HttpDebugConfiguration.HTTP_MOCK_SLEEP);
		checkBoxPreference.setTitle(R.string.jdroid_httpMockSleepTitle);
		checkBoxPreference.setSummary(R.string.jdroid_httpMockSleepDescription);
		// FIXME this is not working
		// checkBoxPreference.setDependency(AppContext.HTTP_MOCK_ENABLED);
		preferenceGroup.addPreference(checkBoxPreference);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(AndroidJsonMockHttpService.HTTP_MOCK_CRASH_TYPE);
		preference.setTitle(R.string.jdroid_httpMockCrashType);
		preference.setDialogTitle(R.string.jdroid_httpMockCrashType);
		preference.setSummary(R.string.jdroid_httpMockCrashTypeDescription);
		List<CharSequence> entries = Lists.newArrayList();
		entries.add("None");
		for (ExceptionType each : ExceptionType.values()) {
			entries.add(each.name());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		// FIXME this is not working
		// preference.setDependency(AppContext.HTTP_MOCK_ENABLED);
		preferenceGroup.addPreference(preference);

		onInitPreferenceCategory(activity, preferenceGroup);
	}

	protected void onInitPreferenceCategory(Activity activity, PreferenceGroup preferenceGroup) {
		// Do nothing
	}
}

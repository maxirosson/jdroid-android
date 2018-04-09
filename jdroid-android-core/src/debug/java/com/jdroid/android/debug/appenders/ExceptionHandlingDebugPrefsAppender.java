package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.crash.CrashGenerator;
import com.jdroid.android.debug.crash.ExceptionType;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

public class ExceptionHandlingDebugPrefsAppender extends PreferencesAppender {
	
	private static final String UI_THREAD_KEY = "uiThread";
	private static final String CRASH_TYPE_KEY = "crashType";

	@Override
	public int getNameResId() {
		return R.string.jdroid_exceptionHandlingSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(CRASH_TYPE_KEY);
		preference.setTitle(R.string.jdroid_exceptionType);
		preference.setDialogTitle(R.string.jdroid_exceptionType);
		preference.setSummary(R.string.jdroid_exceptionType);
		List<CharSequence> entries = Lists.newArrayList();
		for (ExceptionType each : ExceptionType.values()) {
			entries.add(each.name());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		preferenceGroup.addPreference(preference);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(UI_THREAD_KEY);
		checkBoxPreference.setTitle(R.string.jdroid_uiThread);
		checkBoxPreference.setSummary(R.string.jdroid_uiThread);
		preferenceGroup.addPreference(checkBoxPreference);
		
		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.jdroid_crash);
		crashPreference.setSummary(R.string.jdroid_crash);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				Boolean uiThread = SharedPreferencesHelper.get().loadPreferenceAsBoolean(UI_THREAD_KEY, false);
				ExceptionType exceptionType = ExceptionType.valueOf(SharedPreferencesHelper.get().loadPreference(CRASH_TYPE_KEY, ExceptionType.UNEXPECTED_EXCEPTION.name()));
				CrashGenerator.crash(exceptionType, !uiThread);
				return true;
			}
		});
		preferenceGroup.addPreference(crashPreference);

		Preference errorLogPreference = new Preference(activity);
		errorLogPreference.setTitle(R.string.jdroid_trackErrorLog);
		errorLogPreference.setSummary(R.string.jdroid_trackErrorLog);
		errorLogPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getCoreAnalyticsSender().trackErrorLog("Sample message " + IdGenerator.getIntId());
				return true;
			}
		});
		preferenceGroup.addPreference(errorLogPreference);
		
		Preference customKeyPreference = new Preference(activity);
		customKeyPreference.setTitle(R.string.jdroid_trackErrorCustomKey);
		customKeyPreference.setSummary(R.string.jdroid_trackErrorCustomKey);
		customKeyPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getCoreAnalyticsSender().trackErrorCustomKey("Sample key", IdGenerator.getIntId());
				return true;
			}
		});
		preferenceGroup.addPreference(customKeyPreference);
	}
}

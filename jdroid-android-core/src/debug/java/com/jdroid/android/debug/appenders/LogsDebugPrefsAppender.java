package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.log.DatabaseLog;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class LogsDebugPrefsAppender extends PreferencesAppender {

	private static final Logger LOGGER_UTILS = LoggerUtils.getLogger(LogsDebugPrefsAppender.class);

	@Override
	public int getNameResId() {
		return R.string.jdroid_debugDatabase;
	}

	public static void clean() {
		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {

				try {
					Repository<DatabaseLog> repository = AbstractApplication.get().getRepositoryInstance(DatabaseLog.class);
					repository.removeAll();
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		});
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_cleanDebugData);
		preference.setSummary(R.string.jdroid_cleanDebugData);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				LogsDebugPrefsAppender.clean();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}

	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().isDebugLogRepositoryEnabled();
	}
}

package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DatabaseDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_database;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_downloadDatabase);
		preference.setSummary(R.string.jdroid_downloadDatabase);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@SuppressWarnings("resource")
			@Override
			public boolean onPreferenceClick(Preference preference) {
				File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				dir.mkdirs();

				File file = new File(dir, AppUtils.getApplicationName() + ".sqlite");
				try {
					FileUtils.copyStream(new FileInputStream(SQLiteHelper.getDatabaseFile(activity)), file);
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					intent.setType(MimeType.OCTET_STREAM);
					ActivityLauncher.startActivity(activity, intent);
				} catch (FileNotFoundException e) {
					throw new UnexpectedException(e);
				}
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}

	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().isDatabaseEnabled();
	}
}

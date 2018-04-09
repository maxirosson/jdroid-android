package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;
import android.support.v4.app.ShareCompat;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UriMapperPrefsAppender extends PreferencesAppender {

	private Integer htmlRawId;

	public UriMapperPrefsAppender(Integer htmlRawId) {
		this.htmlRawId = htmlRawId;
	}
	public UriMapperPrefsAppender() {
		this(null);
	}

	@Override
	public int getNameResId() {
		return R.string.jdroid_uriMapper;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		if (htmlRawId == null) {
			htmlRawId = activity.getResources().getIdentifier("url_samples", "raw", activity.getPackageName());
		}

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_downloadUrlSample);
		preference.setSummary(R.string.jdroid_downloadUrlSample);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO To make it work on Android N, read this https://medium.com/google-developers/sharing-content-between-android-apps-2e6db9d1368b#.aauyfutg4
				try {
					File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					dir.mkdirs();

					InputStream openInputStream = activity.getResources().openRawResource(htmlRawId);
					File file = new File(dir, AppUtils.getApplicationId() + "_url_samples.html");
					FileUtils.copyStream(openInputStream, file);
					openInputStream.close();

					ExternalAppsUtils.openOnBrowser(activity, file);
				} catch (IOException e) {
					throw new UnexpectedException(e);
				}

				return true;
			}
		});
		preferenceGroup.addPreference(preference);

		preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_emailUrlSample);
		preference.setSummary(R.string.jdroid_emailUrlSample);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				StringBuilder builder = new StringBuilder();
				for (String each : AbstractApplication.get().getDebugContext().getUrlsToTest()) {
					builder.append("<h4><a href=\"");
					builder.append(each);
					builder.append("\" target=\"_blank\">");
					builder.append(each);
					builder.append("</a></h4>");
				}

				Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
						.setType("text/html")
						.setSubject(AppUtils.getApplicationName() + " urls samples")
						.setHtmlText(builder.toString())
						.getIntent();
				if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
					activity.startActivity(shareIntent);
				}

				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}

	@Override
	public List<String> getRequiredPermissions() {
		return Lists.newArrayList(PermissionHelper.WRITE_EXTERNAL_STORAGE_PERMISSION);
	}
}

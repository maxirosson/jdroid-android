package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

public class NotificationsDebugPrefsAppender extends PreferencesAppender {

	private List<String> urlsToTest;

	public NotificationsDebugPrefsAppender() {
		this.urlsToTest = AbstractApplication.get().getDebugContext().getUrlsToTest();
	}

	@Override
	public int getNameResId() {
		return R.string.jdroid_notifications;
	}

	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		for (final String url : urlsToTest) {
			Preference preference = new Preference(activity);
			preference.setTitle(url);
			preference.setSummary(url);
			preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

				@Override
				public boolean onPreferenceClick(Preference preference) {
					NotificationBuilder builder = new NotificationBuilder("notificationFromBundle", AbstractApplication.get().getNotificationChannelTypes().get(0));
					builder.setTicker("Sample Ticker");
					builder.setContentTitle("Sample Content Title");
					builder.setContentText(url);
					builder.setSingleTopUrl(url);
					builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
					builder.setWhen(DateUtils.nowMillis());

					NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
					return true;
				}
			});
			preferenceGroup.addPreference(preference);
		}
	}

	@Override
	public Boolean isEnabled() {
		return !Lists.isNullOrEmpty(urlsToTest);
	}
}

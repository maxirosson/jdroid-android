package com.jdroid.android.sample.application;

import android.app.NotificationChannel;
import android.os.Build;

import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.LocalizationUtils;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

public enum AndroidNotificationChannelType implements NotificationChannelType {

	LOW_IMPORTANCE("lowImportance", R.string.lowImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_LOW) {
		@Override
		public void config(NotificationChannel notificationChannel) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				notificationChannel.setDescription("This is a sample description for low importance notification channel");
				notificationChannel.setShowBadge(false);
			}
		}
	},
	DEFAULT_IMPORTANCE("defaultImportance", R.string.defaultImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_DEFAULT),
	HIGH_IMPORTANCE("highImportance", R.string.highImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_HIGH) {
		@Override
		public void config(NotificationChannel notificationChannel) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				notificationChannel.setShowBadge(true);
			}
		}
	};

	private String channelId;

	@StringRes
	private int nameResId;

	private int importance;
	private Boolean isDeprecated;

	AndroidNotificationChannelType(String channelId, @StringRes int nameResId, int importance) {
		this(channelId, nameResId, importance, false);
	}

	AndroidNotificationChannelType(String channelId, @StringRes int nameResId, int importance, Boolean isDeprecated) {
		this.channelId = channelId;
		this.nameResId = nameResId;
		this.importance = importance;
		this.isDeprecated = isDeprecated;
	}

	@Override
	public void config(NotificationChannel notificationChannel) {
		// Do nothing
	}

	@Override
	public String getChannelId() {
		return channelId;
	}

	@Override
	public String getUserVisibleName() {
		return LocalizationUtils.getString(nameResId);
	}

	@Override
	public int getImportance() {
		return importance;
	}

	@Override
	public boolean isDeprecated() {
		return isDeprecated;
	}
}

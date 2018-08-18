package com.jdroid.android.notification;

import android.app.NotificationChannel;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class NotificationChannelFactory {

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static NotificationChannel createNotificationChannel(NotificationChannelType notificationChannelType) {
		NotificationChannel notificationChannel = new NotificationChannel(notificationChannelType.getChannelId(), notificationChannelType.getName(), notificationChannelType.getImportance());
		notificationChannelType.config(notificationChannel);
		return notificationChannel;
	}
}

package com.jdroid.android.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class NotificationUtils {

	@NonNull
	private final static NotificationManager NOTIFICATION_MANAGER = (NotificationManager)AbstractApplication.get().getSystemService(
		Context.NOTIFICATION_SERVICE);

	private static final List<NotificationChannelType> NOTIFICATION_CHANNEL_TYPES = Lists.newArrayList();

	public static void sendNotification(int id, NotificationBuilder notificationBuilder) {
		sendNotification(id, notificationBuilder.build());
	}

	public static void sendNotification(int id, Notification notification) {
		if (notification != null) {
			NOTIFICATION_MANAGER.notify(id, notification);
		}
	}

	public static void cancelNotification(int id) {
		NOTIFICATION_MANAGER.cancel(id);
	}

	/**
	 * Cancel all previously shown notifications.
	 */
	public static void cancelAllNotifications() {
		NOTIFICATION_MANAGER.cancelAll();
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createNotificationChannel(NotificationChannel notificationChannel) {
		NOTIFICATION_MANAGER.createNotificationChannel(notificationChannel);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createNotificationChannels(List<NotificationChannel> notificationChannels) {
		NOTIFICATION_MANAGER.createNotificationChannels(notificationChannels);
	}

	public static void createNotificationChannelsByType(List<NotificationChannelType> notificationChannelTypes) {
		NOTIFICATION_CHANNEL_TYPES.clear();
		NOTIFICATION_CHANNEL_TYPES.addAll(notificationChannelTypes);
		for (NotificationChannelType notificationChannelType : notificationChannelTypes) {
			if (notificationChannelType.isDeprecated()) {
				NOTIFICATION_CHANNEL_TYPES.remove(notificationChannelType);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					NOTIFICATION_MANAGER.deleteNotificationChannel(notificationChannelType.getChannelId());
				}
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					NotificationUtils.createNotificationChannel(NotificationChannelFactory.INSTANCE.createNotificationChannel(notificationChannelType));
				}
			}
		}
	}

	public static NotificationChannelType findNotificationChannelType(@Nullable String channelId) {
		for (NotificationChannelType notificationChannelType : NOTIFICATION_CHANNEL_TYPES) {
			if (notificationChannelType.getChannelId().equals(channelId)) {
				return notificationChannelType;
			}
		}
		return null;
	}

	public static int getNotificationLargeIconWidthPx() {
		return ScreenUtils.INSTANCE.convertDimenToPixel(android.R.dimen.notification_large_icon_width);
	}

	public static int getNotificationLargeIconHeightPx() {
		return ScreenUtils.INSTANCE.convertDimenToPixel(android.R.dimen.notification_large_icon_height);
	}
}
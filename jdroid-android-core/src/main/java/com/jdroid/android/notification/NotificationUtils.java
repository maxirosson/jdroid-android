package com.jdroid.android.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class NotificationUtils {
	
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NOTIFICATION_CHANNEL_TYPES.clear();
			NOTIFICATION_CHANNEL_TYPES.addAll(notificationChannelTypes);
			for (NotificationChannelType notificationChannelType : notificationChannelTypes) {
				if (notificationChannelType.isDeprecated()) {
					NOTIFICATION_MANAGER.deleteNotificationChannel(notificationChannelType.getChannelId());
				} else {
					NotificationUtils.createNotificationChannel(NotificationChannelFactory.createNotificationChannel(notificationChannelType));
				}
			}
		}
	}
	
	public static NotificationChannelType findNotificationChannelType(String channelId) {
		for (NotificationChannelType notificationChannelType : NOTIFICATION_CHANNEL_TYPES) {
			if (notificationChannelType.getChannelId().equals(channelId)) {
				return notificationChannelType;
			}
		}
		return null;
	}
	
	public static int getNotificationLargeIconWidthPx() {
		return ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_width);
	}
	
	public static int getNotificationLargeIconHeightPx() {
		return ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_height);
	}
}
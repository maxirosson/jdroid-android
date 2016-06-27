package com.jdroid.android.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ScreenUtils;

public class NotificationUtils {
	
	private static final NotificationManager NOTIFICATION_MANAGER = (NotificationManager)AbstractApplication.get().getSystemService(
		Context.NOTIFICATION_SERVICE);
	
	public static void sendNotification(int id, NotificationBuilder notificationBuilder) {
		sendNotification(id, notificationBuilder.build());
	}
	
	public static void sendNotification(int id, Notification notification) {
		NOTIFICATION_MANAGER.notify(id, notification);
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
	
	public static int getNotificationLargeIconWidthPx() {
		return ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_width);
	}
	
	public static int getNotificationLargeIconHeightPx() {
		return ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_height);
	}
}
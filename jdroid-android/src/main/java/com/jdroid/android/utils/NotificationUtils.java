package com.jdroid.android.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.jdroid.android.AbstractApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class NotificationUtils {
	
	private final static NotificationManager NOTIFICATION_MANAGER = (NotificationManager)AbstractApplication.get().getSystemService(
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
}
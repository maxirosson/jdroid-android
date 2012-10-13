package com.jdroid.android.utils;

import org.apache.commons.lang.StringUtils;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.images.RemoteImageResolver;

/**
 * 
 * @author Maxi Rosson
 */
public class NotificationUtils {
	
	private final static int LARGE_ICON_WIDHT = 100;
	private final static int LARGE_ICON_HEIGHT = 100;
	
	private final static NotificationManager NOTIFICATION_MANAGER = (NotificationManager)AbstractApplication.get().getSystemService(
		Context.NOTIFICATION_SERVICE);
	
	public static void sendNotification(int id, int smallIcon, String tickerText, String contentTitle,
			String contentText, Class<?> clazz, Long when, Bundle notificationBundle) {
		NotificationCompat.Builder builder = createBuilder(smallIcon, null, tickerText, contentTitle, contentText,
			clazz, notificationBundle, when);
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, Bitmap largeIcon, String tickerText,
			String contentTitle, String contentText, Intent notificationIntent) {
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIcon, tickerText, contentTitle, contentText,
			notificationIntent, System.currentTimeMillis());
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, String largeIconUrl, String tickerText,
			String contentTitle, String contentText, Intent notificationIntent) {
		Bitmap largeIconBitmap = createLargeIconBitmap(largeIconUrl);
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIconBitmap, tickerText, contentTitle,
			contentText, notificationIntent, System.currentTimeMillis());
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, String tickerText, String contentTitle,
			String contentText, Class<?> clazz) {
		sendNotification(id, smallIcon, tickerText, contentTitle, contentText, clazz, System.currentTimeMillis(), null);
	}
	
	public static void sendNotification(int id, int smallIcon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz) {
		sendNotification(id, smallIcon, tickerText, contentTitle, contentText, clazz, System.currentTimeMillis(), null);
	}
	
	public static void sendNotification(int id, int smallIcon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz, Long when) {
		sendNotification(id, smallIcon, tickerText, contentTitle, contentText, clazz, when, null);
	}
	
	public static void sendNotification(int id, int smallIcon, int tickerText, int contentTitle, int contentText,
			Class<?> clazz, Long when, Bundle notificationBundle) {
		sendNotification(id, smallIcon, (Bitmap)null, tickerText, contentTitle, contentText, clazz);
	}
	
	public static void sendNotification(int id, int smallIcon, String largeIconUrl, String tickerText,
			String contentTitle, String contentText, Class<?> clazz) {
		Bitmap largeIconBitmap = createLargeIconBitmap(largeIconUrl);
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIconBitmap, tickerText, contentTitle,
			contentText, clazz, null, System.currentTimeMillis());
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, Bitmap largeIcon, String tickerText,
			String contentTitle, String contentText, Class<?> clazz) {
		sendNotification(id, smallIcon, largeIcon, tickerText, contentTitle, contentText, clazz, null,
			System.currentTimeMillis());
	}
	
	public static void sendNotification(int id, int smallIcon, Bitmap largeIcon, int tickerText, int contentTitle,
			int contentText, Class<?> clazz) {
		sendNotification(id, smallIcon, largeIcon, tickerText, contentTitle, contentText, clazz, null,
			System.currentTimeMillis());
	}
	
	public static void sendNotification(int id, int smallIcon, String largeIconUrl, int tickerText, int contentTitle,
			int contentText, Class<?> clazz) {
		sendNotification(id, smallIcon, largeIconUrl, tickerText, contentTitle, contentText, clazz, null,
			System.currentTimeMillis());
	}
	
	public static void sendNotification(int id, int smallIcon, Bitmap largeIcon, String tickerText,
			String contentTitle, String contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIcon, tickerText, contentTitle, contentText,
			clazz, notificationBundle, when);
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, String largeIconUrl, String tickerText,
			String contentTitle, String contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		Bitmap largeIconBitmap = createLargeIconBitmap(largeIconUrl);
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIconBitmap, tickerText, contentTitle,
			contentText, clazz, notificationBundle, when);
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, Bitmap largeIcon, int tickerText, int contentTitle,
			int contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIcon, tickerText, contentTitle, contentText,
			clazz, notificationBundle, when);
		sendNotification(id, builder);
	}
	
	public static void sendNotification(int id, int smallIcon, String largeIconUrl, int tickerText, int contentTitle,
			int contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		Bitmap largeIconBitmap = createLargeIconBitmap(largeIconUrl);
		NotificationCompat.Builder builder = createBuilder(smallIcon, largeIconBitmap, tickerText, contentTitle,
			contentText, clazz, notificationBundle, when);
		sendNotification(id, builder);
	}
	
	public static void sendInProgressNotification(int id, int statusBarIcon, int notificationIcon, int progress,
			int tickerText, int contentTitle, int actionText, Class<?> clazz) {
		sendInProgressNotification(id, statusBarIcon, notificationIcon, progress, tickerText, contentTitle, actionText,
			clazz, null);
	}
	
	public static void sendInProgressNotification(int id, int statusBarIcon, int notificationIcon, int progress,
			int tickerText, int contentTitle, int actionText, Class<?> clazz, Bundle notificationBundle) {
		
		Context context = AbstractApplication.get();
		NotificationCompat.Builder builder = createBuilder(statusBarIcon, null, context.getString(tickerText), null,
			null, clazz, notificationBundle, null);
		builder.setOngoing(true);
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_inprogress);
		remoteViews.setTextViewText(R.id.title, context.getString(contentTitle));
		remoteViews.setTextViewText(R.id.action, context.getString(actionText));
		remoteViews.setTextViewText(R.id.progressPercentage, progress + "%");
		remoteViews.setProgressBar(R.id.progressBar, 100, progress, false);
		remoteViews.setImageViewResource(R.id.icon, notificationIcon);
		builder.setContent(remoteViews);
		
		sendNotification(id, builder);
	}
	
	private static NotificationCompat.Builder createBuilder(int smallIcon, Bitmap largeIcon, int tickerText,
			int contentTitle, int contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		Context context = AbstractApplication.get();
		return createBuilder(smallIcon, largeIcon, context.getString(tickerText), context.getString(contentTitle),
			context.getString(contentText), clazz, notificationBundle, when);
	}
	
	private static NotificationCompat.Builder createBuilder(int smallIcon, Bitmap largeIcon, String tickerText,
			String contentTitle, String contentText, Class<?> clazz, Bundle notificationBundle, Long when) {
		Intent notificationIntent = clazz != null ? new Intent(AbstractApplication.get(), clazz) : new Intent();
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (notificationBundle != null) {
			notificationIntent.replaceExtras(notificationBundle);
		}
		return createBuilder(smallIcon, largeIcon, tickerText, contentTitle, contentText, notificationIntent, when);
	}
	
	private static NotificationCompat.Builder createBuilder(int smallIcon, Bitmap largeIcon, String tickerText,
			String contentTitle, String contentText, Intent notificationIntent, Long when) {
		Context context = AbstractApplication.get();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setSmallIcon(smallIcon);
		builder.setTicker(tickerText);
		if (when != null) {
			builder.setWhen(when);
		}
		if (largeIcon != null) {
			builder.setLargeIcon(largeIcon);
		}
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		
		builder.setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, 0));
		
		builder.setAutoCancel(true);
		return builder;
	}
	
	private static Bitmap createLargeIconBitmap(String largeIconUrl) {
		Bitmap largeIconBitmap = null;
		// Avoid loading the image if the Android version doesn't supports large bitmap icons on the notifications
		if ((AndroidUtils.getApiLevel() >= 11) && StringUtils.isNotEmpty(largeIconUrl)) {
			largeIconBitmap = RemoteImageResolver.get().resolve(Uri.parse(largeIconUrl), LARGE_ICON_WIDHT,
				LARGE_ICON_HEIGHT);
		}
		return largeIconBitmap;
	}
	
	public static void sendNotification(int id, NotificationCompat.Builder builder) {
		NOTIFICATION_MANAGER.notify(id, builder.getNotification());
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
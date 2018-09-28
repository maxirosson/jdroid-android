package com.jdroid.android.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.images.BitmapUtils;
import com.jdroid.android.images.loader.BitmapLoader;
import com.jdroid.android.uri.ReferrerUtils;
import com.jdroid.android.uri.UriUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.utils.RandomUtils;
import com.jdroid.java.utils.StringUtils;

public class NotificationBuilder {

	public static final String NOTIFICATION_NAME = "notificationName";
	public static String NOTIFICATION_SCHEME = "notification";

	private String notificationName;

	private NotificationCompat.Builder builder;

	public NotificationBuilder(@NonNull String notificationName, @NonNull NotificationChannelType notificationChannelType) {
		this(notificationName, notificationChannelType.getChannelId());
	}

	public NotificationBuilder(@NonNull String notificationName, @Nullable String channelId) {

		if (NotificationUtils.findNotificationChannelType(channelId) == null) {
			AbstractApplication.get().getExceptionHandler().logHandledException("Channel id not found: " + channelId);
			NotificationChannelType defaultNotificationChannelType = AbstractApplication.get().getDefaultNotificationChannelType();
			if (defaultNotificationChannelType != null) {
				channelId = defaultNotificationChannelType.getChannelId();
			}
		}

		this.notificationName = notificationName;
		builder = new NotificationCompat.Builder(AbstractApplication.get(), channelId);
		builder.setAutoCancel(true);
		setColor(R.color.jdroid_colorPrimary);
	}

	public NotificationCompat.Builder getNotificationCompatBuilder() {
		return builder;
	}

	public Notification build() {
		return builder.build();
	}

	/**
	 * @param icon The drawable resource icon. Vector drawables are not supported
	 */
	public void setSmallIcon(@DrawableRes int icon) {
		builder.setSmallIcon(icon);
	}

	public void setTicker(int tickerId, Object... args) {
		setTicker(LocalizationUtils.getString(tickerId, args));
	}

	public void setTicker(String tickerText) {
		builder.setTicker(tickerText);
	}

	public void setContentTitle(@StringRes int contentTitleId, Object... args) {
		setContentTitle(LocalizationUtils.getString(contentTitleId, args));
	}

	public void setContentTitle(String contentTitle) {
		builder.setContentTitle(contentTitle);
	}

	public void setContentText(@StringRes int contentTextId, Object... args) {
		setContentText(LocalizationUtils.getString(contentTextId, args));
	}

	public void setContentText(String contentText) {
		builder.setContentText(contentText);
	}

	public void setContentIntentSingleTop(Class<?> clazz) {
		setContentIntentSingleTop(clazz, null);
	}

	public void setContentIntentSingleTop(Class<?> clazz, Bundle notificationBundle) {
		Intent notificationIntent = clazz != null ? new Intent(AbstractApplication.get(), clazz) : new Intent();
		if (notificationBundle != null) {
			notificationIntent.replaceExtras(notificationBundle);
		}
		setContentIntentSingleTop(notificationIntent);
	}

	public void setContentIntentSingleTop(Intent notificationIntent) {
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		setContentIntent(notificationIntent);
	}

	public void setContentIntentNewTask(Intent notificationIntent) {
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		setContentIntent(notificationIntent);
	}

	public void setContentIntent(Intent notificationIntent) {
		// TODO Disabled to avoid session creation on Google Analytics
		//	AbstractApplication.get().getCoreAnalyticsSender().trackNotificationDisplayed(notificationName);
		notificationIntent.putExtra(NOTIFICATION_NAME, notificationName);

		// This is a hack to avoid the notification caching
		if (notificationIntent.getData() == null) {
			notificationIntent.setData(createUniqueNotificationUri());
		}

		ReferrerUtils.setReferrer(notificationIntent, generateNotificationsReferrer());

		builder.setContentIntent(PendingIntent.getActivity(AbstractApplication.get(), RandomUtils.get16BitsInt(),
			notificationIntent, 0));
	}

	public void setNewTaskUrl(@NonNull String url) {
		setUrl(url, Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public void setSingleTopUrl(@NonNull String url) {
		setUrl(url, Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	public void setUrl(@NonNull String url) {
		setUrl(url, null);
	}

	public void setUrl(@NonNull String url, Integer flags) {
		if (StringUtils.isNotEmpty(url)) {
			Intent notificationIntent = UriUtils.createIntent(AbstractApplication.get(), url, generateNotificationsReferrer());
			if (flags != null) {
				notificationIntent.setFlags(flags);
			}
			setContentIntent(notificationIntent);
		}
	}

	public static String generateNotificationsReferrer() {
		return NOTIFICATION_SCHEME + "://" + AppUtils.getApplicationId();
	}

	protected Uri createUniqueNotificationUri() {
		return Uri.parse(NOTIFICATION_SCHEME + "://" + RandomUtils.getInt());
	}

	public void setWhen(Long when) {
		if (when != null) {
			builder.setWhen(when);
		}
	}

	public void setLargeIcon(@DrawableRes int resId) {
		builder.setLargeIcon(BitmapUtils.toBitmap(resId));
	}

	public void setLargeIcon(Bitmap largeIcon) {
		if (largeIcon != null) {
			builder.setLargeIcon(largeIcon);
		}
	}

	@WorkerThread
	public void setLargeIcon(BitmapLoader bitmapLoader) {
		if (bitmapLoader != null) {
			builder.setLargeIcon(createLargeIconBitmap(bitmapLoader));
		}
	}

	private Bitmap createLargeIconBitmap(BitmapLoader bitmapLoader) {
		return bitmapLoader.load(NotificationUtils.getNotificationLargeIconHeightPx(), NotificationUtils.getNotificationLargeIconWidthPx());
	}

	public void setInProgress(@DrawableRes int notificationIcon, int progress, int contentTitle, int actionText) {
		builder.setOngoing(true);

		Context context = AbstractApplication.get();

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.jdroid_notification_inprogress);
		remoteViews.setTextViewText(R.id.title, context.getString(contentTitle));
		remoteViews.setTextViewText(R.id.action, context.getString(actionText));
		remoteViews.setTextViewText(R.id.progressPercentage, progress + "%");
		remoteViews.setProgressBar(R.id.progressBar, 100, progress, false);
		remoteViews.setImageViewResource(R.id.icon, notificationIcon);
		builder.setContent(remoteViews);
	}

	public void setColor(@ColorRes int colorResId) {
		builder.setColor(AbstractApplication.get().getResources().getColor(colorResId));
	}

	public void setColor(String color) {
		builder.setColor(Color.parseColor(color));
	}

	public void setWhiteLight() {
		setLight("white");
	}

	public void setBlueLight() {
		setLight("blue");
	}

	private void setLight(String color) {
		builder.setLights(Color.parseColor(color), 1000, 5000);
	}

	public void setSound(int soundResId) {
		Uri notificationSound = Uri.parse("android.resource://" + AppUtils.getApplicationId() + "/" + soundResId);
		if (notificationSound != null) {
			builder.setSound(notificationSound);
		}
	}

	public void setDefaultSound() {
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		if (notificationSound != null) {
			builder.setSound(notificationSound);
		}
	}

	public void setMaxPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_MAX);
	}

	public void setHighPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);
	}

	public void setLowPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_LOW);
	}

	public void setMinPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_MIN);
	}

	public void setCategory(String category) {
		builder.setCategory(category);
	}

	public void setPublicVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
	}

	public void setPrivateVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
	}

	public void setSecretVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
	}

	public void addPerson(String uri) {
		builder.addPerson(uri);
	}

	public void setDefaultVibration() {
		long[] defaultPattern = { 1000, 500 };
		builder.setVibrate(defaultPattern);
	}

	public void setBigTextStyle(String title, String text) {
		if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(text)) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
			bigTextStyle.setBigContentTitle(title);
			bigTextStyle.bigText(text);
			builder.setStyle(bigTextStyle);
		}
	}

	public void addStartActivityAction(@DrawableRes int icon, @StringRes int titleId, Intent intent) {
		addStartActivityAction(icon, LocalizationUtils.getString(titleId), intent);
	}

	public void addStartActivityAction(@DrawableRes int icon, String title, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(AbstractApplication.get(),
			RandomUtils.get16BitsInt(), intent, 0);
		builder.addAction(icon, title, pendingIntent);
	}

	public void addStartServiceAction(@DrawableRes int icon, @StringRes int titleId, Intent intent) {
		addStartServiceAction(icon, LocalizationUtils.getString(titleId), intent);
	}

	public void addStartServiceAction(@DrawableRes int icon, String title, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getService(AbstractApplication.get(), RandomUtils.getInt(),
			intent, 0);
		builder.addAction(icon, title, pendingIntent);
	}

	public void setShowWhen(boolean show) {
		builder.setShowWhen(show);
	}

}

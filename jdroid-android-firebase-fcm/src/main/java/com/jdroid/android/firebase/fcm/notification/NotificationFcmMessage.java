package com.jdroid.android.firebase.fcm.notification;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.images.loader.BitmapLoader;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.TypeUtils;

public class NotificationFcmMessage implements FcmMessage {

	public static final String CHANNEL = "channel";
	public static final String CONTENT_TITLE = "contentTitle";
	public static final String CONTENT_TEXT = "contentText";
	public static final String SOUND_ENABLED = "soundEnabled";
	public static final String VIBRATION_ENABLED = "vibrationEnabled";
	public static final String LIGHT_ENABLED = "lightEnabled";
	public static final String URL = "url";
	public static final String LARGE_ICON_URL = "largeIconUrl";

	public static final String MESSAGE_KEY = "notificationFcmMessage";

	@Override
	public void handle(RemoteMessage remoteMessage) {
		NotificationBuilder builder = new NotificationBuilder(getMessageKey(), remoteMessage.getData().get(CHANNEL));

		initContentTitle(remoteMessage, builder);
		initContentText(remoteMessage, builder);

		initSmallIcon(remoteMessage, builder);
		initLargeIcon(remoteMessage, builder);

		initSound(remoteMessage, builder);
		initVibration(remoteMessage, builder);
		initLight(remoteMessage, builder);

		initContentIntent(remoteMessage, builder);

		builder.setPublicVisibility();
		builder.setWhen(DateUtils.nowMillis());
		configureBuilder(remoteMessage, builder);

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
	}

	protected void initContentTitle(RemoteMessage remoteMessage, NotificationBuilder builder) {
		String contentTitle = null;
		if (remoteMessage.getNotification() != null) {
			contentTitle = remoteMessage.getNotification().getTitle();
		}
		if (StringUtils.isEmpty(contentTitle)) {
			contentTitle = remoteMessage.getData().get(CONTENT_TITLE);
		}
		if (StringUtils.isNotEmpty(contentTitle)) {
			builder.setContentTitle(contentTitle);
		} else {
			throw new UnexpectedException("Missing " + CONTENT_TITLE + " extra for " + getMessageKey());
		}
	}

	protected void initContentText(RemoteMessage remoteMessage, NotificationBuilder builder) {
		String contentText = null;
		if (remoteMessage.getNotification() != null) {
			contentText = remoteMessage.getNotification().getBody();
		}
		if (StringUtils.isEmpty(contentText)) {
			contentText = remoteMessage.getData().get(CONTENT_TEXT);
		}
		if (StringUtils.isNotEmpty(contentText)) {
			builder.setContentText(contentText);
		} else {
			throw new UnexpectedException("Missing " + CONTENT_TEXT + " extra for " + getMessageKey());
		}
	}

	protected void initSound(RemoteMessage remoteMessage, NotificationBuilder builder) {
		if (TypeUtils.getBoolean(remoteMessage.getData().get(SOUND_ENABLED), false)) {
			builder.setDefaultSound();
		}
	}

	protected void initVibration(RemoteMessage remoteMessage, NotificationBuilder builder) {
		if (TypeUtils.getBoolean(remoteMessage.getData().get(VIBRATION_ENABLED), false)) {
			builder.setDefaultVibration();
		}
	}

	protected void initLight(RemoteMessage remoteMessage, NotificationBuilder builder) {
		if (TypeUtils.getBoolean(remoteMessage.getData().get(LIGHT_ENABLED), false)) {
			builder.setWhiteLight();
		}
	}

	protected void initContentIntent(RemoteMessage remoteMessage, NotificationBuilder builder) {
		String url = null;
		if (remoteMessage.getNotification() != null) {
			url = remoteMessage.getNotification().getClickAction();
		}
		if (StringUtils.isEmpty(url)) {
			url = remoteMessage.getData().get(URL);
		}
		if (StringUtils.isNotEmpty(url)) {
			builder.setSingleTopUrl(url);
		} else {
			throw new UnexpectedException("Missing " + URL + " extra for " + getMessageKey());
		}
	}

	protected void initSmallIcon(RemoteMessage remoteMessage, NotificationBuilder builder) {
		builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
	}

	protected void initLargeIcon(RemoteMessage remoteMessage, NotificationBuilder builder) {
		String largeIconUrl = remoteMessage.getData().get(LARGE_ICON_URL);
		BitmapLoader bitmapLoader = createBitmapLoader(largeIconUrl);
		if (largeIconUrl != null) {
			if (bitmapLoader != null) {
				builder.setLargeIcon(bitmapLoader);
			} else {
				AbstractApplication.get().getExceptionHandler().logWarningException("Not bitmapLoader defined to load large icon url");
			}
		}
	}

	protected BitmapLoader createBitmapLoader(String url) {
		return null;
	}

	protected void configureBuilder(RemoteMessage remoteMessage, NotificationBuilder notificationBuilder) {
		// Do Nothing
	}

	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}

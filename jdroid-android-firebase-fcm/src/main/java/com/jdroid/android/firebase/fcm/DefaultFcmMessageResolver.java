package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.firebase.fcm.notification.NotificationFcmMessage;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.TypeUtils;

import org.slf4j.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DefaultFcmMessageResolver implements FcmMessageResolver {

	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultFcmMessageResolver.class);

	public static final String USER_ID_KEY = "userIdKey";
	public static final String MIN_DEVICE_OS_VERSION_KEY = "minDeviceOsVersion";
	public static final String MIN_APP_VERSION_CODE_KEY = "minAppVersionCode";

	public DefaultFcmMessageResolver() {

		NotificationFcmMessage notificationFcmMessage = createNotificationFcmMessage();
		if (notificationFcmMessage != null) {
			FcmContext.addFcmMessage(notificationFcmMessage);
		}
	}

	@Nullable
	protected NotificationFcmMessage createNotificationFcmMessage() {
		return new NotificationFcmMessage();
	}

	@Override
	public FcmMessage resolve(@NonNull RemoteMessage remoteMessage) {
		LOGGER.debug("FCM message received. " + remoteMessage.getData());
		Long minAppVersionCode = TypeUtils.getLong(remoteMessage.getData().get(MIN_APP_VERSION_CODE_KEY), 0L);
		if (AppUtils.getVersionCode() >= minAppVersionCode) {
			Long minDeviceOsVersion = TypeUtils.getLong(remoteMessage.getData().get(MIN_DEVICE_OS_VERSION_KEY), 0L);
			if (AndroidUtils.getApiLevel() >= minDeviceOsVersion) {
				for (FcmMessage each : FcmContext.getFcmMessages()) {
					if (each.matches(remoteMessage)) {

						Long userId = TypeUtils.getLong(remoteMessage.getData().get(USER_ID_KEY));

						// We should ignore messages received for previously logged users
						if ((userId != null) && (!SecurityContext.get().isAuthenticated() || !SecurityContext.get().getUser().getId().equals(userId))) {
							LOGGER.warn("The FCM message is ignored because it was sent to another user: " + userId);
							onNotAuthenticatedUser(userId);
							return null;
						}
						return each;
					}
				}
				AbstractApplication.get().getExceptionHandler().logWarningException("Ignoring FCM message. Not resolved");
			} else {
				LOGGER.debug("Ignoring FCM message. minDeviceOsVersion: " + minDeviceOsVersion);
			}
		} else {
			LOGGER.debug("Ignoring FCM message. minAppVersionCode: " + minAppVersionCode);
		}
		return null;
	}

	protected void onNotAuthenticatedUser(Long userId) {
		// Do nothing
	}
}

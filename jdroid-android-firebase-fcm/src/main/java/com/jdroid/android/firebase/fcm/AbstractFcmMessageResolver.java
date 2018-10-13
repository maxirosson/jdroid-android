package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.firebase.fcm.notification.NotificationFcmMessage;
import com.jdroid.android.firebase.fcm.remoteconfig.RemoteConfigFetchFcmMessage;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.TypeUtils;

import org.slf4j.Logger;

import java.util.List;

import androidx.annotation.Nullable;

public abstract class AbstractFcmMessageResolver implements FcmMessageResolver {

	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractFcmMessageResolver.class);

	public static final String MESSAGE_KEY_EXTRA = "messageKey";
	public static final String USER_ID_KEY = "userIdKey";
	public static final String MIN_DEVICE_OS_VERSION_KEY = "minDeviceOsVersion";
	public static final String MIN_APP_VERSION_CODE_KEY = "minAppVersionCode";

	private List<FcmMessage> fcmMessages;

	public AbstractFcmMessageResolver() {
		this(Lists.newArrayList());
	}

	public AbstractFcmMessageResolver(List<FcmMessage> fcmMessages) {
		this.fcmMessages = fcmMessages;

		NotificationFcmMessage notificationFcmMessage = createNotificationFcmMessage();
		if (notificationFcmMessage != null) {
			this.fcmMessages.add(notificationFcmMessage);
		}

		RemoteConfigFetchFcmMessage remoteConfigFetchFcmMessage = createRemoteConfigFetchFcmMessage();
		if (remoteConfigFetchFcmMessage != null) {
			this.fcmMessages.add(remoteConfigFetchFcmMessage);
		}
	}

	public AbstractFcmMessageResolver(FcmMessage... fcmMessages) {
		this(Lists.newArrayList(fcmMessages));
	}

	@Nullable
	protected NotificationFcmMessage createNotificationFcmMessage() {
		return new NotificationFcmMessage();
	}

	@Nullable
	protected RemoteConfigFetchFcmMessage createRemoteConfigFetchFcmMessage() {
		return new RemoteConfigFetchFcmMessage();
	}

	@Override
	public FcmMessage resolve(RemoteMessage remoteMessage) {
		String messageKey = remoteMessage.getData().get(getMessageKeyExtraName());
		LOGGER.debug("FCM message received. / Message Key: " + messageKey);
		Long minAppVersionCode = TypeUtils.getLong(remoteMessage.getData().get(MIN_APP_VERSION_CODE_KEY), 0L);
		if (AppUtils.getVersionCode() >= minAppVersionCode) {
			Long minDeviceOsVersion = TypeUtils.getLong(remoteMessage.getData().get(MIN_DEVICE_OS_VERSION_KEY), 0L);
			if (AndroidUtils.getApiLevel() >= minDeviceOsVersion) {
				for (FcmMessage each : fcmMessages) {
					if (each.getMessageKey().equalsIgnoreCase(messageKey)) {

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
				AbstractApplication.get().getExceptionHandler().logWarningException("The FCM message key [" + messageKey + "] is unknown");
			} else {
				LOGGER.debug("Ignoring FCM message [" + messageKey + "]. minDeviceOsVersion: " + minDeviceOsVersion);
			}
		} else {
			LOGGER.debug("Ignoring FCM message [" + messageKey + "]. minAppVersionCode: " + minAppVersionCode);
		}
		return null;
	}

	protected String getMessageKeyExtraName() {
		return MESSAGE_KEY_EXTRA;
	}

	protected abstract void onNotAuthenticatedUser(Long userId);
}

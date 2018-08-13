package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FcmListenerResolver {

	private static final Logger LOGGER = LoggerUtils.getLogger(FcmListenerResolver.class);

	/**
	 * Called when message is received. Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution
	 */
	public void onMessageReceived(RemoteMessage remoteMessage) {

		if (LoggerUtils.isEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("Message received. ");
			builder.append("from: ");
			builder.append(remoteMessage.getFrom());

			if (remoteMessage.getMessageType() != null ) {
				builder.append(", ");
				builder.append("message type: ");
				builder.append(remoteMessage.getMessageType());
			}

			if (remoteMessage.getData() != null & !remoteMessage.getData().isEmpty()) {
				builder.append(", ");
				builder.append("data: ");
				builder.append(remoteMessage.getData());
			}
			LOGGER.info(builder.toString());
		}

		FcmMessageResolver fcmResolver = AbstractFcmAppModule.get().getFcmMessageResolver(remoteMessage.getFrom());
		if (fcmResolver != null) {
			FcmMessage fcmMessage = null;
			try {
				fcmMessage = fcmResolver.resolve(remoteMessage);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Error when resolving FCM message", e);
				return;
			}

			if (fcmMessage != null) {
				try {
					fcmMessage.handle(remoteMessage);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Error when handling FCM message", e);
				}
			} else {
				LOGGER.warn("The FCM message was not resolved");
			}
		} else {
			LOGGER.warn("A FCM message was received, but not resolved is configured");
		}
	}

	public void onMessageSent(String msgId) {
		LOGGER.info("Message sent with id: " + msgId);
	}

	public void onSendError(String msgId, Exception exception) {
		AbstractApplication.get().getExceptionHandler().logWarningException("Send error. Message id: " + msgId, exception);
	}

	public void onDeletedMessages() {
		LOGGER.info("Deleted messages");
	}
	
	public void onNewToken(String token) {
		LOGGER.info("New token: " + token);
		AbstractFcmAppModule.get().startFcmRegistration(false);
	}
	
}

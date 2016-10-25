package com.jdroid.android.firebase.fcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.service.ServiceCommand;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;

public class FcmRegistrationCommand extends ServiceCommand {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FcmRegistrationCommand.class);

	private final static String UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp";

	public void start(Boolean updateLastActiveTimestamp) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp);
		start(bundle);
	}

	@Override
	protected int execute(Bundle bundle) {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {
			for (FcmSender fcmSender : AbstractFcmAppModule.get().getFcmSenders()) {
				String registrationToken;
				try {
					registrationToken = getRegistrationToken(fcmSender.getSenderId());
				} catch (IOException e) {
					LOGGER.warn("Error when getting registration token", e);
					return GcmNetworkManager.RESULT_RESCHEDULE;
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Error when getting FCM registration token. Will retry later.", e);
					return GcmNetworkManager.RESULT_RESCHEDULE;
				}

				try {
					LOGGER.info("Registering FCM token on server");
					Boolean updateLastActiveTimestamp = bundle.getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false);
					fcmSender.onRegisterOnServer(registrationToken, updateLastActiveTimestamp, bundle);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on server. Will retry later.", e);
					return GcmNetworkManager.RESULT_RESCHEDULE;
				}
			}
			return GcmNetworkManager.RESULT_SUCCESS;
		} else {
			LOGGER.warn("FCM not initialized because Google Play Services is not available");
			return GcmNetworkManager.RESULT_RESCHEDULE;
		}
	}

	public static String getRegistrationToken(String senderId) throws IOException {
		if (senderId == null) {
			throw new UnexpectedException("Missing FCM Sender Id");
		}
		String registrationToken = FirebaseInstanceId.getInstance().getToken(senderId, "FCM");
		LOGGER.info("Registration token for sender id [" + senderId + "]: " + registrationToken);
		return registrationToken;
	}
}

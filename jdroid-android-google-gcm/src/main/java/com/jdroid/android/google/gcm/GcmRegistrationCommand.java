package com.jdroid.android.google.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.gcm.TaskParams;
import com.google.android.gms.iid.InstanceID;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.service.ServiceCommand;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class GcmRegistrationCommand extends ServiceCommand {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmRegistrationCommand.class);

	private static final String UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp";

	public void start(Boolean updateLastActiveTimestamp) {
		Intent intent = new Intent();
		intent.putExtra(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp);
		start(intent);
	}

	@Override
	protected int execute(Intent intent) {
		return doRunTask(intent.getExtras());
	}

	@Override
	protected int executeRetry(TaskParams taskParams) {
		return doRunTask(taskParams.getExtras());
	}

	private int doRunTask(Bundle bundle) {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {

			String registrationToken;
			try {
				registrationToken = getRegistrationToken(AbstractApplication.get());
			} catch (IOException e) {
				LOGGER.warn("Error when getting registration token", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on gcm. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}

			try {
				LOGGER.info("Registering GCM token on server");
				Boolean updateLastActiveTimestamp = bundle.getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false);
				AbstractGcmAppModule.get().onRegisterOnServer(registrationToken, updateLastActiveTimestamp, bundle);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on server. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}

			try {
				subscribeTopics(registrationToken);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to subscribe to topic channels. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}
			return GcmNetworkManager.RESULT_SUCCESS;
		} else {
			LOGGER.warn("GCM not initialized because Google Play Services is not available");
			return GcmNetworkManager.RESULT_RESCHEDULE;
		}
	}

	/**
	 * Subscribe to any GCM topics of interest, as defined by the getSubscriptionTopics method.
	 *
	 * @param token GCM token
	 * @throws IOException if unable to reach the GCM PubSub service
	 */
	private void subscribeTopics(String token) throws IOException {
		List<String> subscriptionTopics = AbstractGcmAppModule.get().getSubscriptionTopics();
		if (!Lists.isNullOrEmpty(subscriptionTopics)) {
			GcmPubSub pubSub = GcmPubSub.getInstance(AbstractApplication.get());
			for (String topic : subscriptionTopics) {
				pubSub.subscribe(token, "/topics/" + topic, null);
			}
		}
	}

	public static String getRegistrationToken(Context context) throws IOException {
		String senderId = AbstractGcmAppModule.get().getGcmSender().getSenderId();
		if (senderId == null) {
			throw new UnexpectedException("Missing GCM Sender Id");
		}

		InstanceID instanceID = InstanceID.getInstance(context);
		return instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
	}
}

package com.jdroid.android.firebase.fcm;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

// TODO By Google recommendation, we should execute this command every 2 weeks, to have always fresh tokens on server side
public class FcmRegistrationCommand extends ServiceCommand {

	private final static Logger LOGGER = LoggerUtils.getLogger(FcmRegistrationCommand.class);

	private final static String UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp";

	public void start(Boolean updateLastActiveTimestamp) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp);
		start(bundle);
	}

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		if (GooglePlayServicesUtils.INSTANCE.isGooglePlayServicesAvailable(context)) {
			for (FcmSender fcmSender : AbstractFcmAppModule.get().getFcmSenders()) {
				String registrationToken;
				try {
					registrationToken = getRegistrationToken(fcmSender.getSenderId());
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Error when getting FCM registration token. Will retry later.", e);
					return true;
				}

				if (registrationToken == null) {
					LOGGER.info("Null registration token. Will retry later.");
					return true;
				}

				try {
					LOGGER.info("Registering FCM token on server");
					boolean updateLastActiveTimestamp = bundle.getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false);
					fcmSender.onRegisterOnServer(registrationToken, updateLastActiveTimestamp, bundle);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on server. Will retry later.", e);
					return true;
				}
			}
			return false;
		} else {
			LOGGER.warn("FCM not initialized because Google Play Services is not available");
			return true;
		}
	}

	@WorkerThread
	@Nullable
	public static String getRegistrationToken(String senderId) {
		if (GooglePlayServicesUtils.INSTANCE.isGooglePlayServicesAvailable(AbstractApplication.get())) {
			String registrationToken;
			try {
				if (senderId != null) {
					registrationToken = FirebaseInstanceId.getInstance().getToken(senderId, "FCM");
					LOGGER.info("Registration token for sender id [" + senderId + "]: " + registrationToken);
				} else {
					Task<InstanceIdResult> task = FirebaseInstanceId.getInstance().getInstanceId();
					// Block on the task and get the result synchronously
					InstanceIdResult instanceIdResult = Tasks.await(task);
					registrationToken = instanceIdResult.getToken();
					LOGGER.info("Registration token for default sender id: " + registrationToken);
				}
			} catch (IOException | ExecutionException | InterruptedException e) {
				throw new UnexpectedException(e);
			}
			return registrationToken;
		}
		return null;
	}
}

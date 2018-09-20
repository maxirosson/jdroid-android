package com.jdroid.android.firebase.fcm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

public class FcmRegistrationWorker extends AbstractWorker {
	
	public static final String WORK_MANAGER_TAG = "fcm";
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FcmRegistrationWorker.class);

	private final static String UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp";

	public FcmRegistrationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	public static void start(Boolean updateLastActiveTimestamp) {
		OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(FcmRegistrationWorker.class);
		
		Data.Builder dataBuilder = new Data.Builder();
		dataBuilder.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp);
		requestBuilder.setInputData(dataBuilder.build());
		
		Constraints.Builder constrainsBuilder = new Constraints.Builder();
		constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
		requestBuilder.setConstraints(constrainsBuilder.build());
		
		requestBuilder.addTag(WORK_MANAGER_TAG);
		
		WorkManager.getInstance().beginUniqueWork(FcmRegistrationWorker.class.getSimpleName(), ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue();
	}
	
	// By Google recommendation, we should execute this worker every 2 weeks, to have always fresh tokens on server side
	private void startPeriodic(Boolean updateLastActiveTimestamp) {
		PeriodicWorkRequest.Builder requestBuilder = new PeriodicWorkRequest.Builder(FcmRegistrationWorker.class, 14, TimeUnit.DAYS, 7, TimeUnit.DAYS);
		
		Data.Builder dataBuilder = new Data.Builder();
		dataBuilder.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp);
		requestBuilder.setInputData(dataBuilder.build());
		
		Constraints.Builder constrainsBuilder = new Constraints.Builder();
		constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
		requestBuilder.setConstraints(constrainsBuilder.build());
		
		requestBuilder.addTag(WORK_MANAGER_TAG);
		
		WorkManager.getInstance().enqueueUniquePeriodicWork(FcmRegistrationWorker.class.getSimpleName(), ExistingPeriodicWorkPolicy.KEEP, requestBuilder.build());
	}
	
	@NonNull
	@Override
	protected Result onWork() {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getApplicationContext())) {
			Boolean updateLastActiveTimestamp = getInputData().getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false);
			for (FcmSender fcmSender : AbstractFcmAppModule.get().getFcmSenders()) {
				String registrationToken;
				try {
					registrationToken = getRegistrationToken(fcmSender.getSenderId());
				} catch (IOException e) {
					LOGGER.warn("Error when getting registration token", e);
					return Result.RETRY;
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Error when getting FCM registration token. Will retry later.", e);
					return Result.RETRY;
				}
				
				if (registrationToken == null) {
					LOGGER.info("Null registration token. Will retry later.");
					return Result.RETRY;
				}

				try {
					LOGGER.info("Registering FCM token on server");
					fcmSender.onRegisterOnServer(registrationToken, updateLastActiveTimestamp, getInputData().getKeyValueMap());
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on server. Will retry later.", e);
					return Result.RETRY;
				}
			}
			startPeriodic(updateLastActiveTimestamp);
			return Result.SUCCESS;
		} else {
			LOGGER.warn("FCM not initialized because Google Play Services is not available");
			return Result.RETRY;
		}
	}
	

	
	public static String getRegistrationToken(String senderId) throws IOException {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {
			String registrationToken;
			if (senderId != null) {
				registrationToken = FirebaseInstanceId.getInstance().getToken(senderId, "FCM");
				LOGGER.info("Registration token for sender id [" + senderId + "]: " + registrationToken);
			} else {
				registrationToken = FirebaseInstanceId.getInstance().getToken();
				LOGGER.info("Registration token for default sender id: " + registrationToken);
			}
			return registrationToken;
		}
		return null;
	}
}

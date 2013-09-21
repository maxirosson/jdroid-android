package com.jdroid.android.gcm;

import java.io.IOException;
import org.slf4j.Logger;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.android.utils.IntentRetryUtils;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractGcmRegistrationService extends WorkerService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractGcmRegistrationService.class);
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		String registrationId = null;
		try {
			GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
			
			String googleProjectId = AbstractApplication.get().getAndroidApplicationContext().getGoogleProjectId();
			registrationId = googleCloudMessaging.register(googleProjectId);
			GcmPreferences.setRegistrationId(getApplicationContext(), registrationId);
		} catch (IOException e) {
			LOGGER.warn("Failed to register the device on gcm. Will retry later.", e);
			IntentRetryUtils.retry(intent);
			return;
		}
		
		try {
			onRegisterOnServer(registrationId);
			GcmPreferences.setRegisteredOnServer(getApplicationContext(), true);
		} catch (ApplicationException e) {
			LOGGER.warn("Failed to register the device on server. Will retry later.", e);
			IntentRetryUtils.retry(intent);
		}
	}
	
	protected abstract void onRegisterOnServer(String registrationId);
	
	public static void runRegistrationService(Context context,
			Class<? extends AbstractGcmRegistrationService> serviceClass) {
		if (GooglePlayUtils.isGooglePlayServicesAvailable(context)
				&& (!GcmPreferences.isRegistered(context) || !GcmPreferences.isRegisteredOnServer(context))) {
			WorkerService.runIntentInService(context, new Intent(), serviceClass);
		}
	}
}

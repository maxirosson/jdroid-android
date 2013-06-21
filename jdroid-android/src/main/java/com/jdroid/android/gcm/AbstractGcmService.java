package com.jdroid.android.gcm;

import org.slf4j.Logger;
import android.app.IntentService;
import android.content.Context;
import com.google.android.gcm.GCMBaseIntentService;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Base {@link IntentService} to handle Google Cloud Messaging (GCM) messages.
 * 
 * You need to add the following permissions to your manifest:
 * 
 * <pre>
 * &lt;permission android:name="[app package].permission.C2D_MESSAGE" android:protectionLevel="signature" />
 * &lt;uses-permission android:name="[app package].permission.C2D_MESSAGE" />
 * &lt;uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 * &lt;uses-permission android:name="android.permission.WAKE_LOCK" />
 * &lt;uses-permission android:name="android.permission.GET_ACCOUNTS" />
 * </pre>
 */
public abstract class AbstractGcmService extends GCMBaseIntentService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractGcmService.class);
	
	// Error codes
	private static final String ERR_ACCOUNT_MISSING = "ACCOUNT_MISSING";
	private static final String ERR_AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
	private static final String ERR_INVALID_SENDER = "INVALID_SENDER";
	private static final String ERR_PHONE_REGISTRATION_ERROR = "PHONE_REGISTRATION_ERROR";
	private static final String ERR_INVALID_PARAMETERS = "INVALID_PARAMETERS";
	
	public AbstractGcmService() {
		super(AbstractApplication.get().getAndroidApplicationContext().getGoogleProjectId());
	}
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onRegistered(android.content.Context, java.lang.String)
	 */
	@Override
	public void onRegistered(Context context, String registrationId) {
		// This method shouldn't be called
	}
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onUnregistered(android.content.Context, java.lang.String)
	 */
	@Override
	public void onUnregistered(Context context, String registrationId) {
		// This method shouldn't be called
	}
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onError(android.content.Context, java.lang.String)
	 */
	@Override
	protected void onError(Context context, String errorId) {
		if (ERR_ACCOUNT_MISSING.equals(errorId)) {
			LOGGER.warn("There is no Google account on the device.");
			onMissingGoogleAccountError(context);
		} else if (ERR_AUTHENTICATION_FAILED.equals(errorId)) {
			LOGGER.warn("Authentication failed.");
			onAuthenticationFailedError(context);
		} else if (ERR_INVALID_PARAMETERS.equals(errorId)) {
			LOGGER.warn("The request sent by this device does not contain the expected parameters.");
			onInvalidParametersError(context);
		} else if (ERR_INVALID_SENDER.equals(errorId)) {
			LOGGER.warn("The sender account is not recognized.");
			onInvalidSenderError(context);
		} else if (ERR_PHONE_REGISTRATION_ERROR.equals(errorId)) {
			LOGGER.warn("This phone doesn't currently support GCM.");
			onGcmNotSupportedError(context);
		}
	}
	
	/**
	 * Called on registration error. There is no Google account on the phone. The application should ask the user to
	 * open the account manager and add a Google account. Fix on the device side.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onMissingGoogleAccountError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. Bad password. The application should ask the user to enter his/her password, and
	 * let user retry manually later. Fix on the device side.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onAuthenticationFailedError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * The request sent by the phone does not contain the expected parameters.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onInvalidParametersError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. The sender account is not recognized.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onInvalidSenderError(Context context) {
		// Do nothing by default
	}
	
	/**
	 * Called on registration error. Incorrect phone registration with Google. This phone doesn't currently support GCM.
	 * 
	 * This is called in the context of a Service - no dialog or UI.
	 * 
	 * @param context The {@link Context}
	 */
	public void onGcmNotSupportedError(Context context) {
		// Do nothing by default
	}
}

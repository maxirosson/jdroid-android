package com.jdroid.android.gcm;

import java.sql.Timestamp;
import org.slf4j.Logger;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Utilities for device registration.
 * <p>
 * <strong>Note:</strong> this class uses a private {@link SharedPreferences} object to keep track of the registration
 * token.
 */
public final class GcmPreferences {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmPreferences.class);
	
	// Default lifespan (7 days) of the {@link #isRegisteredOnServer(Context)} flag until it is considered expired.
	private static final long DEFAULT_ON_SERVER_LIFESPAN_MS = 1000 * 3600 * 24 * 7;
	
	private static final String PREFERENCES = "com.google.android.gcm";
	private static final String PROPERTY_REG_ID = "regId";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_ON_SERVER = "onServer";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTime";
	private static final String PROPERTY_ON_SERVER_LIFESPAN = "onServerLifeSpan";
	
	/**
	 * Gets the current registration id for application on GCM service.
	 * 
	 * @param context application's context.
	 * @return registration id, or null if the registration is not complete.
	 */
	public static String getRegistrationId(Context context) {
		SharedPreferences prefs = getGCMPreferences(context);
		
		// check if app was updated; if so, it must clear registration id to
		// avoid a race condition if GCM sends a message
		int oldVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int newVersion = AndroidUtils.getVersionCode();
		if ((oldVersion != Integer.MIN_VALUE) && (oldVersion != newVersion)) {
			LOGGER.trace("App version changed from " + oldVersion + " to " + newVersion + ". Clearing registration id");
			clearRegistrationId(context);
		}
		return prefs.getString(PROPERTY_REG_ID, null);
	}
	
	/**
	 * Checks whether the application was successfully registered on GCM service.
	 * 
	 * @param context application's context.
	 * @return Whether the application was successfully registered on GCM service.
	 */
	public static boolean isRegistered(Context context) {
		return getRegistrationId(context) != null;
	}
	
	/**
	 * Clears the registration id in the persistence store.
	 * 
	 * @param context application's context.
	 */
	public static void clearRegistrationId(Context context) {
		setRegistrationId(context, null);
	}
	
	/**
	 * Sets the registration id in the persistence store.
	 * 
	 * @param context application's context.
	 * @param registrationId The registrationId
	 */
	public static void setRegistrationId(Context context, String registrationId) {
		int appVersion = AndroidUtils.getVersionCode();
		
		SharedPreferences prefs = getGCMPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, registrationId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
		
		LOGGER.trace("Saved the registrationId [" + registrationId + "] on app version " + appVersion);
	}
	
	/**
	 * Sets whether the device was successfully registered in the server side.
	 * 
	 * @param context
	 * @param flag
	 */
	public static void setRegisteredOnServer(Context context, boolean flag) {
		SharedPreferences prefs = getGCMPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean(PROPERTY_ON_SERVER, flag);
		long expirationTime = System.currentTimeMillis() + getRegisterOnServerLifespan(context);
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, System.currentTimeMillis()
				+ getRegisterOnServerLifespan(context));
		editor.commit();
		LOGGER.trace("Setted registeredOnServer status as " + flag + " until " + new Timestamp(expirationTime));
	}
	
	/**
	 * Checks whether the device was successfully registered in the server side, as set by
	 * {@link #setRegisteredOnServer(Context, boolean)}.
	 * 
	 * <p>
	 * To avoid the scenario where the device sends the registration to the server but the server loses it, this flag
	 * has an expiration date, which is {@link #DEFAULT_ON_SERVER_LIFESPAN_MS} by default (but can be changed by
	 * {@link #setRegisterOnServerLifespan(Context, long)}).
	 * 
	 * @param context
	 * @return whether the device was successfully registered in the server side
	 */
	public static boolean isRegisteredOnServer(Context context) {
		SharedPreferences prefs = getGCMPreferences(context);
		boolean isRegistered = prefs.getBoolean(PROPERTY_ON_SERVER, false);
		LOGGER.trace("Is registered on server: " + isRegistered);
		if (isRegistered) {
			// checks if the information is not stale
			long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
			if (System.currentTimeMillis() > expirationTime) {
				LOGGER.trace("registeredOnServer flag expired on: " + new Timestamp(expirationTime));
				return false;
			}
		}
		return isRegistered;
	}
	
	/**
	 * Gets how long (in milliseconds) the {@link #isRegistered(Context)} property is valid.
	 * 
	 * @param context application's context.
	 * @return value set by {@link #setRegisteredOnServer(Context, boolean)} or {@link #DEFAULT_ON_SERVER_LIFESPAN_MS}
	 *         if not set.
	 */
	public static long getRegisterOnServerLifespan(Context context) {
		return getGCMPreferences(context).getLong(PROPERTY_ON_SERVER_LIFESPAN, DEFAULT_ON_SERVER_LIFESPAN_MS);
	}
	
	/**
	 * Sets how long (in milliseconds) the {@link #isRegistered(Context)} flag is valid.
	 * 
	 * @param context application's context.
	 * @param lifespan
	 */
	public static void setRegisterOnServerLifespan(Context context, long lifespan) {
		SharedPreferences prefs = getGCMPreferences(context);
		Editor editor = prefs.edit();
		editor.putLong(PROPERTY_ON_SERVER_LIFESPAN, lifespan);
		editor.commit();
	}
	
	private static SharedPreferences getGCMPreferences(Context context) {
		return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}
}

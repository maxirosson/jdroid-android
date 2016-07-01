package com.jdroid.android.google;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GooglePlayServicesUtils {

	private GooglePlayServicesUtils(){}

	private static final Logger LOGGER = LoggerUtils.getLogger(GooglePlayServicesUtils.class);

	private static final String GOOGLE_PLAY_SERVICES = "com.google.android.gms";

	private static final int GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST = 1;

	private static String advertisingId;

	public static GooglePlayServicesResponse verifyGooglePlayServices(Activity activity) {
		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(AbstractApplication.get());
		LOGGER.info("Google Play Services result code: " +  resultCode);
		if (resultCode == ConnectionResult.SUCCESS) {
			return new GooglePlayServicesResponse();
		} else {
			Dialog dialog = null;
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				dialog = googleApiAvailability.getErrorDialog(activity, resultCode, GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST);
				if (dialog != null) {
					// This dialog will help the user update to the latest GooglePlayServices
					dialog.show();
				}
			}
			return new GooglePlayServicesResponse(dialog, resultCode);
		}
	}

	public static void checkGooglePlayServices(Activity activity) {
		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
		LOGGER.info("Google Play Services result code: " + resultCode);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				googleApiAvailability.showErrorDialogFragment(activity, resultCode, GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST);
			}
		}
	}

	public static boolean isGooglePlayServicesAvailable(Context context) {
		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
		LOGGER.info("Google Play Services result code: " + resultCode);
		return resultCode == ConnectionResult.SUCCESS;
	}

	public static void launchGooglePlayServicesUpdate(Activity c) {
		GooglePlayUtils.launchAppDetails(c, GOOGLE_PLAY_SERVICES);
	}

	public static String getAdvertisingId(Context context) {
		if (advertisingId == null) {
			try {
				advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
			} catch (Exception e) {
				LOGGER.warn("Error getting the advertising id " + e.getMessage());
			}
		}
		return advertisingId;
	}

	public static class GooglePlayServicesResponse {

		private Boolean isAvailable;
		private Dialog dialog;
		private Integer resultCode;

		public GooglePlayServicesResponse() {
			isAvailable = true;
		}

		public GooglePlayServicesResponse(Dialog dialog, Integer resultCode) {
			isAvailable = false;
			this.dialog = dialog;
			this.resultCode = resultCode;
		}

		public Boolean isAvailable() {
			return isAvailable;
		}

		public Dialog getDialog() {
			return dialog;
		}

		public Integer getResultCode() {
			return resultCode;
		}
	}
}

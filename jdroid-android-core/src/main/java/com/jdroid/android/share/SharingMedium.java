package com.jdroid.android.share;

import android.provider.Settings;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ExternalAppsUtils;

public enum SharingMedium {

	FACEBOOK("facebook", ExternalAppsUtils.FACEBOOK_PACKAGE_NAME),
	TWITTER("twitter", ExternalAppsUtils.TWITTER_PACKAGE_NAME),
	WHATSAPP("whatsapp", ExternalAppsUtils.WHATSAPP_PACKAGE_NAME),
	TELEGRAM("telegram", ExternalAppsUtils.TELEGRAM_PACKAGE_NAME),
	HANGOUTS("hangouts", ExternalAppsUtils.HANGOUTS_PACKAGE_NAME),
	SMS("sms", null) {
		@Override
		public String getApplicationId() {
			return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), "sms_default_application");
		}
	};

	private String mediumName;
	private String applicationId;

	SharingMedium(String mediumName, String applicationId) {
		this.mediumName = mediumName;
		this.applicationId = applicationId;
	}

	public String getMediumName() {
		return mediumName;
	}

	public String getApplicationId() {
		return applicationId;
	}

}

package com.jdroid.android.share;

import android.provider.Settings;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ExternalAppsUtils;

public enum SharingMedium {
	
	GOOGLE_PLUS("googlePlus", ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME),
	FACEBOOK("facebook", ExternalAppsUtils.FACEBOOK_PACKAGE_NAME),
	TWITTER("twitter", ExternalAppsUtils.TWITTER_PACKAGE_NAME),
	WHATSAPP("whatsapp", ExternalAppsUtils.WHATSAPP_PACKAGE_NAME),
	TELEGRAM("telegram", ExternalAppsUtils.TELEGRAM_PACKAGE_NAME),
	HANGOUTS("hangouts", ExternalAppsUtils.HANGOUTS_PACKAGE_NAME),
	SMS("sms", null) {
		@Override
		public String getPackageName() {
			return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), "sms_default_application");
		}
	};
	
	private String name;
	private String packageName;
	
	SharingMedium(String name, String packageName) {
		this.name = name;
		this.packageName = packageName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
}

package com.jdroid.android.firebase.admob;

import com.jdroid.java.remoteconfig.RemoteConfigParameter;

public enum AdMobRemoteConfigParameter implements RemoteConfigParameter {

	ADS_ENABLED(false),
	MIN_APP_LOADS_TO_DISPLAY_ADS(5L),
	MIN_DAYS_TO_DISPLAY_ADS(7L),
	ADMOB_APP_ID,
	DEFAULT_AD_UNIT_ID;

	private Object defaultValue;

	AdMobRemoteConfigParameter() {
		this(null);
	}

	AdMobRemoteConfigParameter(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return AdMobAppModule.getAdMobAppContext().getBuildConfigValue(name(), defaultValue);
	}
}

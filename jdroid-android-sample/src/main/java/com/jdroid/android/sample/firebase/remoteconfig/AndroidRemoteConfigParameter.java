package com.jdroid.android.sample.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigParameter;

public enum AndroidRemoteConfigParameter implements FirebaseRemoteConfigParameter {

	SAMPLE_CONFIG_1("defaultConfigValue1"),
	SAMPLE_CONFIG_2("default", true),
	SAMPLE_CONFIG_3(null);

	private Object defaultValue;
	private Boolean isUserProperty;

	AndroidRemoteConfigParameter(Object defaultValue) {
		this(defaultValue, false);
	}

	AndroidRemoteConfigParameter(Object defaultValue, Boolean isUserProperty) {
		this.defaultValue = defaultValue;
		this.isUserProperty = isUserProperty;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Boolean isUserProperty() {
		return isUserProperty;
	}
}

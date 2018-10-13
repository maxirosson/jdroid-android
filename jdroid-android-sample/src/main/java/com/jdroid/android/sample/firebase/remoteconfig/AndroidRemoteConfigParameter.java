package com.jdroid.android.sample.firebase.remoteconfig;

import com.jdroid.java.remoteconfig.RemoteConfigParameter;

public enum AndroidRemoteConfigParameter implements RemoteConfigParameter {

	SAMPLE_CONFIG_1("defaultConfigValue1"),
	SAMPLE_CONFIG_2("default"),
	SAMPLE_CONFIG_3(null),
	INTERSTITIAL_ENABLED(true),
	PRIVACY_POLICY_URL("https://google.com");

	private Object defaultValue;

	AndroidRemoteConfigParameter(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}
}

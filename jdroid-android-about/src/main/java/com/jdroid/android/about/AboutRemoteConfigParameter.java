package com.jdroid.android.about;

import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;

public enum AboutRemoteConfigParameter implements RemoteConfigParameter {
	
	RATE_APP_MIN_DAYS_SINCE_LAST_RESPONSE(90),
	RATE_APP_MIN_DAYS_SINCE_FIRST_APP_LOAD(7),
	RATE_APP_MIN_APP_LOADS(10),
	RATE_APP_MIN_DAYS_SINCE_LAST_CRASH(21);

	private Object defaultValue;

	AboutRemoteConfigParameter() {
		this(null);
	}

	AboutRemoteConfigParameter(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return AboutAppModule.get().getAboutContext().getBuildConfigValue(name(), defaultValue);
	}

	@Override
	public Boolean isUserProperty() {
		return false;
	}
}

package com.jdroid.android.firebase.remoteconfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

public class StaticFirebaseRemoteConfigValue implements FirebaseRemoteConfigValue {

	private RemoteConfigParameter remoteConfigParameter;

	public StaticFirebaseRemoteConfigValue(RemoteConfigParameter remoteConfigParameter) {
		this.remoteConfigParameter = remoteConfigParameter;
	}

	@Override
	public long asLong() throws IllegalArgumentException {
		return (long)remoteConfigParameter.getDefaultValue();
	}

	@Override
	public double asDouble() throws IllegalArgumentException {
		return (double)remoteConfigParameter.getDefaultValue();
	}

	@Override
	public String asString() {
		return remoteConfigParameter.getDefaultValue().toString();
	}

	@Override
	public byte[] asByteArray() {
		return (byte[])remoteConfigParameter.getDefaultValue();
	}

	@Override
	public boolean asBoolean() throws IllegalArgumentException {
		return (boolean)remoteConfigParameter.getDefaultValue();
	}

	@Override
	public int getSource() {
		return FirebaseRemoteConfig.VALUE_SOURCE_STATIC;
	}
}

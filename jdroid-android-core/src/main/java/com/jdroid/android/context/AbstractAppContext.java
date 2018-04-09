package com.jdroid.android.context;

public abstract class AbstractAppContext {

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property) {
		return BuildConfigUtils.getBuildConfigValue(property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return BuildConfigUtils.getBuildConfigValue(property, defaultValue);
	}

	public Boolean getBuildConfigBoolean(String property, Boolean defaultValue) {
		return BuildConfigUtils.getBuildConfigValue(property, defaultValue);
	}
}

package com.jdroid.android.context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public class BuildConfigResolver {

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property, defaultValue);
	}

	private Class<?> getBuildConfigClass() {
		return ReflectionUtils.getClass(AbstractApplication.get().getManifestPackageName() + ".BuildConfig");
	}
}

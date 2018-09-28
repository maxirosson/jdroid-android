package com.jdroid.android.context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public class BuildConfigResolver {

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T getBuildConfigValue(@NonNull String property, @Nullable Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property, defaultValue);
	}

	private Class<?> getBuildConfigClass() {
		return ReflectionUtils.getClass(AbstractApplication.get().getManifestPackageName() + ".BuildConfig");
	}
}

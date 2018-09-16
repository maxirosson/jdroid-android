package com.jdroid.android.sample;

import com.jdroid.android.context.BuildConfigResolver;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class TestBuildConfigResolver extends BuildConfigResolver {

	private static Map<String, Object> props = Maps.newHashMap();

	static {
		props.put("BUILD_TYPE", "test");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)(props.containsKey(property) ? props.get(property) : defaultValue);
	}
}

package com.jdroid.android.firebase.remoteconfig;

import android.support.annotation.RestrictTo;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.remoteconfig.RemoteConfigLoader;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.TypeUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

public class MockRemoteConfigLoader implements RemoteConfigLoader {

	private static final Logger LOGGER = LoggerUtils.getLogger(MockRemoteConfigLoader.class);

	private static final String MOCKS_ENABLED = "firebase.remote.config.mocks.enabled";

	private static Boolean mocksEnabled;

	private SharedPreferencesHelper sharedPreferencesHelper;
	private Map<String, String> cache;

	public static MockRemoteConfigLoader get() {
		return ((MockRemoteConfigLoader)AbstractApplication.get().getRemoteConfigLoader());
	}

	public MockRemoteConfigLoader(Boolean fetch) {
		sharedPreferencesHelper = SharedPreferencesHelper.get(MockRemoteConfigLoader.class);
		cache = fetch ? (Map<String, String>)sharedPreferencesHelper.loadAllPreferences() : Maps.newHashMap();
	}

	@Override
	public void fetch() {
		// Do nothing
	}

	@Override
	public Object getObject(RemoteConfigParameter remoteConfigParameter) {
		return getValue(remoteConfigParameter, cache.get(remoteConfigParameter.getKey()));
	}

	@Override
	public String getString(RemoteConfigParameter remoteConfigParameter) {
		Object value = getValue(remoteConfigParameter, cache.get(remoteConfigParameter.getKey()));
		return value != null ? value.toString() : null;
	}

	@Override
	public List<String> getStringList(RemoteConfigParameter remoteConfigParameter) {
		return (List<String>)getValue(remoteConfigParameter, StringUtils.splitWithCommaSeparator(getString(remoteConfigParameter)));
	}

	@Override
	public Boolean getBoolean(RemoteConfigParameter remoteConfigParameter) {
		return (Boolean)getValue(remoteConfigParameter, TypeUtils.getBoolean(cache.get(remoteConfigParameter.getKey())));
	}

	@Override
	public Long getLong(RemoteConfigParameter remoteConfigParameter) {
		return (Long)getValue(remoteConfigParameter, TypeUtils.getLong(cache.get(remoteConfigParameter.getKey())));
	}

	@Override
	public Double getDouble(RemoteConfigParameter remoteConfigParameter) {
		return (Double)getValue(remoteConfigParameter, TypeUtils.getDouble(cache.get(remoteConfigParameter.getKey())));
	}

	private Object getValue(RemoteConfigParameter remoteConfigParameter, Object value) {
		if (value == null) {
			value = remoteConfigParameter.getDefaultValue();
		}
		if (LoggerUtils.isEnabled()) {
			LOGGER.info("Loaded Mock Remote Config. Key [" + remoteConfigParameter.getKey() + "] Value [" + value + "]");
		}
		return value;
	}

	@RestrictTo(LIBRARY)
	public void saveRemoteConfigParameter(RemoteConfigParameter remoteConfigParameter, String value) {
		sharedPreferencesHelper.savePreferenceAsync(remoteConfigParameter.getKey(), value);
		cache.put(remoteConfigParameter.getKey(), value);
	}

	@RestrictTo(LIBRARY)
	public static void setMocksEnabled(Boolean mocksEnabled) {
		MockRemoteConfigLoader.mocksEnabled = mocksEnabled;
		AbstractApplication.get().setRemoteConfigLoader(mocksEnabled ? new MockRemoteConfigLoader(false) : new FirebaseRemoteConfigLoader());
		SharedPreferencesHelper.get(MockRemoteConfigLoader.class).savePreference(MOCKS_ENABLED, mocksEnabled);
	}

	@RestrictTo(LIBRARY)
	public static Boolean isMocksEnabled() {
		if (mocksEnabled == null) {
			mocksEnabled = SharedPreferencesHelper.get(MockRemoteConfigLoader.class).loadPreferenceAsBoolean(MOCKS_ENABLED, false);
		}
		return mocksEnabled;
	}
}

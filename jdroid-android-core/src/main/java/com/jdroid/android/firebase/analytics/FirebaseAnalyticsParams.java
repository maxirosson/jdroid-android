package com.jdroid.android.firebase.analytics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FirebaseAnalyticsParams {

	private static final int PARAMETER_NAME_MAX_CHARS_LONG = 40;
	private static final int PARAMETER_VALUE_MAX_CHARS_LONG = 100;

	private Bundle bundle;

	public FirebaseAnalyticsParams(@NonNull Bundle bundle) {
		this.bundle = bundle;
	}

	public FirebaseAnalyticsParams() {
		this(new Bundle());
	}

	public void put(@NonNull String key, @Nullable String value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			if (value.length() > PARAMETER_VALUE_MAX_CHARS_LONG) {
				FirebaseAnalyticsHelper.LOGGER.warn("Parameter value [" + value + "] for key [" + key + "] must be " + PARAMETER_VALUE_MAX_CHARS_LONG + " chars long as maximum.");
				value = value.substring(0, PARAMETER_VALUE_MAX_CHARS_LONG - 1);
			}
			bundle.putString(key, value);
		}
	}

	public void put(@NonNull String key, @Nullable Integer value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			bundle.putLong(key, value);
		}
	}

	public void put(@NonNull String key, @Nullable Long value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			bundle.putLong(key, value);
		}
	}

	public void put(@NonNull String key, @Nullable Float value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			bundle.putDouble(key, value);
		}
	}

	public void put(@NonNull String key, @Nullable Double value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			bundle.putDouble(key, value);
		}
	}

	public void put(@NonNull String key, @Nullable Boolean value) {
		key = getSanitizedKey(key);
		if (value != null) {
			key = getSanitizedKey(key);
			bundle.putString(key, value.toString());
		}
	}

	private String getSanitizedKey(@NonNull String key) {
		if (key.length() > PARAMETER_NAME_MAX_CHARS_LONG) {
			FirebaseAnalyticsHelper.LOGGER.warn("Parameter key [" + key + "] must be " + PARAMETER_NAME_MAX_CHARS_LONG + " chars long as maximum.");
			key = key.substring(0, PARAMETER_NAME_MAX_CHARS_LONG - 1);
		}
		return key;
	}

	public Bundle getBundle() {
		return bundle;
	}

	@Override
	public String toString() {
		return bundle.toString();
	}
}

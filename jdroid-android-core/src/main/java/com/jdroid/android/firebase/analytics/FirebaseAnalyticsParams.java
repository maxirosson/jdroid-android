package com.jdroid.android.firebase.analytics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FirebaseAnalyticsParams {
	
	private Bundle bundle;
	
	public FirebaseAnalyticsParams(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public FirebaseAnalyticsParams() {
		this(new Bundle());
	}
	
	public void put(@NonNull String key, @Nullable String value) {
		bundle.putString(key, value);
	}
	
	public void put(@NonNull String key, @Nullable Integer value) {
		bundle.putLong(key, value);
	}
	
	public void put(@NonNull String key, @Nullable Long value) {
		bundle.putLong(key, value);
	}
	
	public void put(@NonNull String key, @Nullable Float value) {
		bundle.putDouble(key, value);
	}
	
	public void put(@NonNull String key, @Nullable Double value) {
		bundle.putDouble(key, value);
	}
	
	public void put(@NonNull String key, @Nullable Boolean value) {
		if (value != null) {
			bundle.putString(key, value.toString());
		}
	}
	
	public Bundle getBundle() {
		return bundle;
	}
}

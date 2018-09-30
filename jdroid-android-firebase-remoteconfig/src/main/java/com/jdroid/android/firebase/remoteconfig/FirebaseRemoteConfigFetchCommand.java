package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class FirebaseRemoteConfigFetchCommand extends ServiceCommand {

	private static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	private static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";

	public void start(long cacheExpirationSeconds, final Boolean setExperimentUserProperty) {
		Bundle bundle = new Bundle();
		bundle.putLong(FirebaseRemoteConfigFetchCommand.CACHE_EXPIRATION_SECONDS, cacheExpirationSeconds);
		bundle.putBoolean(FirebaseRemoteConfigFetchCommand.SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
		start(bundle);
	}

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		Long cacheExpirationSeconds = bundle.getLong(CACHE_EXPIRATION_SECONDS);
		Boolean setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY);
		FirebaseRemoteConfigLoader.get().fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return false;
	}
}

package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class FirebaseRemoteConfigFetchCommand extends ServiceCommand {

	public static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	public static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		Long cacheExpirationSeconds = bundle.getLong(CACHE_EXPIRATION_SECONDS);
		Boolean setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY);
		FirebaseRemoteConfigLoader.get().fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return false;
	}
}

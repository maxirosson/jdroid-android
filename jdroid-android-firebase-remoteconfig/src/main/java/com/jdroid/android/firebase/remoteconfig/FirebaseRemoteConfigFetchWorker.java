package com.jdroid.android.firebase.remoteconfig;

import android.support.annotation.NonNull;

import com.jdroid.android.jetpack.work.AbstractWorker;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class FirebaseRemoteConfigFetchWorker extends AbstractWorker {
	
	private static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	private static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";
	
	public static void start(long cacheExpirationSeconds, final Boolean setExperimentUserProperty) {
		OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(FirebaseRemoteConfigFetchWorker.class);
		
		Data.Builder dataBuilder = new Data.Builder();
		dataBuilder.putLong(CACHE_EXPIRATION_SECONDS, cacheExpirationSeconds);
		dataBuilder.putBoolean(SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
		requestBuilder.setInputData(dataBuilder.build());
		
		Constraints.Builder constrainsBuilder = new Constraints.Builder();
		constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
		requestBuilder.setConstraints(constrainsBuilder.build());
		
		WorkManager.getInstance().enqueue(requestBuilder.build());
	}
	
	@NonNull
	@Override
	protected Result onWork() {
		Long cacheExpirationSeconds = getInputData().getLong(CACHE_EXPIRATION_SECONDS, 0);
		Boolean setExperimentUserProperty = getInputData().getBoolean(SET_EXPERIMENT_USER_PROPERTY, false);
		FirebaseRemoteConfigLoader.get().fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return Result.SUCCESS;
	}
	
}

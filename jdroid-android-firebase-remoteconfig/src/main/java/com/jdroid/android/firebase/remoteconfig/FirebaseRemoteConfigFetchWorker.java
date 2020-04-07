package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;

import com.jdroid.android.jetpack.work.AbstractWorker;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

public class FirebaseRemoteConfigFetchWorker extends AbstractWorker {
	
	public static final String WORK_MANAGER_TAG = "firebase_remote_config";
	
	private static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	private static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";

	public FirebaseRemoteConfigFetchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	public static void start(long cacheExpirationSeconds, final Boolean setExperimentUserProperty) {
		OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(FirebaseRemoteConfigFetchWorker.class);
		
		Data.Builder dataBuilder = new Data.Builder();
		dataBuilder.putLong(CACHE_EXPIRATION_SECONDS, cacheExpirationSeconds);
		dataBuilder.putBoolean(SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
		requestBuilder.setInputData(dataBuilder.build());
		
		Constraints.Builder constrainsBuilder = new Constraints.Builder();
		constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
		requestBuilder.setConstraints(constrainsBuilder.build());
		
		requestBuilder.addTag(WORK_MANAGER_TAG);
		
		WorkManager.getInstance().beginUniqueWork(FirebaseRemoteConfigFetchWorker.class.getSimpleName(), ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue();
	}
	
	@NonNull
	@Override
	protected Result onWork() {
		Long cacheExpirationSeconds = getInputData().getLong(CACHE_EXPIRATION_SECONDS, 0);
		Boolean setExperimentUserProperty = getInputData().getBoolean(SET_EXPERIMENT_USER_PROPERTY, false);
		FirebaseRemoteConfigLoader.get().fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return Result.success();
	}
	
}

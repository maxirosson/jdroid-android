package com.jdroid.android.firebase.fcm.remoteconfig;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.jetpack.work.AbstractWorker;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

public class RemoteConfigFetchWorker extends AbstractWorker {

	public static void start() {
		OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(RemoteConfigFetchWorker.class);

		Constraints.Builder constrainsBuilder = new Constraints.Builder();
		constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
		requestBuilder.setConstraints(constrainsBuilder.build());

		WorkManager.getInstance().beginUniqueWork(RemoteConfigFetchWorker.class.getSimpleName(), ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue();
	}

	public RemoteConfigFetchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	protected Result onWork() {
		AbstractApplication.get().getRemoteConfigLoader().fetch();
		return Result.SUCCESS;
	}
}

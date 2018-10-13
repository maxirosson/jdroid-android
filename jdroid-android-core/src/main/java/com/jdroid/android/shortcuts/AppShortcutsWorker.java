package com.jdroid.android.shortcuts;

import android.content.Context;
import android.os.Build;

import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.android.utils.AndroidUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

public class AppShortcutsWorker extends AbstractWorker {
	
	public static final String WORK_MANAGER_TAG = "app_shortcuts";

	public AppShortcutsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	public static void start() {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(AppShortcutsWorker.class);
			
			requestBuilder.addTag(WORK_MANAGER_TAG);
			
			WorkManager.getInstance().beginUniqueWork(AppShortcutsWorker.class.getSimpleName(), ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue();
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N_MR1)
	@NonNull
	@Override
	protected Result onWork() {
		AppShortcutsHelper.setDynamicShortcuts(AppShortcutsHelper.getInitialShortcutInfos());
		return Result.SUCCESS;
	}
}

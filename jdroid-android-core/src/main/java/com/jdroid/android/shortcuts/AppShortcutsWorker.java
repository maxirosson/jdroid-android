package com.jdroid.android.shortcuts;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.android.utils.AndroidUtils;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AppShortcutsWorker extends AbstractWorker {

	public static void start() {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			OneTimeWorkRequest.Builder requestBuilder = new OneTimeWorkRequest.Builder(AppShortcutsWorker.class);
			WorkManager.getInstance().enqueue(requestBuilder.build());
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

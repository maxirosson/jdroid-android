package com.jdroid.android.sample.ui.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker4 extends AbstractWorker {

	public SampleWorker4(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	protected Worker.Result onWork() {
		ExecutorUtils.sleep(20, TimeUnit.SECONDS);
		return Result.SUCCESS;
	}
	
	// TODO
//	@Nullable
//	@Override
//	protected Long getMinimumTimeBetweenExecutions() {
//		return TimeUnit.MINUTES.toMillis(1);
//	}
}

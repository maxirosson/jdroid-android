package com.jdroid.android.sample.ui.service;

import android.content.Context;

import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker3 extends AbstractWorker {

	public SampleWorker3(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	protected Worker.Result onWork() {
		ExecutorUtils.sleep(30, TimeUnit.SECONDS);
		Data.Builder outputBuilder = new Data.Builder();
		outputBuilder.putString("result", "ok");
		setOutputData(outputBuilder.build());
		return Result.SUCCESS;
	}
}

package com.jdroid.android.sample.ui.service;

import android.support.annotation.NonNull;

import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Worker;

public class SampleWorker3 extends AbstractWorker {
	
	@NonNull
	@Override
	protected Worker.Result onWork() {
		ExecutorUtils.sleep(30, TimeUnit.SECONDS);
		return Result.SUCCESS;
	}
}

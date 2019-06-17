package com.jdroid.android.sample.ui.service;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

public class SampleServiceCommand4 extends AbstractSampleServiceCommand {

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		ExecutorUtils.INSTANCE.sleep(20, TimeUnit.SECONDS);
		return false;
	}

	@Nullable
	@Override
	protected Long getMinimumTimeBetweenExecutions() {
		return TimeUnit.MINUTES.toMillis(1);
	}
}

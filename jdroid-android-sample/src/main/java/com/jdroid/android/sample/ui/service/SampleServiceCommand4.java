package com.jdroid.android.sample.ui.service;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

public class SampleServiceCommand4 extends ServiceCommand {

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		ExecutorUtils.sleep(20, TimeUnit.SECONDS);
		return false;
	}
	
	@Nullable
	@Override
	protected Long getMinimumTimeBetweenExecutions() {
		return TimeUnit.MINUTES.toMillis(1);
	}
}

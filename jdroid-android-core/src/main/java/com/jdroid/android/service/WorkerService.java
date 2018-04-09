package com.jdroid.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.performance.TraceHelper;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

public abstract class WorkerService extends IntentService {

	private static String TAG = WorkerService.class.getSimpleName();

	public WorkerService() {
		super(TAG);
	}
	
	public WorkerService(String name) {
		super(name);
	}
	
	@Override
	protected final void onHandleIntent(Intent intent) {
		String tag = getClass().getSimpleName();
		if (intent != null) {
			Trace trace = null;
			try {
				tag = getTag(intent);
				if (timingTrackingEnabled()) {
					trace = TraceHelper.startTrace(tag);
				}
				LoggerUtils.getLogger(tag).info("Executing service.");
				long startTime = DateUtils.nowMillis();
				doExecute(intent);
				long executionTime = DateUtils.nowMillis() - startTime;
				LoggerUtils.getLogger(tag).info("Service finished. Execution time: " + DateUtils.formatDuration(executionTime));
				
				if (trace != null) {
					trace.incrementCounter("success");
				}
			} catch (Exception e) {
				if (trace != null) {
					trace.incrementCounter("failure");
				}
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			} finally {
				if (trace != null) {
					trace.stop();
				}
			}
		} else {
			LoggerUtils.getLogger(tag).warn("Null intent when starting the service: " + getClass().getName());
		}
	}

	protected Boolean timingTrackingEnabled() {
		return true;
	}
	
	protected String getTag(Intent intent) {
		return getClass().getSimpleName();
	}

	protected abstract void doExecute(Intent intent);
	
	public static void runIntentInService(Context context, Bundle bundle, Class<? extends WorkerService> serviceClass) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		runIntentInService(context, intent, serviceClass);
	}
	
	public static void runIntentInService(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		try {
			context.startService(getServiceIntent(context, intent, serviceClass));
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	public static Intent getServiceIntent(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		intent.setClass(context, serviceClass);
		return intent;
	}
	
}

package com.jdroid.android.firebase.jobdispatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.AbstractWorkerService;
import com.jdroid.java.utils.LoggerUtils;

import java.io.Serializable;

public abstract class ServiceCommand implements Serializable {
	
	final static String COMMAND_EXTRA = "com.jdroid.android.firebase.jobdispatcher.command";
	
	public void start() {
		start(null);
	}

	public final void start(Bundle bundle) {
		startFirebaseJobService(bundle);
	}
	
	@Deprecated
	public void startWorkerService(Bundle bundle) {
		LoggerUtils.getLogger(getClass()).info("Starting Worker Service");
		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.putExtra(ServiceCommand.COMMAND_EXTRA, getClass().getName());
		AbstractWorkerService.runIntentInService(AbstractApplication.get(), intent, CommandWorkerService.class);
	}
	
	public void startFirebaseJobService(Bundle bundle) {
		LoggerUtils.getLogger(getClass()).info("Scheduling Firebase Job Service");
		
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable(ServiceCommand.COMMAND_EXTRA, getClass().getName());
		
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(AbstractApplication.get()));
		Job.Builder builder = createJobBuilder(dispatcher, bundle);
		builder.setService(CommandJobService.class);
		dispatcher.mustSchedule(builder.build());
	}

	protected Job.Builder createJobBuilder(FirebaseJobDispatcher dispatcher, Bundle bundle) {
		Job.Builder builder = dispatcher.newJobBuilder();
		builder.setRecurring(false); // one-off job
		builder.setLifetime(Lifetime.FOREVER);
		builder.setTag(getClass().getSimpleName());
		builder.setTrigger(Trigger.NOW);
		builder.setReplaceCurrent(false); // don't overwrite an existing job with the same tag
		builder.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL); // retry with exponential backoff
		builder.setExtras(bundle);
		return builder;
	}

	@WorkerThread
	protected abstract boolean execute(Context context, Bundle bundle);

	@Deprecated
	public void setInstantExecutionRequired(Boolean isInstantExecutionRequired) {
	}
}

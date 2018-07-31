package com.jdroid.android.firebase.jobdispatcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class ServiceCommand implements Serializable {
	
	final static String COMMAND_EXTRA = "com.jdroid.android.firebase.jobdispatcher.command";
	
	private final static Map<Class<? extends ServiceCommand>, Long> LAST_EXECUTION_MAP = new HashMap<>();
	
	public void start() {
		start(null);
	}
	
	public final void start(Bundle bundle) {
		Long lastExecution = LAST_EXECUTION_MAP.get(getClass());
		Long minimumTimeBetweenExecutions = getMinimumTimeBetweenExecutions();
		if (lastExecution == null || minimumTimeBetweenExecutions == null || System.currentTimeMillis() - lastExecution > minimumTimeBetweenExecutions) {
			LAST_EXECUTION_MAP.put(getClass(), System.currentTimeMillis());
			startFirebaseJobService(bundle);
		} else {
			LoggerUtils.getLogger(getClass()).info("Firebase Job Service schedule skipped");
		}
	}
	
	public void startFirebaseJobService(Bundle bundle) {
		LoggerUtils.getLogger(getClass()).info("Scheduling Firebase Job Service");
		
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable(ServiceCommand.COMMAND_EXTRA, getClass().getName());
		
		try {
			FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(AbstractApplication.get()));
			Job.Builder builder = createJobBuilder(dispatcher, bundle);
			//builder.setService(CommandJobService.class);
			dispatcher.mustSchedule(builder.build());
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	@Nullable
	protected Long getMinimumTimeBetweenExecutions() {
		return null;
	}
	
	@NonNull
	protected Job.Builder createJobBuilder(FirebaseJobDispatcher dispatcher, Bundle bundle) {
		Job.Builder builder = dispatcher.newJobBuilder();
		builder.setRecurring(false); // one-off job
		builder.setLifetime(Lifetime.FOREVER);
		builder.setTag(getClass().getSimpleName());
		builder.setTrigger(Trigger.NOW);
		builder.setReplaceCurrent(false); // don't overwrite an existing job with the same tag
		builder.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL); // retry with exponential backoff
		builder.setConstraints(Constraint.ON_ANY_NETWORK);
		builder.setExtras(bundle);
		return builder;
	}
	
	@WorkerThread
	protected abstract boolean execute(Context context, Bundle bundle);
	
}

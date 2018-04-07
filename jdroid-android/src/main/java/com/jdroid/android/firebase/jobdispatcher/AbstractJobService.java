package com.jdroid.android.firebase.jobdispatcher;


import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.performance.TraceHelper;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

// TODO See if we should extend SimpleJobService
public abstract class AbstractJobService extends JobService {
	
	@MainThread
	@Override
	public final boolean onStartJob(final JobParameters jobParameters) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				Boolean needsReschedule = false;
				Trace trace = null;
				String tag = getClass().getSimpleName();
				try {
					tag = getTag(jobParameters);
					if (timingTrackingEnabled()) {
						trace = TraceHelper.startTrace(tag);
					}
					LoggerUtils.getLogger(tag).info("Executing Firebase Job.");
					long startTime = DateUtils.nowMillis();
					needsReschedule = onRunJob(jobParameters);
					long executionTime = DateUtils.nowMillis() - startTime;
					LoggerUtils.getLogger(tag).info("Firebase Job finished successfully. NeedsReschedule: " + needsReschedule + " - Execution time: " + DateUtils.formatDuration(executionTime));
				} catch (Exception e) {
					needsReschedule = needsReschedule(e);
					LoggerUtils.getLogger(tag).error("Firebase Job finished with error. NeedsReschedule: " + needsReschedule);
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				} finally {
					if (trace != null) {
						trace.stop();
					}
					jobFinished(jobParameters, needsReschedule);
				}
			}
		});
		return true;
	}
	
	protected Boolean timingTrackingEnabled() {
		return true;
	}
	
	@WorkerThread
	public abstract boolean onRunJob(JobParameters jobParameters);
	
	protected Boolean needsReschedule(Throwable throwable) {
		return throwable instanceof ConnectionException;
	}
	
	protected String getTag(JobParameters jobParameters) {
		return getClass().getSimpleName();
	}
	
	@MainThread
	@Override
	public boolean onStopJob(JobParameters jobParameters) {
		return true;
	}
}

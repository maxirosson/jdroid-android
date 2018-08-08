package com.jdroid.android.jetpack.work;

import android.support.annotation.NonNull;

import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.performance.TraceHelper;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

import androidx.work.Worker;

public abstract class AbstractWorker extends Worker {
	
	@NonNull
	@Override
	public final Result doWork() {
		Trace trace = null;
		String trackingTag = getClass().getSimpleName();
		Result result = null;
		try {
			trackingTag = getTrackingTag();
			if (timingTrackingEnabled()) {
				trace = TraceHelper.startTrace(trackingTag);
			}
			LoggerUtils.getLogger(trackingTag).info("Executing Worker.");
			long startTime = DateUtils.nowMillis();
			result = onWork();
			long executionTime = DateUtils.nowMillis() - startTime;
			LoggerUtils.getLogger(trackingTag).info("Worker finished. Result: " + result + ". Run attempt: " + getRunAttemptCount() + ". Execution time: " + DateUtils.formatDuration(executionTime));
		} catch (Throwable e) {
			result = getResult(e);
			LoggerUtils.getLogger(trackingTag).error("Worker finished with exception. Result: " + result + ". Run attempt: " + getRunAttemptCount());
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		} finally {
			if (trace != null) {
				if (result != null) {
					trace.putAttribute("result", result.name());
					trace.incrementMetric(result.name(), 1);
				}
				trace.stop();
			}
		}
		return result;
	}
	
	@NonNull
	protected abstract Result onWork();
	
	@NonNull
	protected Result getResult(Throwable throwable) {
		return throwable instanceof ConnectionException ? Result.RETRY : Result.FAILURE;
	}
	
	@NonNull
	protected Boolean timingTrackingEnabled() {
		return true;
	}
	
	@NonNull
	protected String getTrackingTag() {
		return getClass().getSimpleName();
	}
}

package com.jdroid.android.sample.ui.service;

import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.jobdispatcher.AbstractJobService;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

import androidx.annotation.MainThread;

public class SampleFirebaseJobService extends AbstractJobService {

	@MainThread
	@Override
	public boolean onRunJob(JobParameters jobParameters) {
		Boolean fail = jobParameters.getExtras().getBoolean("fail");
		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(jobParameters.getExtras().get("a").toString());

			NotificationUtils.INSTANCE.sendNotification(IdGenerator.INSTANCE.getIntId(), builder);
			return false;
		}
	}

	public static void runIntentInService(Bundle bundle) {
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(AbstractApplication.get()));
		Job.Builder builder = dispatcher.newJobBuilder();
		builder.setRecurring(false); // one-off job
		builder.setLifetime(Lifetime.FOREVER);
		builder.setTag(SampleFirebaseJobService.class.getSimpleName());
		builder.setService(SampleFirebaseJobService.class);
		builder.setTrigger(Trigger.executionWindow(0, 5));
		builder.setReplaceCurrent(false);
		builder.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL);
		builder.setExtras(bundle);
		dispatcher.mustSchedule(builder.build());
	}
}

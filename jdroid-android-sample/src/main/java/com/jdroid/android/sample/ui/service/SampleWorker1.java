package com.jdroid.android.sample.ui.service;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

public class SampleWorker1 extends AbstractWorker {

	public SampleWorker1(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	protected Result onWork() {
		
		Boolean fail = getInputData().getBoolean("fail", false);
		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(getInputData().getString("a"));
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return Result.SUCCESS;
		}
	}
}

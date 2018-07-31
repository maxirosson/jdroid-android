package com.jdroid.android.sample.ui.service;

import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.jetpack.work.AbstractWorker;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.IdGenerator;

import androidx.work.Worker;

public class SampleWorker2 extends AbstractWorker {
	
	@NonNull
	@Override
	protected Worker.Result onWork() {
		Boolean fail = getInputData().getBoolean("fail", false);
		if (fail) {
			throw new ConnectionException("Failing service");
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

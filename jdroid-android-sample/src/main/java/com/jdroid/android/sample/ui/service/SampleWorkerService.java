package com.jdroid.android.sample.ui.service;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.android.service.AbstractWorkerService;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

public class SampleWorkerService extends AbstractWorkerService {

	@Override
	protected void doExecute(@NonNull Intent intent) {

		Boolean fail = intent.getExtras().getBoolean("fail");

		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(intent.getExtras().get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
		}
	}

	public static void runIntentInService(Intent intent) {
		AbstractWorkerService.runIntentInService(AbstractApplication.get(), intent, SampleWorkerService.class);
	}
}

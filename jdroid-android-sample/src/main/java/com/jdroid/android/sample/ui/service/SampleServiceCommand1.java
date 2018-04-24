package com.jdroid.android.sample.ui.service;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand1 extends AbstractSampleServiceCommand {

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		Boolean fail = bundle.getBoolean("fail");
		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(bundle.get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return false;
		}
	}
}

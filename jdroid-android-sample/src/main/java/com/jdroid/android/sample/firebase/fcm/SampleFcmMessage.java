package com.jdroid.android.sample.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.AbstractFcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.date.DateTimeFormat;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.TypeUtils;

import java.util.Date;

public class SampleFcmMessage extends AbstractFcmMessage {

	@Override
	public String getMessageKey() {
		return "sampleMessage";
	}

	@Override
	public void handle(RemoteMessage remoteMessage) {
		NotificationBuilder builder = new NotificationBuilder("pushNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
		builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
		builder.setTicker("Sample Ticker");
		builder.setContentTitle("Sample Content Title");
		String description = "Sample Content Description";
		Long timestamp = TypeUtils.getLong(remoteMessage.getData().get("timestamp"));
		if (timestamp != null) {
			description = DateUtils.format(new Date(timestamp), DateTimeFormat.YYYYMMDDHHMMSSSSS);
		}
		builder.setContentText(description);
		builder.setWhen(DateUtils.nowMillis());

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
	}
}

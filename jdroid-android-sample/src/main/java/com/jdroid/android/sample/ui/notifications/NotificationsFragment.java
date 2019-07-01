package com.jdroid.android.sample.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.glide.GlideBitmapLoader;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.StringUtils;

public class NotificationsFragment extends AbstractFragment {

	private EditText notificationName;
	private EditText notificationChannel;
	private EditText contentTitle;
	private EditText contentText;
	private EditText largeIconUrlEditText;
	private CheckBox largeIconDrawable;
	private EditText urlEditText;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.notifications_fragment;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		notificationName = findView(R.id.notificationName);
		notificationName.setText("myNotification");

		notificationChannel = findView(R.id.notificationChannel);
		notificationChannel.setText(AndroidNotificationChannelType.DEFAULT_IMPORTANCE.getChannelId());

		contentTitle = findView(R.id.contentTitle);
		contentTitle.setText(R.string.contentTitleSample);

		contentText = findView(R.id.contentText);
		contentText.setText(R.string.contextTextSample);

		largeIconUrlEditText = findView(R.id.largeIconUrl);
		largeIconUrlEditText.setText("https://jdroidtools.com/images/gradle.png");

		urlEditText = findView(R.id.url);
		urlEditText.setText("https://jdroidtools.com/uri");

		largeIconDrawable = findView(R.id.largeIconDrawable);
		largeIconDrawable.setChecked(false);

		findView(R.id.sendNotification).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AppExecutors.INSTANCE.getNetworkIOExecutor().execute(new Runnable() {
					@Override
					public void run() {
						NotificationBuilder builder = new NotificationBuilder(notificationName.getText().toString(), notificationChannel.getText().toString());
						builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());

						if (largeIconDrawable.isChecked()) {
							builder.setLargeIcon(R.drawable.marker);
						} else {
							String largeIconUrl = largeIconUrlEditText.getText().toString();
							if (StringUtils.isNotEmpty(largeIconUrl)) {
								builder.setLargeIcon(new GlideBitmapLoader(largeIconUrl));
							}
						}

						builder.setContentTitle(contentTitle.getText().toString());
						builder.setContentText(contentText.getText().toString());

						String url = urlEditText.getText().toString();
						if (StringUtils.isNotBlank(url)) {
							builder.setSingleTopUrl(url);
						} else {
							Intent intent = new Intent(getActivity(), AbstractApplication.get().getHomeActivityClass());
							builder.setContentIntent(intent);
						}

						builder.setWhen(DateUtils.INSTANCE.nowMillis());
						builder.setBlueLight();
						builder.setDefaultSound();

						NotificationUtils.sendNotification(IdGenerator.INSTANCE.getIntId(), builder);
					}
				});
			}
		});

		findView(R.id.cancelNotifications).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NotificationUtils.cancelAllNotifications();
			}
		});
	}
}

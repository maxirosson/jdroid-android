package com.jdroid.android.about.appinvite;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jdroid.android.about.R;
import com.jdroid.android.firebase.invites.AppInviteSender;
import com.jdroid.android.utils.LocalizationUtils;

public class AppInviteView extends RelativeLayout {

	private TextView titleTextView;
	private String title;

	private TextView subtitleTextView;
	private String subtitle;

	private AppInviteSender appInviteSender;

	public AppInviteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AppInviteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AppInviteView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.jdroid_app_invite_view, this, true);

		titleTextView = findViewById(R.id.title);
		subtitleTextView = findViewById(R.id.subTitle);
	}

	public void configure(@Nullable Activity activity) {

		if (title == null) {
			title = LocalizationUtils.getString(R.string.jdroid_appInviteButtonTitle);
		}
		titleTextView.setText(title);

		if (subtitle == null) {
			subtitle = LocalizationUtils.getString(R.string.jdroid_appInviteButtonSubtitle, activity.getString(R.string.jdroid_appName));
		}
		subtitleTextView.setText(subtitle);

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (appInviteSender == null) {
					appInviteSender = new AppInviteSender();
					appInviteSender.setActivity(activity);
				}
				appInviteSender.sendInvitation();
			}
		});
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setAppInviteSender(AppInviteSender appInviteSender) {
		this.appInviteSender = appInviteSender;
	}
}

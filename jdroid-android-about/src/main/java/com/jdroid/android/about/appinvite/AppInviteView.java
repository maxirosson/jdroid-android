package com.jdroid.android.about.appinvite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.about.R;
import com.jdroid.java.utils.RandomUtils;

public class AppInviteView extends RelativeLayout {

	private int requestCode = RandomUtils.get16BitsInt();

	private TextView titleTextView;
	private String title;

	private TextView subtitleTextView;
	private String subtitle;

	private String appInviteTitle;
	private String appInviteMessage;
	private String appInviteDeeplink;

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
		LayoutInflater.from(context).inflate(R.layout.app_invite_view, this, true);

		titleTextView = (TextView)findViewById(R.id.title);
		subtitleTextView = (TextView)findViewById(R.id.subTitle);
	}

	public void configure(final Activity activity) {

		if (title == null) {
			title = activity.getString(R.string.appInviteButtonTitle);
		}
		titleTextView.setText(title);

		if (subtitle == null) {
			subtitle = activity.getString(R.string.appInviteButtonSubtitle, activity.getString(R.string.appName));
		}
		subtitleTextView.setText(subtitle);

		if (appInviteTitle == null) {
			appInviteTitle = AboutAppModule.get().getAboutContext().getAppInviteTitle();
		}

		if (appInviteMessage == null) {
			appInviteMessage = AboutAppModule.get().getAboutContext().getAppInviteMessage();
		}

		if (appInviteDeeplink == null) {
			appInviteDeeplink = AboutAppModule.get().getAboutContext().getAppInviteDeeplink();
		}

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppInviteInvitation.IntentBuilder intentBuilder = new AppInviteInvitation.IntentBuilder(appInviteTitle);
				intentBuilder.setMessage(appInviteMessage);
				intentBuilder.setDeepLink(Uri.parse(appInviteDeeplink));
				Intent intent = intentBuilder.build();
				activity.startActivityForResult(intent, requestCode);
			}
		});
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setAppInviteTitle(String appInviteTitle) {
		this.appInviteTitle = appInviteTitle;
	}

	public void setAppInviteMessage(String appInviteMessage) {
		this.appInviteMessage = appInviteMessage;
	}

	public void setAppInviteDeeplink(String appInviteDeeplink) {
		this.appInviteDeeplink = appInviteDeeplink;
	}
}

package com.jdroid.android.share;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.invites.AppInviteSender;

public class AppInviteSharingItem extends SharingItem {

	@Override
	public Drawable getAppIcon() {
		return DrawableCompat.wrap(AbstractApplication.get().getResources().getDrawable(R.drawable.jdroid_ic_mail_outline_black_48dp));
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}

	@Override
	public String getPackageName() {
		return null;
	}

	@Override
	public void share() {
		createAppInviteSender().sendInvitation();
	}

	protected AppInviteSender createAppInviteSender() {
		return new AppInviteSender();
	}
}

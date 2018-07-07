package com.jdroid.android.facebook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialNetwork;

public class FacebookHelper {

	public static void openPage(String pageId) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
			ActivityLauncher.startActivityNewTask(intent);
		} catch (ActivityNotFoundException e) {
			ActivityLauncher.startActivityNewTask(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + pageId)));
		} finally {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.FACEBOOK.getName(),
					SocialAction.OPEN_PROFILE, pageId);
		}
	}
}

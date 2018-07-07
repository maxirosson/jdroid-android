package com.jdroid.android.social.twitter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialNetwork;

public class TwitterHelper {
	
	public static Integer CHARACTERS_LIMIT = 140;
	public static Integer URL_CHARACTERS_COUNT = 22;
	
	public static void openProfile(String account) {
		try {
			Intent intent = new Intent();
			intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
			intent.putExtra("screen_name", account);
			ActivityLauncher.startActivityNewTask(intent);
		} catch (ActivityNotFoundException e) {
			Intent intent =
					new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + account));
			ActivityLauncher.startActivityNewTask(intent);
		} finally {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.TWITTER.getName(),
				SocialAction.OPEN_PROFILE, account);
		}
	}
}

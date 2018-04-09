package com.jdroid.android.social.twitter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialNetwork;
import com.jdroid.android.social.SocialAction;

public class TwitterHelper {
	
	public static Integer CHARACTERS_LIMIT = 140;
	public static Integer URL_CHARACTERS_COUNT = 22;
	
	public static void openProfile(String account) {
		try {
			Intent intent = new Intent();
			intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
			intent.putExtra("screen_name", account);
			AbstractApplication.get().getCurrentActivity().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			AbstractApplication.get().getCurrentActivity().startActivity(
				new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + account)));
		} finally {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.TWITTER.getName(),
				SocialAction.OPEN_PROFILE, account);
		}
	}
}

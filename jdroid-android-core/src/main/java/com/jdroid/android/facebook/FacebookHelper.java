package com.jdroid.android.facebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialNetwork;
import com.jdroid.android.social.SocialAction;

public class FacebookHelper {

	public static void openPage(Context context, String pageId) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			context.startActivity(
					new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + pageId)));
		} finally {
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(SocialNetwork.FACEBOOK.getName(),
					SocialAction.OPEN_PROFILE, pageId);
		}
	}
}

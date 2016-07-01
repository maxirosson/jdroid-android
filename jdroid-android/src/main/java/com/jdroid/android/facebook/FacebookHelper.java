package com.jdroid.android.facebook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;

public class FacebookHelper {

	private FacebookHelper(){}

	public static void openPage(String pageId) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
			AbstractApplication.get().getCurrentActivity().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			AbstractApplication.get().getCurrentActivity().startActivity(
					new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + pageId)));
		} finally {
			AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(AccountType.FACEBOOK,
					SocialAction.OPEN_PROFILE, pageId);
		}
	}
}

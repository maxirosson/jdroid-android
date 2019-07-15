package com.jdroid.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.Html;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.http.MimeType;

public class ShareUtils {

	public static void shareTextContent(Activity activity, String shareKey, int shareTitle, int shareSubject, int shareText) {
		shareTextContent(activity, shareKey, activity.getString(shareTitle), activity.getString(shareSubject),
			activity.getString(shareText));

		AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}

	public static void shareTextContent(Activity activity, String shareKey, String shareTitle, String shareSubject, String shareText) {
		Intent intent = createShareTextContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));

		AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}

	public static void shareHtmlContent(Activity activity, String shareKey, String shareTitle, String shareSubject, String shareText) {
		Intent intent = createShareHtmlContentIntent(shareSubject, shareText);
		ActivityLauncher.INSTANCE.startActivity(activity, Intent.createChooser(intent, shareTitle));

		AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}

	public static void share(Activity activity, SharingMedium sharingMedium, String shareKey, String shareText) {
		String applicationId = sharingMedium.getApplicationId();

		Intent intent = createShareTextContentIntent(null, shareText);
		intent.setPackage(applicationId);
		try {
			ActivityLauncher.INSTANCE.startActivity(activity, intent);
			AbstractApplication.get().getCoreAnalyticsSender().trackSocialInteraction(sharingMedium.getName(), SocialAction.SHARE, shareKey);
		} catch (ActivityNotFoundException e) {
			Integer installedAppVersionCode = ExternalAppsUtils.INSTANCE.getInstalledAppVersionCode(applicationId);
			String message = "ACTION_SEND not supported by " + applicationId;
			if (installedAppVersionCode != null) {
				message += " version " + installedAppVersionCode;
			}
			AbstractApplication.get().getExceptionHandler().logWarningException(message, e);
		}
	}

	public static Intent createShareTextContentIntent(String shareSubject, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.TEXT);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, shareText);
		return intent;
	}

	public static Intent createShareHtmlContentIntent(String shareSubject, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.HTML);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareText));
		return intent;
	}
}
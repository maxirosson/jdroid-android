package com.jdroid.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.EncodingUtils;

public class ShareUtils {

	public ShareUtils(){}
	
	public static void shareTextContent(String shareKey, int shareTitle, int shareSubject, int shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		shareTextContent(shareKey, activity.getString(shareTitle), activity.getString(shareSubject),
				activity.getString(shareText));
		
		AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}
	
	public static void shareTextContent(String shareKey, String shareTitle, String shareSubject, String shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		Intent intent = createShareTextContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));
		
		AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}
	
	public static void shareHtmlContent(String shareKey, String shareTitle, String shareSubject, String shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		Intent intent = createShareHtmlContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));
		
		AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(null, SocialAction.SHARE, shareKey);
	}
	
	public static void shareOnTwitter(String shareKey, String shareText) {
		share(ExternalAppsUtils.TWITTER_PACKAGE_NAME, AccountType.TWITTER, shareKey, shareText);
	}
	
	public static void shareOnFacebook(String shareKey, String shareText) {
		share(ExternalAppsUtils.FACEBOOK_PACKAGE_NAME, AccountType.FACEBOOK, shareKey, shareText);
	}
	
	public static void shareOnWhatsApp(String shareKey, String shareText) {
		share(ExternalAppsUtils.WHATSAPP_PACKAGE_NAME, AccountType.WHATSAPP, shareKey, shareText);
	}
	
	public static void shareOnTelegram(String shareKey, String shareText) {
		share(ExternalAppsUtils.TELEGRAM_PACKAGE_NAME, AccountType.TELEGRAM, shareKey, shareText);
	}
	
	public static void shareOnHangouts(String shareKey, String shareText) {
		share(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME, AccountType.HANGOUTS, shareKey, shareText);
	}
	
	public static void shareOnGooglePlus(String shareKey, String shareText) {
		share(ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME, AccountType.GOOGLE_PLUS, shareKey, shareText);
	}
	
	public static void shareOnSmsApp(String packageName, String shareKey, String shareText) {
		share(packageName, AccountType.SMS, shareKey, shareText);
	}
	
	private static void share(String packageName, AccountType accountType, String shareKey, String shareText) {
		Intent intent = createShareTextContentIntent(null, shareText);
		intent.setPackage(packageName);
		try {
			AbstractApplication.get().getCurrentActivity().startActivity(intent);
			AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(accountType, SocialAction.SHARE, shareKey);
		} catch (ActivityNotFoundException e) {
			Integer installedAppVersionCode = ExternalAppsUtils.getInstalledAppVersionCode(AbstractApplication.get(), packageName);
			String message = "ACTION_SEND not supported by " + packageName;
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
	
	public static Intent createOpenMail(String mailto, String subject) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		String uriText = "mailto:" + mailto + "?subject=" + EncodingUtils.encodeURL(subject);
		Uri uri = Uri.parse(uriText);
		intent.setData(uri);
		return intent;
	}
}
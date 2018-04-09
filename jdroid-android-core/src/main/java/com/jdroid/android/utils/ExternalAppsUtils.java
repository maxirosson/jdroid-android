package com.jdroid.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.java.utils.EncodingUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.File;

public class ExternalAppsUtils {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ExternalAppsUtils.class);
	
	public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
	public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
	public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
	public static final String TELEGRAM_PACKAGE_NAME = "org.telegram.messenger";
	public static final String HANGOUTS_PACKAGE_NAME = "com.google.android.talk";
	public static final String GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus";
	public static final String GOOGLE_MAPS_PACKAGE_NAME = "com.google.android.apps.maps";
	
	public static boolean isAppInstalled(Context context, String packageName) {
		return isAppInstalled(context, packageName, null);
	}
	
	public static boolean isAppInstalled(Context context, String packageName, Integer minimumVersionCode) {
		boolean installed = false;
		Integer installedAppVersionCode = getInstalledAppVersionCode(context, packageName);
		if (installedAppVersionCode != null) {
			if ((minimumVersionCode == null) || (installedAppVersionCode >= minimumVersionCode)) {
				installed = true;
			}
		}
		return installed;
	}

	public static Integer getInstalledAppVersionCode(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		} catch (RuntimeException e) {
			if (e.getMessage().equals("Package manager has died")
					|| e.getMessage().equals("Transaction has failed to Package manger")) {
				AbstractApplication.get().getExceptionHandler().logWarningException(
						"Runtime error while loading package info", e);
				return null;
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * Launch packageName app or open Google Play to download.
	 * 
	 * @param context
	 * @param packageName
	 * @return true if app is installed, false otherwise.
	 */
	public static boolean launchOrDownloadApp(Context context, String packageName) {
		boolean isAppInstalled = isAppInstalled(context, packageName);
		if (isAppInstalled) {
			launchExternalApp(context, packageName);
		} else {
			GooglePlayUtils.launchAppDetails(context, packageName);
		}
		return isAppInstalled;
	}
	
	public static void launchExternalApp(Context context, String packageName) {
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(launchIntent);
	}
	
	public static Boolean startSkypeCall(Context context, String username) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("skype:" + username + "?call"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (IntentUtils.isIntentAvailable(intent)) {
			context.startActivity(intent);
			return true;
		} else {
			LOGGER.info("Skype call intent not supported");
			return false;
		}
	}

	public static void openCustomMapOnBrowser(Context context, String mapId) {
		IntentUtils.startUrl(context, getCustomMapUrl(mapId));
	}

	public static void openCustomMap(Context context, String mapId) {
		boolean isAppInstalled = isAppInstalled(context, GOOGLE_MAPS_PACKAGE_NAME);
		if (isAppInstalled) {
			String mapUrl = getCustomMapUrl(mapId);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setPackage(GOOGLE_MAPS_PACKAGE_NAME);
			intent.setData(Uri.parse(mapUrl));
			if (IntentUtils.isIntentAvailable(intent)) {
				context.startActivity(intent);
			} else {
				openUrl(mapUrl);
			}
		} else {
			GooglePlayUtils.launchAppDetails(context, GOOGLE_MAPS_PACKAGE_NAME);
		}
	}

	private static String getCustomMapUrl(String mapId) {
		return "https://www.google.com/maps/d/viewer?mid=" + mapId;
	}
	
	public static void openUrl(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		AbstractApplication.get().startActivity(intent);
	}
	
	public static Drawable getAppIcon(String packageName) {
		try {
			return AbstractApplication.get().getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static void openAppInfo(Context context) {
		Uri packageURI = Uri.parse("package:" + AppUtils.getApplicationId());
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
		context.startActivity(intent);
	}

	public static void openOnBrowser(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.fromFile(file));
		intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
		if (IntentUtils.isIntentAvailable(intent)) {
			context.startActivity(intent);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.fromFile(file));
			intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			context.startActivity(intent);
		}
	}
	
	public static Boolean openYoutubeVideo(Context context, String videoUrl) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
		if (IntentUtils.isIntentAvailable(intent)) {
			context.startActivity(intent);
			return true;
		} else {
			LOGGER.info("Dial phone intent not supported");
			return false;
		}
	}
	
	public static Boolean dialPhoneNumber(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.fromParts("tel", phoneNumber, null));
		if (IntentUtils.isIntentAvailable(intent)) {
			context.startActivity(intent);
			return true;
		} else {
			LOGGER.info("Dial phone intent not supported");
			return false;
		}
	}
	
	public static Boolean openEmail(Context context, String mailto) {
		return openEmail(context, mailto, null);
	}
	
	public static Boolean openEmail(Context context, String mailto, String subject) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:" + mailto  + (subject != null ? "?subject=" + EncodingUtils.encodeURL(subject) : "")));
		if (IntentUtils.isIntentAvailable(intent)) {
			context.startActivity(intent);
			return true;
		} else {
			LOGGER.info("Open email intent not supported");
			return false;
		}
	}
}

package com.jdroid.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.jdroid.android.activity.ActivityLauncher;
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
	
	public static boolean isAppInstalled(String applicationId) {
		return isAppInstalled(applicationId, null);
	}
	
	public static boolean isAppInstalled(String applicationId, Integer minimumVersionCode) {
		boolean installed = false;
		Integer installedAppVersionCode = getInstalledAppVersionCode(applicationId);
		if (installedAppVersionCode != null) {
			if ((minimumVersionCode == null) || (installedAppVersionCode >= minimumVersionCode)) {
				installed = true;
			}
		}
		return installed;
	}

	public static Integer getInstalledAppVersionCode(String applicationId) {
		PackageManager pm = AbstractApplication.get().getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(applicationId, PackageManager.GET_ACTIVITIES);
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
	 * Launch applicationId app or open Google Play to download.
	 * 
	 * @param applicationId
	 * @return true if app is installed, false otherwise.
	 */
	public static boolean launchOrDownloadApp(String applicationId) {
		boolean isAppInstalled = isAppInstalled(applicationId);
		if (isAppInstalled) {
			launchExternalApp(applicationId);
		} else {
			GooglePlayUtils.launchAppDetails(applicationId);
		}
		return isAppInstalled;
	}
	
	public static void launchExternalApp(String applicationId) {
		Intent launchIntent = AbstractApplication.get().getPackageManager().getLaunchIntentForPackage(applicationId);
		if (launchIntent != null) {
			ActivityLauncher.startActivityNewTask(launchIntent);
		} else {
			LOGGER.info("Could not open launch intent for applicationId: " + applicationId);
		}
	}
	
	public static Boolean startSkypeCall(String username) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("skype:" + username + "?call"));
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
			return true;
		} else {
			LOGGER.info("Skype call intent not supported");
			return false;
		}
	}

	public static void openCustomMapOnBrowser(String mapId) {
		ExternalAppsUtils.openUrl(getCustomMapUrl(mapId));
	}

	public static void openCustomMap(@Nullable Activity activity, String mapId) {
		boolean isAppInstalled = isAppInstalled(GOOGLE_MAPS_PACKAGE_NAME);
		if (isAppInstalled) {
			String mapUrl = getCustomMapUrl(mapId);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setPackage(GOOGLE_MAPS_PACKAGE_NAME);
			intent.setData(Uri.parse(mapUrl));
			if (IntentUtils.isIntentAvailable(intent)) {
				ActivityLauncher.startActivity(activity, intent);
			} else {
				openUrl(mapUrl);
			}
		} else {
			GooglePlayUtils.launchAppDetails(GOOGLE_MAPS_PACKAGE_NAME);
		}
	}

	private static String getCustomMapUrl(String mapId) {
		return "https://www.google.com/maps/d/viewer?mid=" + mapId;
	}
	
	public static void openUrl(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		ActivityLauncher.startActivityNewTask(intent);
	}
	
	public static Drawable getAppIcon(String applicationId) {
		try {
			return AbstractApplication.get().getPackageManager().getApplicationIcon(applicationId);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static void openAppInfo() {
		Uri packageURI = Uri.parse("package:" + AppUtils.getApplicationId());
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
		ActivityLauncher.startActivityNewTask(intent);
	}

	public static void openOnBrowser(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.fromFile(file));
		intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.fromFile(file));
			intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			ActivityLauncher.startActivityNewTask(intent);
		}
	}
	
	public static Boolean openYoutubeVideo(String videoUrl) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
			return true;
		} else {
			LOGGER.info("Youtube video intent not supported");
			return false;
		}
	}
	
	public static Boolean dialPhoneNumber(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.fromParts("tel", phoneNumber, null));
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
			return true;
		} else {
			LOGGER.info("Dial phone intent not supported");
			return false;
		}
	}
	
	public static Boolean openEmail(String mailto) {
		return openEmail(mailto, null);
	}
	
	public static Boolean openEmail(String mailto, String subject) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:" + mailto  + (subject != null ? "?subject=" + EncodingUtils.encodeURL(subject) : "")));
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
			return true;
		} else {
			LOGGER.info("Open email intent not supported");
			return false;
		}
	}
}

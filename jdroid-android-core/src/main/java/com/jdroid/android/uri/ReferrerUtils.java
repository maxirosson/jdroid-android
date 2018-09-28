package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.ActivityCompat;

import com.google.firebase.appindexing.AndroidAppUri;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class ReferrerUtils {

	private static final Logger LOGGER = LoggerUtils.getLogger(ReferrerUtils.class);

	private static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";
	private static final String HTTP_UNDEFINED = "http://undefined";

	public static String getReferrerCategory(Activity activity) {
		String referrerCategory = null;
		Uri referrerUri = null;
		try {
			referrerUri = ActivityCompat.getReferrer(activity);
		} catch (Exception e) {
			LOGGER.error("Error when getting referrer", e);
		}
		if (referrerUri != null && referrerUri.getScheme() != null) {
			if (referrerUri.getScheme().equals("http") || referrerUri.getScheme().equals("https")) {
				referrerCategory = "http://";
				// App was opened from a browser
				String host = referrerUri.getHost();
				if (host.equals("www.google.com")) {
					referrerCategory += "google.com";
				} else {
					referrerCategory += host;
				}

			} else if (referrerUri.getScheme().equals("android-app")) {
				referrerCategory = "android-app://";
				// App was opened from another app
				// Google Search App: com.google.android.googlequicksearchbox
				// Google Crawler App: com.google.appcrawler
				AndroidAppUri appUri = AndroidAppUri.newAndroidAppUri(referrerUri);
				referrerCategory += appUri.getPackageName();
			} else {
				referrerCategory = referrerUri.toString();
			}
		} else {
			referrerCategory = HTTP_UNDEFINED;
		}
		return referrerCategory;
	}

	public static void setReferrer(Intent intent, Uri referrer) {
		if (referrer == null || HTTP_UNDEFINED.equals(referrer.toString())) {
			intent.putExtra(EXTRA_REFERRER, (String)null);
		} else {
			intent.putExtra(EXTRA_REFERRER, referrer);
		}
	}

	public static void setReferrer(Intent intent, String referrer) {
		if (referrer != null) {
			setReferrer(intent, Uri.parse(referrer));
		}
	}

	public static Boolean isUndefined(String referrer) {
		return HTTP_UNDEFINED.equals(referrer);
	}
}

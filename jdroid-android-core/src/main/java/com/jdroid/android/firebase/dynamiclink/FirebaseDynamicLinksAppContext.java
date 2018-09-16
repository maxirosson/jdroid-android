package com.jdroid.android.firebase.dynamiclink;

import com.jdroid.android.context.BuildConfigUtils;

public class FirebaseDynamicLinksAppContext {

	public static Boolean isFirebaseDynamicLinksEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_DYNAMIC_LINKS_ENABLED", true);
	}

	public static String getDynamicLinksDomain() {
		return BuildConfigUtils.getBuildConfigValue("FIREBASE_DYNAMIC_LINKS_DOMAIN");
	}

	public static String getWebApiKey() {
		return BuildConfigUtils.getBuildConfigValue("FIREBASE_WEB_API_KEY");
	}
}

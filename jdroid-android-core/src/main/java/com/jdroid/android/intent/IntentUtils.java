package com.jdroid.android.intent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.jdroid.android.application.AbstractApplication;

import java.util.List;

public class IntentUtils {

	/**
	 * Indicates whether the specified action can be used as an intent. This method queries the package manager for
	 * installed packages that can respond to an intent with the specified action. If no suitable package is found, this
	 * method returns false.
	 *
	 * @param action The Intent action to check for availability.
	 * @return True if an Intent with the specified action can be sent and responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(String action) {
		return isIntentAvailable(new Intent(action));
	}

	/**
	 * Indicates whether the specified intent can be used. This method queries the package manager for installed
	 * packages that can respond to the specified intent. If no suitable package is found, this method returns false.
	 *
	 * @param intent The Intent to check for availability.
	 * @return True if the specified Intent can be sent and responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Intent intent) {
		List<ResolveInfo> list = AbstractApplication.get().getPackageManager().queryIntentActivities(intent,
			PackageManager.MATCH_DEFAULT_ONLY);
		return !list.isEmpty();
	}
}

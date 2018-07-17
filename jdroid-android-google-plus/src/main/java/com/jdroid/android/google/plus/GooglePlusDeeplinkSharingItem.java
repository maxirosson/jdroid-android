package com.jdroid.android.google.plus;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.share.SharingItem;
import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class GooglePlusDeeplinkSharingItem extends SharingItem {
	
	@Override
	public String getApplicationId() {
		return ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME;
	}
	
	@Override
	public void share(Activity activity) {
		GooglePlusHelperFragment googlePlusHelperFragment = GooglePlusHelperFragment.get((FragmentActivity)activity);
		if (googlePlusHelperFragment != null) {
			googlePlusHelperFragment.shareDeeplink(getContent(), getLink());
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("GooglePlusHelperFragment is null when trying to share");
		}
	}
	
	protected abstract String getContent();
	
	protected abstract String getLink();
}

package com.jdroid.android.share;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class SharingItem {

	public abstract String getApplicationId();

	public Integer getMinimumVersionCode() {
		return null;
	}

	public Drawable getAppIcon() {
		return ExternalAppsUtils.getAppIcon(getApplicationId());
	}

	public abstract void share(Activity activity);

	public Boolean isEnabled() {
		String applicationId = getApplicationId();
		return (applicationId != null)
			&& ExternalAppsUtils.isAppInstalled(applicationId, getMinimumVersionCode());
	}

}

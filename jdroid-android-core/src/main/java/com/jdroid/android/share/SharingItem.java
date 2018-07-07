package com.jdroid.android.share;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class SharingItem {
	
	public abstract String getPackageName();
	
	public Integer getMinimumVersionCode() {
		return null;
	}
	
	public Drawable getAppIcon() {
		return ExternalAppsUtils.getAppIcon(getPackageName());
	}
	
	public abstract void share(Activity activity);
	
	public Boolean isEnabled() {
		String packageName = getPackageName();
		return (packageName != null)
				&& ExternalAppsUtils.isAppInstalled(packageName, getMinimumVersionCode());
	}
	
}

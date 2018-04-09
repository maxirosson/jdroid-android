package com.jdroid.android.share;

import android.graphics.drawable.Drawable;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class SharingItem {
	
	public abstract String getPackageName();
	
	public Integer getMinimumVersionCode() {
		return null;
	}
	
	public Drawable getAppIcon() {
		return ExternalAppsUtils.getAppIcon(getPackageName());
	}
	
	public abstract void share();
	
	public Boolean isEnabled() {
		String packageName = getPackageName();
		return (packageName != null)
				&& ExternalAppsUtils.isAppInstalled(AbstractApplication.get(), packageName, getMinimumVersionCode());
	}
	
}

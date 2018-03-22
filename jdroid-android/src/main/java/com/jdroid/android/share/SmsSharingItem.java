package com.jdroid.android.share;

import android.support.annotation.NonNull;

import com.jdroid.android.utils.ExternalAppsUtils;

public class SmsSharingItem extends AppSharingItem {
	
	public SmsSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.SMS;
	}
	
	@Override
	public Boolean isEnabled() {
		return super.isEnabled() && !getPackageName().equals(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME);
	}
}

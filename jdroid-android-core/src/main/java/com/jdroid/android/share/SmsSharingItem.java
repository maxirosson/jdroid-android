package com.jdroid.android.share;

import androidx.annotation.NonNull;

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
		return super.isEnabled() && !getApplicationId().equals(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME);
	}
}

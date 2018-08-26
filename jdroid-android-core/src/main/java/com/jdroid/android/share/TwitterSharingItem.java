package com.jdroid.android.share;

import android.support.annotation.NonNull;

public class TwitterSharingItem extends AppSharingItem {

	public TwitterSharingItem(SharingData sharingData) {
		super(sharingData);
	}

	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.TWITTER;
	}

	@Override
	public Integer getMinimumVersionCode() {
		return 420;
	}
}

package com.jdroid.android.share;

import android.support.annotation.NonNull;

public class GooglePlusSharingItem extends AppSharingItem {

	public GooglePlusSharingItem(SharingData sharingData) {
		super(sharingData);
	}

	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.GOOGLE_PLUS;
	}

	@Override
	public Integer getMinimumVersionCode() {
		return 416583705;
	}
}

package com.jdroid.android.share;

import androidx.annotation.NonNull;

public class HangoutsSharingItem extends AppSharingItem {

	public HangoutsSharingItem(SharingData sharingData) {
		super(sharingData);
	}

	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.HANGOUTS;
	}

	@Override
	public Integer getMinimumVersionCode() {
		return 23370799;
	}
}

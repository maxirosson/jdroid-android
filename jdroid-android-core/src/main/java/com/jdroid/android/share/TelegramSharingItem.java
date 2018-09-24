package com.jdroid.android.share;

import androidx.annotation.NonNull;

public class TelegramSharingItem extends AppSharingItem {

	public TelegramSharingItem(SharingData sharingData) {
		super(sharingData);
	}

	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.TELEGRAM;
	}
}

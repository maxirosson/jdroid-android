package com.jdroid.android.share;

import android.support.annotation.NonNull;

public class WhatsAppSharingItem extends AppSharingItem {
	
	public WhatsAppSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@NonNull
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.WHATSAPP;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 49000;
	}
}

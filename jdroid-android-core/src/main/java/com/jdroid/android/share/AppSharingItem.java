package com.jdroid.android.share;

import android.support.annotation.NonNull;

public abstract class AppSharingItem extends SharingItem {
	
	private SharingData sharingData;
	
	public AppSharingItem(@NonNull SharingData sharingData) {
		this.sharingData = sharingData;
	}
	
	@Override
	public void share() {
		SharingDataItem sharingDataItem = sharingData.getShareInfoItemMap().get(getSharingMedium().getName());
		if (sharingDataItem == null) {
			sharingDataItem = sharingData.getDefaultSharingDataItem();
		}
		
		String text = sharingDataItem.getText() != null ? sharingDataItem.getText() : sharingData.getDefaultSharingDataItem().getText();
		String link = sharingDataItem.getLink() != null ? sharingDataItem.getLink() : sharingData.getDefaultSharingDataItem().getLink();
		if (link != null) {
			text = text.replace("${link}", link);
		}
		ShareUtils.share(getSharingMedium(), sharingData.getShareKey(), text);
	}
	
	@NonNull
	public abstract SharingMedium getSharingMedium();
	
	@Override
	public String getPackageName() {
		return getSharingMedium().getPackageName();
	}
	
}

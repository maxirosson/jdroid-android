package com.jdroid.android.share;

import java.util.Map;

public class SharingData {
	
	private String shareKey;
	private Map<String, SharingDataItem> shareInfoItemMap;
	private SharingDataItem defaultSharingDataItem;
	
	public SharingData(String shareKey, Map<String, SharingDataItem> shareInfoItemMap, SharingDataItem defaultSharingDataItem) {
		this.shareKey = shareKey;
		this.shareInfoItemMap = shareInfoItemMap;
		this.defaultSharingDataItem = defaultSharingDataItem;
	}
	
	public Map<String, SharingDataItem> getShareInfoItemMap() {
		return shareInfoItemMap;
	}
	
	public void setShareInfoItemMap(Map<String, SharingDataItem> shareInfoItemMap) {
		this.shareInfoItemMap = shareInfoItemMap;
	}
	
	public String getShareKey() {
		return shareKey;
	}
	
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}
	
	public SharingDataItem getDefaultSharingDataItem() {
		return defaultSharingDataItem;
	}
	
	public void setDefaultSharingDataItem(SharingDataItem defaultSharingDataItem) {
		this.defaultSharingDataItem = defaultSharingDataItem;
	}
}

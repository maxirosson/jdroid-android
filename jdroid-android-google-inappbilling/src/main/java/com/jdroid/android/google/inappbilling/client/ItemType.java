package com.jdroid.android.google.inappbilling.client;

import com.android.billingclient.api.BillingClient;

public enum ItemType {
	
	MANAGED("inapp"),
	SUBSCRIPTION("subs");
	
	private String type;
	
	ItemType(String type) {
		this.type = type;
	}
	
	@BillingClient.SkuType
	public String getType() {
		return type;
	}
}
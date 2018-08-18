package com.jdroid.android.google.inappbilling.client;

public enum ItemType {
	MANAGED("inapp"),
	SUBSCRIPTION("subs");

	private String type;

	ItemType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
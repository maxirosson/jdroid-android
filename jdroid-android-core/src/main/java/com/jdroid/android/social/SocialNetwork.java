package com.jdroid.android.social;

public enum SocialNetwork {

	GOOGLE_PLUS("googlePlus"),
	FACEBOOK("facebook"),
	TWITTER("twitter");

	private String name;

	SocialNetwork(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

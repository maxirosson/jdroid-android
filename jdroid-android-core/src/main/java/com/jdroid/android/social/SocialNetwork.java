package com.jdroid.android.social;

import com.jdroid.android.R;

public enum SocialNetwork {
	
	GOOGLE_PLUS("googlePlus", R.drawable.jdroid_ic_google_plus),
	FACEBOOK("facebook", R.drawable.jdroid_ic_facebook_24dp),
	TWITTER("twitter", R.drawable.jdroid_ic_twitter_24dp);
	
	private String name;
	private Integer iconResId;
	
	SocialNetwork(String name, Integer iconResId) {
		this.name = name;
		this.iconResId = iconResId;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getIconResId() {
		return iconResId;
	}
}

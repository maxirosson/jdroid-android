package com.jdroid.android.social;

public enum SocialAction {

	LIKE("like"),
	SHARE("share"),
	OPEN_PROFILE("openProfile");

	private String name;

	SocialAction(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}

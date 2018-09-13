package com.jdroid.android.social;

public enum SocialAction {

	LIKE("like"),
	SHARE("share"),
	OPEN_PROFILE("openProfile"),
	PLUS_ONE("+1"),
	PLUS_ONE_UNDO("-1");

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

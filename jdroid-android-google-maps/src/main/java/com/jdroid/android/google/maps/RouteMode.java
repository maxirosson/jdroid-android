package com.jdroid.android.google.maps;

import com.jdroid.android.dialog.ChoiceItem;

public enum RouteMode implements ChoiceItem {

	WALKING(R.color.jdroid_walkingColor, R.string.jdroid_walking, "walking"),
	DRIVING(R.color.jdroid_drivingColor, R.string.jdroid_driving, "driving");

	private int colorId;
	private int resourceTitleId;
	private String argName;

	RouteMode(int colorId, int resourceTitleId, String argName) {
		this.colorId = colorId;
		this.resourceTitleId = resourceTitleId;
		this.argName = argName;
	}

	/**
	 * @return the colorId
	 */
	public int getColorId() {
		return colorId;
	}

	/**
	 * @return the argName
	 */
	public String getArgName() {
		return argName;
	}

	/**
	 * @see com.jdroid.android.dialog.ChoiceItem#getResourceTitleId()
	 */
	@Override
	public int getResourceTitleId() {
		return resourceTitleId;
	}
}
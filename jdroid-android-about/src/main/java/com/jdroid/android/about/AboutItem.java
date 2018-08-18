package com.jdroid.android.about;

import android.app.Activity;

public abstract class AboutItem {

	private Integer iconResId;
	private Integer nameResId;

	public AboutItem(Integer iconResId, Integer nameResId) {
		this.iconResId = iconResId;
		this.nameResId = nameResId;
	}

	public Integer getIconResId() {
		return iconResId;
	}

	public Integer getNameResId() {
		return nameResId;
	}

	public abstract void onSelected(Activity activity);

}

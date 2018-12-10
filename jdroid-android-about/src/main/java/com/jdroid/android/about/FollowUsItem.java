package com.jdroid.android.about;

import android.app.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public abstract class FollowUsItem {

	@NonNull
	@DrawableRes
	private Integer imageResId;

	@NonNull
	@StringRes
	private Integer titleResId;

	public FollowUsItem(@NonNull Integer imageResId, @NonNull Integer titleResId) {
		this.imageResId = imageResId;
		this.titleResId = titleResId;
	}

	public abstract void onSelected(Activity activity);

	@NonNull
	public Integer getTitleResId() {
		return titleResId;
	}

	@NonNull
	public Integer getImageResId() {
		return imageResId;
	}
}

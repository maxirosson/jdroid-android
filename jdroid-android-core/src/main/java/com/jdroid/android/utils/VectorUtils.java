package com.jdroid.android.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.jdroid.android.application.AbstractApplication;

public class VectorUtils {

	@SuppressLint("NewApi")
	public static Drawable getTintedVectorDrawable(@DrawableRes int resDrawable, @ColorRes int resColor) {
		Resources resources = AbstractApplication.get().getResources();
		VectorDrawableCompat drawable = VectorDrawableCompat.create(resources, resDrawable, null);
		drawable.setTint(resources.getColor(resColor));
		return drawable;
	}
}

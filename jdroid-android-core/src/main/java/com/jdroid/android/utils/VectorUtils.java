package com.jdroid.android.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.jdroid.android.application.AbstractApplication;

public class VectorUtils {

	public static Drawable getTintedVectorDrawable(@DrawableRes int resDrawable, @ColorRes int resColor) {
		Resources resources = AbstractApplication.get().getResources();
		VectorDrawableCompat drawable = VectorDrawableCompat.create(resources, resDrawable, null);
		drawable.setTint(resources.getColor(resColor));
		return drawable;
	}
}

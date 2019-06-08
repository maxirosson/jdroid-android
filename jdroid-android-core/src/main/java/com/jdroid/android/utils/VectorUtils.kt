package com.jdroid.android.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.jdroid.android.application.AbstractApplication

object VectorUtils {

    fun getTintedVectorDrawable(@DrawableRes resDrawable: Int, @ColorRes resColor: Int): Drawable {
        val resources = AbstractApplication.get().resources
        val drawable = VectorDrawableCompat.create(resources, resDrawable, null)
        drawable!!.setTint(resources.getColor(resColor))
        return drawable
    }
}

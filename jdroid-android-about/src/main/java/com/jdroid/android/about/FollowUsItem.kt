package com.jdroid.android.about

import android.app.Activity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

abstract class FollowUsItem(
    @DrawableRes
    val imageResId: Int,
    @StringRes
    val titleResId: Int
) {

    abstract fun onSelected(activity: Activity)
}

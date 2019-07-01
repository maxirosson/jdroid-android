package com.jdroid.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build

object MultiWindowUtils {

    @SuppressLint("NewApi")
    fun isInMultiWindowMode(activity: Activity): Boolean {
        return AndroidUtils.apiLevel >= Build.VERSION_CODES.N && activity.isInMultiWindowMode
    }

    @SuppressLint("NewApi")
    fun isInPictureInPictureMode(activity: Activity): Boolean {
        return AndroidUtils.apiLevel >= Build.VERSION_CODES.N && activity.isInPictureInPictureMode
    }
}

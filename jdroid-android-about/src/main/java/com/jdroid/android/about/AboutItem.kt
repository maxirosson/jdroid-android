package com.jdroid.android.about

import android.app.Activity

abstract class AboutItem(val iconResId: Int, val nameResId: Int) {

    abstract fun onSelected(activity: Activity)
}

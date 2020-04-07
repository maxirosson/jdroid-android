package com.jdroid.android.images.loader

import android.widget.ImageView

interface ImageViewLoader {

    fun displayImage(url: String, imageView: ImageView, defaultImage: Int?, timeToLive: Long?)
}

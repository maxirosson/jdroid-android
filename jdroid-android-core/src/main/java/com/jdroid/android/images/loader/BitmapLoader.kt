package com.jdroid.android.images.loader

import android.graphics.Bitmap
import androidx.annotation.WorkerThread

interface BitmapLoader {

    @WorkerThread
    fun load(height: Int, width: Int): Bitmap
}

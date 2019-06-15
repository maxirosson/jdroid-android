package com.jdroid.android.images

import android.graphics.Bitmap
import androidx.collection.LruCache

class BitmapLruCache(maxSizeBytes: Int) : LruCache<String, Bitmap>(maxSizeBytes) {

    override fun sizeOf(key: String, value: Bitmap): Int {
        return value.rowBytes * value.height
    }
}

package com.jdroid.android.glide

import android.graphics.Bitmap

import com.bumptech.glide.request.RequestOptions
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.images.loader.BitmapLoader

import java.util.concurrent.ExecutionException

class GlideBitmapLoader(private val url: String) : BitmapLoader {

    override fun load(height: Int, width: Int): Bitmap {
        var options = RequestOptions()
        options = options.override(width, height)
        try {
            return GlideHelper.with(AbstractApplication.get()).asBitmap().load(url).apply(options).submit().get()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            throw RuntimeException(e)
        }
    }
}

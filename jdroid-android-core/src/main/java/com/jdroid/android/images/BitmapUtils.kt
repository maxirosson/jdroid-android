package com.jdroid.android.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.net.Uri
import androidx.annotation.DrawableRes
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

object BitmapUtils {

    private val LOGGER = LoggerUtils.getLogger(BitmapUtils::class.java)

    fun toBitmap(@DrawableRes resId: Int): Bitmap {
        return BitmapFactory.decodeResource(AbstractApplication.get().resources, resId)
    }

    fun toBitmap(@DrawableRes resId: Int, maxWidth: Int, maxHeight: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(AbstractApplication.get().resources, resId)
        val scaled = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
        bitmap.recycle()
        return scaled
    }

    fun toBitmap(input: InputStream, maxWidth: Int?, maxHeight: Int?): Bitmap? {

        val options = Options()
        if (maxWidth != null && maxHeight != null) {
            // Set the scaling options.
            val scale = Math.min(maxWidth.toFloat() / options.outWidth, maxHeight.toFloat() / options.outHeight)
            options.inSampleSize = Math.round(1 / scale)
        }
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(input, null, options)
    }

    /**
     * Gets a [Bitmap] from a [Uri]. Resizes the image to a determined width and height.
     *
     * @param uri The [Uri] from which the image is obtained.
     * @param maxWidth The maximum width of the image used to scale it. If null, the image won't be scaled
     * @param maxHeight The maximum height of the image used to scale it. If null, the image won't be scaled
     * @return [Bitmap] The resized image.
     */
    fun toBitmap(uri: Uri, maxWidth: Int?, maxHeight: Int?): Bitmap? {
        try {
            val context = AbstractApplication.get()

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = Options()
            options.inJustDecodeBounds = true
            var openInputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(openInputStream, null, options)
            openInputStream?.close()

            // Calculate inSampleSize
            if (maxWidth != null && maxHeight != null) {
                val scale = Math.min(maxWidth.toFloat() / options.outWidth, maxHeight.toFloat() / options.outHeight)
                options.inSampleSize = Math.round(1 / scale)
            }

            // Decode bitmap with inSampleSize set
            openInputStream = context.contentResolver.openInputStream(uri)
            options.inJustDecodeBounds = false
            val result = BitmapFactory.decodeStream(openInputStream, null, options)
            openInputStream?.close()
            return result
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            return null
        }
    }

    fun toPNGInputStream(uri: Uri, maxWidth: Int?, maxHeight: Int?): ByteArrayInputStream {
        val bitmap = toBitmap(uri, maxWidth, maxHeight)
        return toPNGInputStream(bitmap!!)
    }

    /**
     * Compress the bitmap to a PNG and return its [ByteArrayInputStream]
     *
     * @param bitmap The [Bitmap] to compress
     * @return The [ByteArrayInputStream]
     */
    fun toPNGInputStream(bitmap: Bitmap): ByteArrayInputStream {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        return ByteArrayInputStream(bytes.toByteArray())
    }
}

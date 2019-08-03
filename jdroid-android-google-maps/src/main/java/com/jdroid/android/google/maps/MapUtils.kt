package com.jdroid.android.google.maps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.domain.GeoLocation

object MapUtils {

    fun safeVectorToBitmap(@DrawableRes drawableRes: Int, @ColorRes colorRes: Int): BitmapDescriptor? {
        try {
            return vectorToBitmap(drawableRes, colorRes)
        } catch (e: Exception) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
        return null
    }

    /**
     * Convert a [Drawable] to a [BitmapDescriptor], for use as a marker icon.
     */
    fun vectorToBitmap(@DrawableRes drawableRes: Int, @ColorRes colorRes: Int): BitmapDescriptor {
        val vectorDrawable = VectorDrawableCompat.create(AbstractApplication.get().resources, drawableRes, null)!!
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, AbstractApplication.get().resources.getColor(colorRes))
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun createLatLng(geoLocation: GeoLocation?): LatLng? {
        return if (geoLocation != null) LatLng(geoLocation.requireLatitude(), geoLocation.requireLongitude()) else null
    }
}

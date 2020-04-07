package com.jdroid.android.utils

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.StringUtils
import kotlin.math.max

object ScreenUtils {

    const val LDPI_DENSITY_NAME = "ldpi"
    const val MDPI_DENSITY_NAME = "mdpi"
    const val HDPI_DENSITY_NAME = "hdpi"
    const val XHDPI_DENSITY_NAME = "xhdpi"
    const val XXHDPI_DENSITY_NAME = "xxhdpi"
    const val XXXHDPI_DENSITY_NAME = "xxxhdpi"

    /**
     * Gets the [WindowManager] from the context.
     *
     * @return [WindowManager] The window manager.
     */
    fun getWindowManager(): WindowManager {
        return AbstractApplication.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun getWindowHeight(): Int {
        val display = getWindowManager().defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    fun getWindowWidth(): Int {
        val display = getWindowManager().defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun isLessThan7Inches(): Boolean {
        return AbstractApplication.get().resources.getBoolean(R.bool.jdroid_isLessThan7Inches)
    }

    fun isBetween7And10Inches(): Boolean {
        return AbstractApplication.get().resources.getBoolean(R.bool.jdroid_isBetween7And10Inches)
    }

    fun is10Inches(): Boolean {
        return AbstractApplication.get().resources.getBoolean(R.bool.jdroid_is10Inches)
    }

    fun is10InchesLand(): Boolean {
        val resources = AbstractApplication.get().resources
        return resources.getBoolean(R.bool.jdroid_is10Inches) && resources.getBoolean(R.bool.jdroid_isOrientationLandscape)
    }

    fun is7InchesOrLarger(): Boolean {
        return isBetween7And10Inches() || is10Inches()
    }

    fun getSmallestScreenWidthDp(): Int {
        return AbstractApplication.get().resources.configuration.smallestScreenWidthDp
    }

    fun getScreenWidthDp(): Int {
        return AbstractApplication.get().resources.configuration.screenWidthDp
    }

    fun getScreenHeightDp(): Int {
        return AbstractApplication.get().resources.configuration.screenHeightDp
    }

    fun getDensityDpi(): Int {
        val metrics = DisplayMetrics()
        getWindowManager().defaultDisplay.getMetrics(metrics)
        return metrics.densityDpi
    }

    fun isLdpiDensity(): Boolean {
        return getDensityDpi() <= DisplayMetrics.DENSITY_LOW
    }

    fun isMdpiDensity(): Boolean {
        val densityDpi = getDensityDpi()
        return densityDpi > DisplayMetrics.DENSITY_LOW && densityDpi <= DisplayMetrics.DENSITY_MEDIUM
    }

    fun isHdpiDensity(): Boolean {
        val densityDpi = getDensityDpi()
        return densityDpi > DisplayMetrics.DENSITY_MEDIUM && densityDpi <= DisplayMetrics.DENSITY_HIGH
    }

    fun isXhdpiDensity(): Boolean {
        val densityDpi = getDensityDpi()
        return densityDpi > DisplayMetrics.DENSITY_HIGH && densityDpi <= DisplayMetrics.DENSITY_XHIGH
    }

    fun isXXhdpiDensity(): Boolean {
        val densityDpi = getDensityDpi()
        return densityDpi > DisplayMetrics.DENSITY_XHIGH && densityDpi <= DisplayMetrics.DENSITY_XXHIGH
    }

    fun isXXXhdpiDensity(): Boolean {
        val densityDpi = getDensityDpi()
        return densityDpi > DisplayMetrics.DENSITY_XXHIGH && densityDpi <= DisplayMetrics.DENSITY_XXXHIGH
    }

    fun getScreenDensity(): String {
        var density = StringUtils.EMPTY
        if (isLdpiDensity()) {
            density = LDPI_DENSITY_NAME
        } else if (isMdpiDensity()) {
            density = MDPI_DENSITY_NAME
        } else if (isHdpiDensity()) {
            density = HDPI_DENSITY_NAME
        } else if (isXhdpiDensity()) {
            density = XHDPI_DENSITY_NAME
        } else if (isXXhdpiDensity()) {
            density = XXHDPI_DENSITY_NAME
        } else if (isXXXhdpiDensity()) {
            density = XXXHDPI_DENSITY_NAME
        }
        return density
    }

    fun getLargestScreenWidthPx(): Int {
        val display = getWindowManager().defaultDisplay
        val size = Point()
        display.getSize(size)
        return max(size.x, size.y)
    }

    fun getScreenWidthPx(): Int {
        val display = getWindowManager().defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return An int value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi / 160f)).toInt()
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return An int value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Int, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi / 160f)).toInt()
    }

    fun convertSpDimenToSp(spDimenResId: Int, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return context.resources.getDimensionPixelSize(spDimenResId) / metrics.scaledDensity
    }

    fun convertDimenToPixel(dimenResId: Int): Int {
        return AbstractApplication.get().resources.getDimension(dimenResId).toInt()
    }
}

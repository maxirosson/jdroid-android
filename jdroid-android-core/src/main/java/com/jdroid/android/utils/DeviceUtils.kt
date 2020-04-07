package com.jdroid.android.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.telephony.TelephonyManager

import com.facebook.device.yearclass.YearClass
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.intent.IntentUtils
import com.jdroid.java.utils.FileUtils
import com.jdroid.java.utils.StringUtils

object DeviceUtils {

    fun getDeviceYearClass(): Int {
        return YearClass.get(AbstractApplication.get())
    }

    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    fun getDeviceManufacturer(): String? {
        return Build.MANUFACTURER
    }

    fun getDeviceName(): String {
        val manufacturer = getDeviceManufacturer()
        val model = getDeviceModel()
        return if (model != null && model.startsWith(manufacturer!!)) {
            StringUtils.capitalize(model)
        } else if (manufacturer != null) {
            StringUtils.capitalize(manufacturer) + " " + model
        } else {
            "Unknown"
        }
    }

    fun hasCamera(): Boolean {
        return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE)
    }

    fun getDeviceType(): String {
        return when {
            ScreenUtils.is10Inches() -> "10\" tablet"
            ScreenUtils.isBetween7And10Inches() -> "7\" tablet"
            else -> "phone"
        }
    }

    fun isEmulator(): Boolean {
        return "google_sdk" == Build.PRODUCT
    }

    fun getNetworkOperatorName(): String {
        val manager = AbstractApplication.get().getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        return manager.networkOperatorName
    }

    fun getSimOperatorName(): String {
        val manager = AbstractApplication.get().getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager
        return manager.simOperatorName
    }

    /**
     * @return The HEAP size in MegaBytes
     */
    fun getHeapSize(): Int {
        return (AbstractApplication.get().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
    }

    /**
     * @return The available storage in MegaBytes
     */
    fun getAvailableInternalDataSize(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        val size = stat.availableBlocks.toLong() * stat.blockSize.toLong()
        return size / FileUtils.BYTES_TO_MB
    }

    /**
     * @return The total storage in MegaBytes
     */
    fun getTotalInternalDataSize(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        val size = stat.blockCount.toLong() * stat.blockSize.toLong()
        return size / FileUtils.BYTES_TO_MB
    }

    fun isMediaMounted(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}

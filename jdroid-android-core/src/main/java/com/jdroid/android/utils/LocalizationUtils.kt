package com.jdroid.android.utils

import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.exception.ErrorCode

object LocalizationUtils {

    fun getRequiredString(resId: Int, vararg args: Any): String {
        return AbstractApplication.get().getString(resId, *args)
    }

    /**
     * Returns a formatted string, using the localized resource as format and the supplied arguments
     *
     * @param resId The resource id to obtain the format
     * @param args arguments to replace format specifiers
     * @return The localized and formatted string
     */
    fun getString(resId: Int?, vararg args: Any): String? {
        return if (resId != null) {
            AbstractApplication.get().getString(resId, *args)
        } else {
            null
        }
    }

    fun getTitle(errorCode: ErrorCode?): String? {
        return getString(errorCode?.getTitleResId())
    }

    fun getTitle(errorCode: ErrorCode?, vararg args: Any): String? {
        return getString(errorCode?.getTitleResId(), *args)
    }

    fun getDescription(errorCode: ErrorCode?): String? {
        return getString(errorCode?.getDescriptionResId())
    }

    fun getDescription(errorCode: ErrorCode?, vararg args: Any): String? {
        return getString(errorCode?.getDescriptionResId(), *args)
    }
}

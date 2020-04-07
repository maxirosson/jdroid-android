package com.jdroid.android.google

import android.app.Activity
import android.app.Dialog
import android.content.Context
import androidx.annotation.WorkerThread
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils

object GooglePlayServicesUtils {

    private val LOGGER = LoggerUtils.getLogger(GooglePlayServicesUtils::class.java)

    private const val GOOGLE_PLAY_SERVICES = "com.google.android.gms"
    private const val GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST = 1

    @WorkerThread
    fun getAdvertisingId(): String? {
        return try {
            AdvertisingIdClient.getAdvertisingIdInfo(AbstractApplication.get()).id
        } catch (e: Exception) {
            LOGGER.warn("Error getting the advertising id " + e.message)
            null
        }
    }

    fun verifyGooglePlayServices(activity: Activity): GooglePlayServicesResponse {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(AbstractApplication.get())
        LOGGER.info("Google Play Services result code: $resultCode")
        return if (resultCode == ConnectionResult.SUCCESS) {
            GooglePlayServicesResponse()
        } else {
            var dialog: Dialog? = null
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                dialog = googleApiAvailability.getErrorDialog(activity, resultCode, GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST)
                dialog?.show()
            }
            GooglePlayServicesResponse(dialog, resultCode)
        }
    }

    fun checkGooglePlayServices(activity: Activity) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        LOGGER.info("Google Play Services result code: $resultCode")
        if (resultCode != ConnectionResult.SUCCESS && googleApiAvailability.isUserResolvableError(resultCode)) {
            googleApiAvailability.showErrorDialogFragment(activity, resultCode, GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST)
        }
    }

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        LOGGER.info("Google Play Services result code: $resultCode")
        return resultCode == ConnectionResult.SUCCESS
    }

    fun launchGooglePlayServicesUpdate() {
        GooglePlayUtils.launchAppDetails(GOOGLE_PLAY_SERVICES)
    }

    class GooglePlayServicesResponse {

        var isAvailable: Boolean
        var dialog: Dialog? = null
        var resultCode: Int? = null

        constructor() {
            isAvailable = true
        }

        constructor(dialog: Dialog?, resultCode: Int) {
            isAvailable = false
            this.dialog = dialog
            this.resultCode = resultCode
        }
    }
}

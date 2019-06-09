package com.jdroid.android.intent

import android.content.Intent
import android.content.pm.PackageManager
import com.jdroid.android.application.AbstractApplication

object IntentUtils {

    /**
     * Indicates whether the specified action can be used as an intent. This method queries the package manager for
     * installed packages that can respond to an intent with the specified action. If no suitable package is found, this
     * method returns false.
     *
     * @param action The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and responded to, false otherwise.
     */
    fun isIntentAvailable(action: String): Boolean {
        return isIntentAvailable(Intent(action))
    }

    /**
     * Indicates whether the specified intent can be used. This method queries the package manager for installed
     * packages that can respond to the specified intent. If no suitable package is found, this method returns false.
     *
     * @param intent The Intent to check for availability.
     * @return True if the specified Intent can be sent and responded to, false otherwise.
     */
    fun isIntentAvailable(intent: Intent): Boolean {
        return AbstractApplication.get().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }
}

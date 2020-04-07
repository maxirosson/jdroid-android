package com.jdroid.android.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.google.GooglePlayUtils
import com.jdroid.android.intent.IntentUtils
import com.jdroid.java.utils.EncodingUtils
import com.jdroid.java.utils.LoggerUtils
import java.io.File

object ExternalAppsUtils {

    private val LOGGER = LoggerUtils.getLogger(ExternalAppsUtils::class.java)

    const val TWITTER_PACKAGE_NAME = "com.twitter.android"
    const val FACEBOOK_PACKAGE_NAME = "com.facebook.katana"
    const val WHATSAPP_PACKAGE_NAME = "com.whatsapp"
    const val TELEGRAM_PACKAGE_NAME = "org.telegram.messenger"
    const val HANGOUTS_PACKAGE_NAME = "com.google.android.talk"
    const val GOOGLE_MAPS_PACKAGE_NAME = "com.google.android.apps.maps"

    fun isAppInstalled(applicationId: String, minimumVersionCode: Int? = null): Boolean {
        var installed = false
        val installedAppVersionCode = getInstalledAppVersionCode(applicationId)
        if (installedAppVersionCode != null) {
            if (minimumVersionCode == null || installedAppVersionCode >= minimumVersionCode) {
                installed = true
            }
        }
        return installed
    }

    fun getInstalledAppVersionCode(applicationId: String): Int? {
        val pm = AbstractApplication.get().packageManager
        try {
            val packageInfo = pm.getPackageInfo(applicationId, PackageManager.GET_ACTIVITIES)
            return packageInfo.versionCode
        } catch (e: NameNotFoundException) {
            return null
        } catch (e: RuntimeException) {
            if (e.message == "Package manager has died" || e.message == "Transaction has failed to Package manger") {
                AbstractApplication.get().exceptionHandler.logWarningException("Runtime error while loading package info", e)
                return null
            } else {
                throw e
            }
        }
    }

    /**
     * Launch applicationId app or open Google Play to download.
     *
     * @param applicationId
     * @return true if app is installed, false otherwise.
     */
    fun launchOrDownloadApp(applicationId: String): Boolean {
        val isAppInstalled = isAppInstalled(applicationId)
        if (isAppInstalled) {
            launchExternalApp(applicationId)
        } else {
            GooglePlayUtils.launchAppDetails(applicationId)
        }
        return isAppInstalled
    }

    fun launchExternalApp(applicationId: String) {
        val launchIntent = AbstractApplication.get().packageManager.getLaunchIntentForPackage(applicationId)
        if (launchIntent != null) {
            ActivityLauncher.startActivityNewTask(launchIntent)
        } else {
            LOGGER.info("Could not open launch intent for applicationId: $applicationId")
        }
    }

    fun startSkypeCall(username: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("skype:$username?call")
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
            return true
        } else {
            LOGGER.info("Skype call intent not supported")
            return false
        }
    }

    fun openCustomMapOnBrowser(mapId: String) {
        openUrl(getCustomMapUrl(mapId))
    }

    fun openCustomMap(activity: Activity, mapId: String) {
        val isAppInstalled = isAppInstalled(GOOGLE_MAPS_PACKAGE_NAME)
        if (isAppInstalled) {
            val mapUrl = getCustomMapUrl(mapId)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage(GOOGLE_MAPS_PACKAGE_NAME)
            intent.data = Uri.parse(mapUrl)
            if (IntentUtils.isIntentAvailable(intent)) {
                ActivityLauncher.startActivity(activity, intent)
            } else {
                openUrl(mapUrl)
            }
        } else {
            GooglePlayUtils.launchAppDetails(GOOGLE_MAPS_PACKAGE_NAME)
        }
    }

    private fun getCustomMapUrl(mapId: String): String {
        return "https://www.google.com/maps/d/viewer?mid=$mapId"
    }

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        ActivityLauncher.startActivityNewTask(intent)
    }

    fun getAppIcon(applicationId: String): Drawable? {
        try {
            return AbstractApplication.get().packageManager.getApplicationIcon(applicationId)
        } catch (e: NameNotFoundException) {
            return null
        }
    }

    fun openAppInfo() {
        val packageURI = Uri.parse("package:" + AppUtils.getApplicationId())
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
        ActivityLauncher.startActivityNewTask(intent)
    }

    fun openOnBrowser(file: File) {
        var intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.fromFile(file)
        intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main")
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.fromFile(file)
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
            ActivityLauncher.startActivityNewTask(intent)
        }
    }

    fun openYoutubeVideo(videoUrl: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
            return true
        } else {
            LOGGER.info("Youtube video intent not supported")
            return false
        }
    }

    fun dialPhoneNumber(phoneNumber: String): Boolean {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.fromParts("tel", phoneNumber, null)
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
            return true
        } else {
            LOGGER.info("Dial phone intent not supported")
            return false
        }
    }

    @JvmOverloads
    fun openEmail(mailto: String, subject: String? = null): Boolean {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data =
            Uri.parse("mailto:" + mailto + if (subject != null) "?subject=" + EncodingUtils.encodeURL(subject) else "")
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
            return true
        } else {
            LOGGER.info("Open email intent not supported")
            return false
        }
    }
}

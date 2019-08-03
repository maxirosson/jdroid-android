package com.jdroid.android.google

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.dialog.AlertDialogFragment
import com.jdroid.android.intent.IntentUtils
import com.jdroid.android.kotlin.getRequiredStringArgument
import com.jdroid.android.utils.AppUtils

object GooglePlayUtils {

    private const val GOOGLE_PLAY_DETAILS_LINK = "http://play.google.com/store/apps/details?id="

    class UpdateAppDialogFragment : AlertDialogFragment() {

        override fun onPositiveClick() {
            launchAppDetails(AppUtils.getReleaseApplicationId())
            requireActivity().finish()
        }
    }

    class DownloadAppDialogFragment : AlertDialogFragment() {

        override fun onPositiveClick() {
            launchAppDetails(getRequiredStringArgument(APPLICATION_ID))
        }

        fun setApplicationId(applicationId: String) {
            addParameter(APPLICATION_ID, applicationId)
        }

        companion object {
            private const val APPLICATION_ID = "APPLICATION_ID"
        }
    }

    fun showUpdateDialog(fragmentActivity: FragmentActivity?) {
        if (fragmentActivity != null) {
            val title = fragmentActivity.getString(com.jdroid.android.R.string.jdroid_updateAppTitle)
            val message = fragmentActivity.getString(com.jdroid.android.R.string.jdroid_updateAppMessage)
            AlertDialogFragment.show(
                fragmentActivity, UpdateAppDialogFragment(), title, message, null, null,
                fragmentActivity.getString(com.jdroid.android.R.string.jdroid_ok), false
            )
        }
    }

    fun showDownloadDialog(fragmentActivity: FragmentActivity?, appNameResId: Int, applicationId: String) {
        if (fragmentActivity != null) {
            val appName = fragmentActivity.getString(appNameResId)
            val title = fragmentActivity.getString(com.jdroid.android.R.string.jdroid_installAppTitle, appName)
            val message = fragmentActivity.getString(com.jdroid.android.R.string.jdroid_installAppMessage, appName)
            val fragment = DownloadAppDialogFragment()
            fragment.setApplicationId(applicationId)
            AlertDialogFragment.show(fragmentActivity, fragment, title, message, fragmentActivity.getString(com.jdroid.android.R.string.jdroid_no), null, fragmentActivity.getString(com.jdroid.android.R.string.jdroid_yes), true)
        }
    }

    @JvmOverloads
    fun launchAppDetails(applicationId: String = AppUtils.getReleaseApplicationId()) {
        val uri = Uri.parse("market://details?id=$applicationId")
        var intent = Intent(Intent.ACTION_VIEW, uri)
        if (IntentUtils.isIntentAvailable(intent)) {
            ActivityLauncher.startActivityNewTask(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getGooglePlayLink(applicationId))
            ActivityLauncher.startActivityNewTask(intent)
        }
    }

    @JvmOverloads
    fun getGooglePlayLink(applicationId: String = AppUtils.getReleaseApplicationId(), referrer: String? = null): String {
        val builder = StringBuilder()
        builder.append(GOOGLE_PLAY_DETAILS_LINK)
        builder.append(applicationId)
        if (referrer != null) {
            builder.append("&referrer=")
            builder.append(referrer)
        }
        return builder.toString()
    }
}

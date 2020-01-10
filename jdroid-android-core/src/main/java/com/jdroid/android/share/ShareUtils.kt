package com.jdroid.android.share

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.text.Html

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.social.SocialAction
import com.jdroid.android.utils.ExternalAppsUtils
import com.jdroid.java.http.MimeType

object ShareUtils {

    fun shareTextContent(activity: Activity, shareKey: String, shareTitle: Int, shareSubject: Int, shareText: Int) {
        shareTextContent(activity, shareKey, activity.getString(shareTitle), activity.getString(shareSubject), activity.getString(shareText))
        AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(null, SocialAction.SHARE, shareKey)
    }

    fun shareTextContent(activity: Activity, shareKey: String, shareTitle: String, shareSubject: String, shareText: String) {
        val intent = createShareTextContentIntent(shareSubject, shareText)
        activity.startActivity(Intent.createChooser(intent, shareTitle))

        AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(null, SocialAction.SHARE, shareKey)
    }

    fun shareHtmlContent(activity: Activity, shareKey: String, shareTitle: String, shareSubject: String, shareText: String) {
        val intent = createShareHtmlContentIntent(shareSubject, shareText)
        ActivityLauncher.startActivity(activity, Intent.createChooser(intent, shareTitle))
        AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(null, SocialAction.SHARE, shareKey)
    }

    fun share(activity: Activity, sharingMedium: SharingMedium, shareKey: String, shareText: String) {
        val applicationId = sharingMedium.applicationId

        val intent = createShareTextContentIntent(null, shareText)
        intent.setPackage(applicationId)
        try {
            ActivityLauncher.startActivity(activity, intent)
            AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(sharingMedium.mediumName, SocialAction.SHARE, shareKey)
        } catch (e: ActivityNotFoundException) {
            val installedAppVersionCode = ExternalAppsUtils.getInstalledAppVersionCode(applicationId)
            var message = "ACTION_SEND not supported by $applicationId"
            if (installedAppVersionCode != null) {
                message += " version $installedAppVersionCode"
            }
            AbstractApplication.get().exceptionHandler.logWarningException(message, e)
        }
    }

    fun createShareTextContentIntent(shareSubject: String?, shareText: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = MimeType.TEXT
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        return intent
    }

    fun createShareHtmlContentIntent(shareSubject: String, shareText: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = MimeType.HTML
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareText))
        return intent
    }
}

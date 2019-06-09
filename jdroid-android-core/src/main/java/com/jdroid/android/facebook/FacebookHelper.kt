package com.jdroid.android.facebook

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.social.SocialAction
import com.jdroid.android.social.SocialNetwork

object FacebookHelper {

    fun openPage(pageId: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/$pageId"))
            ActivityLauncher.startActivityNewTask(intent)
        } catch (e: ActivityNotFoundException) {
            ActivityLauncher.startActivityNewTask(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/$pageId"))
            )
        } finally {
            AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(SocialNetwork.FACEBOOK.getName(), SocialAction.OPEN_PROFILE, pageId)
        }
    }
}

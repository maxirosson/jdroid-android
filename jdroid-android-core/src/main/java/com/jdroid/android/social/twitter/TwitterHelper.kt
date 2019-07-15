package com.jdroid.android.social.twitter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.social.SocialAction
import com.jdroid.android.social.SocialNetwork

object TwitterHelper {

    var CHARACTERS_LIMIT: Int = 140
    var URL_CHARACTERS_COUNT: Int = 22

    fun openProfile(account: String) {
        try {
            val intent = Intent()
            intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity")
            intent.putExtra("screen_name", account)
            ActivityLauncher.startActivityNewTask(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/$account"))
            ActivityLauncher.startActivityNewTask(intent)
        } finally {
            AbstractApplication.get().coreAnalyticsSender.trackSocialInteraction(SocialNetwork.TWITTER.networkName, SocialAction.OPEN_PROFILE, account)
        }
    }
}

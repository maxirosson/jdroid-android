package com.jdroid.android.instagram

import android.content.Intent
import android.net.Uri

import com.jdroid.android.activity.ActivityLauncher

object InstagramHelper {

    fun openProfile(account: String) {
        ActivityLauncher.startActivityNewTask(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/$account")))
    }
}

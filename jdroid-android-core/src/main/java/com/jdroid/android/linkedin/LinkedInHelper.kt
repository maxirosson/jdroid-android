package com.jdroid.android.linkedin

import android.content.Intent
import android.net.Uri

import com.jdroid.android.activity.ActivityLauncher

object LinkedInHelper {

    fun openCompanyPage(companyPageId: String) {
        ActivityLauncher.startActivityNewTask(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/$companyPageId"))
        )
    }
}

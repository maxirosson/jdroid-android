package com.jdroid.android.debug.appenders

import android.os.Environment
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.jdroid.android.R
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.permission.PermissionHelper
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.ExternalAppsUtils
import com.jdroid.java.collections.Lists
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.FileUtils
import java.io.File
import java.io.IOException

class UriMapperPrefsAppender(private var htmlRawId: Int? = null) : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_uriMapper
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {

        if (htmlRawId == null) {
            htmlRawId = activity.resources.getIdentifier("url_samples", "raw", AppUtils.getApplicationId())
        }

        var preference = Preference(activity)
        preference.setTitle(R.string.jdroid_downloadUrlSample)
        preference.setSummary(R.string.jdroid_downloadUrlSample)
        preference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                // TODO To make it work on Android N, read this https://medium.com/google-developers/sharing-content-between-android-apps-2e6db9d1368b#.aauyfutg4
                try {
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    dir.mkdirs()

                    val openInputStream = activity.resources.openRawResource(htmlRawId!!)
                    val file = File(dir, AppUtils.getApplicationId() + "_url_samples.html")
                    FileUtils.copyStream(openInputStream, file)
                    openInputStream.close()

                    ExternalAppsUtils.openOnBrowser(file)
                } catch (e: IOException) {
                    throw UnexpectedException(e)
                }

                return true
            }
        }
        preferenceGroup.addPreference(preference)

        preference = Preference(activity)
        preference.setTitle(R.string.jdroid_emailUrlSample)
        preference.setSummary(R.string.jdroid_emailUrlSample)
        preference.onPreferenceClickListener = object : OnPreferenceClickListener {
            override fun onPreferenceClick(it: Preference): Boolean {
                val builder = StringBuilder()
                for (each in AbstractApplication.get().debugContext.getUrlsToTest()) {
                    builder.append("<h4><a href=\"")
                    builder.append(each)
                    builder.append("\" target=\"_blank\">")
                    builder.append(each)
                    builder.append("</a></h4>")
                }

                val shareIntent = ShareCompat.IntentBuilder.from(activity)
                    .setType("text/html")
                    .setSubject(AppUtils.getApplicationName() + " urls samples")
                    .setHtmlText(builder.toString())
                    .intent
                if (shareIntent.resolveActivity(activity.packageManager) != null) {
                    ActivityLauncher.startActivity(activity, shareIntent)
                }

                return true
            }
        }
        preferenceGroup.addPreference(preference)
    }

    override fun getRequiredPermissions(): List<String> {
        return Lists.newArrayList(PermissionHelper.WRITE_EXTERNAL_STORAGE_PERMISSION)
    }
}

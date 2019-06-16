package com.jdroid.android.debug.appenders

import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.IdGenerator

class NotificationsDebugPrefsAppender : PreferencesAppender() {

    private val urlsToTest: List<String> = AbstractApplication.get().debugContext.getUrlsToTest()

    override fun getNameResId(): Int {
        return R.string.jdroid_notifications
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        for (url in urlsToTest) {
            val preference = Preference(activity)
            preference.title = url
            preference.summary = url
            preference.onPreferenceClickListener = object : OnPreferenceClickListener {
                override fun onPreferenceClick(it: Preference): Boolean {
                    val builder = NotificationBuilder("notificationFromBundle", AbstractApplication.get().notificationChannelTypes[0])
                    builder.setTicker("Sample Ticker")
                    builder.setContentTitle("Sample Content Title")
                    builder.setContentText(url)
                    builder.setSingleTopUrl(url)
                    builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
                    builder.setWhen(DateUtils.nowMillis())

                    NotificationUtils.sendNotification(IdGenerator.getIntId()!!, builder)
                    return true
                }
            }
            preferenceGroup.addPreference(preference)
        }
    }

    override fun isEnabled(): Boolean {
        return urlsToTest.isNullOrEmpty()
    }
}

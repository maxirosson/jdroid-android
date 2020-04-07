package com.jdroid.android.sample.application

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import com.jdroid.android.notification.NotificationChannelType
import com.jdroid.android.sample.R
import com.jdroid.android.utils.LocalizationUtils

enum class AndroidNotificationChannelType constructor(
    private val channelId: String,
    @StringRes private val nameResId: Int,
    private val importance: Int,
    private val isDeprecated: Boolean = false
) : NotificationChannelType {

    LOW_IMPORTANCE("lowImportance", R.string.lowImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_LOW) {
        override fun config(notificationChannel: NotificationChannel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.description = "This is a sample description for low importance notification channel"
                notificationChannel.setShowBadge(false)
            }
        }
    },
    DEFAULT_IMPORTANCE("defaultImportance", R.string.defaultImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_DEFAULT),
    HIGH_IMPORTANCE("highImportance", R.string.highImportanceNotificationChannelName, NotificationManagerCompat.IMPORTANCE_HIGH) {
        override fun config(notificationChannel: NotificationChannel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setShowBadge(true)
            }
        }
    };

    override fun config(notificationChannel: NotificationChannel) {
        // Do nothing
    }

    override fun getChannelId(): String {
        return channelId
    }

    override fun getUserVisibleName(): String {
        return LocalizationUtils.getRequiredString(nameResId)
    }

    override fun getImportance(): Int {
        return importance
    }

    override fun isDeprecated(): Boolean {
        return isDeprecated
    }
}

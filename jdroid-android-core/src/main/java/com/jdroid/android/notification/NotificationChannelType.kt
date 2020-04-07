package com.jdroid.android.notification

import android.app.NotificationChannel

interface NotificationChannelType {

    fun getChannelId(): String

    fun getUserVisibleName(): String

    fun getImportance(): Int

    /*
	 * Return false if the notification channel should be created. Return true if the notification channel was already
	 * created in the past, and should be removed because is currently deprecated.
	 */
    fun isDeprecated(): Boolean

    fun config(notificationChannel: NotificationChannel)
}

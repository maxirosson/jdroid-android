package com.jdroid.android.firebase.fcm.instanceid

import androidx.annotation.WorkerThread
import com.google.firebase.iid.FirebaseInstanceId
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.google.GooglePlayServicesUtils
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.utils.LoggerUtils
import java.io.IOException
import java.util.UUID

object InstanceIdHelper {

    private val LOGGER = LoggerUtils.getLogger(InstanceIdHelper::class.java)

    private const val INSTANCE_ID_PREFERENCES = "usageStats"
    private const val INSTANCE_ID = "instanceId"
    private const val ANONYMOUS_INSTANCE_ID = "anonymousInstanceId"

    private val sharedPreferencesHelper: SharedPreferencesHelper by lazy { SharedPreferencesHelper.get(INSTANCE_ID_PREFERENCES) }

    private var instanceId: String? = null
    private var anonymousInstanceId: String? = null

    @WorkerThread
    @Synchronized
    fun getInstanceId(): String {
        if (instanceId == null) {
            instanceId = sharedPreferencesHelper.loadPreference(INSTANCE_ID)
            if (instanceId == null) {
                instanceId = FirebaseInstanceId.getInstance().id
                sharedPreferencesHelper.savePreferenceAsync(INSTANCE_ID, instanceId)
            }
            LOGGER.debug("Instance id: " + instanceId!!)
        }
        if (instanceId != null) {
            return instanceId!!
        } else {
            if (anonymousInstanceId == null) {
                anonymousInstanceId = sharedPreferencesHelper.loadPreference(ANONYMOUS_INSTANCE_ID)
                if (anonymousInstanceId == null) {
                    anonymousInstanceId = "anonymous" + UUID.randomUUID()
                    sharedPreferencesHelper.savePreferenceAsync(ANONYMOUS_INSTANCE_ID, anonymousInstanceId)
                }
                LOGGER.debug("Anonymous Instance id: " + anonymousInstanceId!!)
            }
            return anonymousInstanceId!!
        }
    }

    @WorkerThread
    fun removeInstanceId() {
        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
                instanceId = null
                sharedPreferencesHelper.removePreferences(INSTANCE_ID)
            } catch (e: IOException) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        } else {
            LOGGER.warn("Instance id not removed because Google Play Services is not available")
        }
    }
}

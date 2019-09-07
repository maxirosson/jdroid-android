package com.jdroid.android.firebase.fcm

import android.content.Context
import android.os.Bundle
import androidx.annotation.WorkerThread
import com.google.android.gms.tasks.Tasks
import com.google.firebase.iid.FirebaseInstanceId
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand
import com.jdroid.android.google.GooglePlayServicesUtils
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.LoggerUtils
import java.io.IOException
import java.util.concurrent.ExecutionException

// TODO By Google recommendation, we should execute this command every 2 weeks, to have always fresh tokens on server side
class FcmRegistrationCommand : ServiceCommand() {

    fun start(updateLastActiveTimestamp: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp)
        start(bundle)
    }

    override fun execute(context: Context, bundle: Bundle): Boolean {
        if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(context)) {
            for (fcmSender in AbstractFcmAppModule.get().getFcmSenders()) {
                val registrationToken: String?
                try {
                    registrationToken = getRegistrationToken(fcmSender.getSenderId())
                } catch (e: Exception) {
                    AbstractApplication.get().exceptionHandler.logHandledException("Error when getting FCM registration token. Will retry later.", e)
                    return true
                }

                if (registrationToken == null) {
                    LOGGER.info("Null registration token. Will retry later.")
                    return true
                }

                try {
                    LOGGER.info("Registering FCM token on server")
                    val updateLastActiveTimestamp = bundle.getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false)
                    fcmSender.onRegisterOnServer(registrationToken, updateLastActiveTimestamp, bundle)
                } catch (e: Exception) {
                    AbstractApplication.get().exceptionHandler.logHandledException(
                        "Failed to register the device on server. Will retry later.", e
                    )
                    return true
                }
            }
            return false
        } else {
            LOGGER.warn("FCM not initialized because Google Play Services is not available")
            return true
        }
    }

    companion object {

        private val LOGGER = LoggerUtils.getLogger(FcmRegistrationCommand::class.java)

        private const val UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp"

        @WorkerThread
        fun getRegistrationToken(senderId: String?): String? {
            if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {
                val registrationToken: String?
                try {
                    if (senderId != null) {
                        registrationToken = FirebaseInstanceId.getInstance().getToken(senderId, "FCM")
                        LOGGER.info("Registration token for sender id [$senderId]: $registrationToken")
                    } else {
                        val task = FirebaseInstanceId.getInstance().instanceId
                        // Block on the task and get the result synchronously
                        val instanceIdResult = Tasks.await(task)
                        registrationToken = instanceIdResult.token
                        LOGGER.info("Registration token for default sender id: $registrationToken")
                    }
                } catch (e: IOException) {
                    throw UnexpectedException(e)
                } catch (e: ExecutionException) {
                    throw UnexpectedException(e)
                } catch (e: InterruptedException) {
                    throw UnexpectedException(e)
                }
                return registrationToken
            }
            return null
        }
    }
}

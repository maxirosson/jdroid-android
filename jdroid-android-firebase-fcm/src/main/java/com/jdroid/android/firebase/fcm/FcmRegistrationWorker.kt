package com.jdroid.android.firebase.fcm

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.iid.FirebaseInstanceId
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule.Companion.get
import com.jdroid.android.google.GooglePlayServicesUtils.isGooglePlayServicesAvailable
import com.jdroid.android.jetpack.work.AbstractWorker
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.LoggerUtils
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class FcmRegistrationWorker(context: Context, workerParams: WorkerParameters) : AbstractWorker(context, workerParams) {

    companion object {

        const val WORK_MANAGER_TAG = "fcm"
        private val LOGGER = LoggerUtils.getLogger(FcmRegistrationWorker::class.java)
        private const val UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA = "updateLastActiveTimestamp"

        fun enqueue(updateLastActiveTimestamp: Boolean) {
            val requestBuilder = OneTimeWorkRequest.Builder(FcmRegistrationWorker::class.java)
            val dataBuilder = Data.Builder()
            dataBuilder.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp)
            requestBuilder.setInputData(dataBuilder.build())
            val constrainsBuilder = Constraints.Builder()
            constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
            requestBuilder.setConstraints(constrainsBuilder.build())
            requestBuilder.addTag(WORK_MANAGER_TAG)
            WorkManager.getInstance(AbstractApplication.get())
                .beginUniqueWork(FcmRegistrationWorker::class.java.simpleName, ExistingWorkPolicy.KEEP, requestBuilder.build()).enqueue()
        }

        @WorkerThread
        fun getRegistrationToken(senderId: String?): String? {
            if (isGooglePlayServicesAvailable(AbstractApplication.get())) {
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

    override fun onWork(): Result {
        return if (isGooglePlayServicesAvailable(applicationContext)) {
            val updateLastActiveTimestamp = inputData.getBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, false)
            for (fcmSender in AbstractFcmAppModule.get().getFcmSenders()) {
                val registrationToken: String?
                try {
                    registrationToken = getRegistrationToken(fcmSender.getSenderId())
                } catch (e: Exception) {
                    AbstractApplication.get().exceptionHandler.logHandledException("Error when getting FCM registration token. Will retry later.", e)
                    return Result.retry()
                }

                if (registrationToken == null) {
                    LOGGER.info("Null registration token. Will retry later.")
                    return Result.retry()
                }

                try {
                    LOGGER.info("Registering FCM token on server")
                    fcmSender.onRegisterOnServer(registrationToken, updateLastActiveTimestamp, inputData.keyValueMap)
                } catch (e: Exception) {
                    AbstractApplication.get().exceptionHandler.logHandledException("Failed to register the device on server. Will retry later.", e)
                    return Result.retry()
                }
            }
            startPeriodic(updateLastActiveTimestamp)
            Result.success()
        } else {
            LOGGER.warn("FCM not initialized because Google Play Services is not available")
            Result.retry()
        }
    }

    // By Google recommendation, we should execute this worker every 2 weeks, to have always fresh tokens on server side
    private fun startPeriodic(updateLastActiveTimestamp: Boolean) {
        val requestBuilder = PeriodicWorkRequest.Builder(FcmRegistrationWorker::class.java,
            14L, TimeUnit.DAYS,
            7L, TimeUnit.DAYS
        )
        val dataBuilder = Data.Builder()
        dataBuilder.putBoolean(UPDATE_LAST_ACTIVE_TIMESTAMP_EXTRA, updateLastActiveTimestamp)
        requestBuilder.setInputData(dataBuilder.build())
        val constrainsBuilder = Constraints.Builder()
        constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
        requestBuilder.setConstraints(constrainsBuilder.build())
        requestBuilder.addTag(WORK_MANAGER_TAG)
        WorkManager.getInstance(AbstractApplication.get()).enqueueUniquePeriodicWork(
            FcmRegistrationWorker::class.java.simpleName,
            ExistingPeriodicWorkPolicy.KEEP,
            requestBuilder.build()
        )
    }
}

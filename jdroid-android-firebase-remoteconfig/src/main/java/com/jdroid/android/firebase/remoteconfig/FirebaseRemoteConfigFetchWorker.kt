package com.jdroid.android.firebase.remoteconfig

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.jetpack.work.AbstractWorker

class FirebaseRemoteConfigFetchWorker(context: Context, workerParams: WorkerParameters) : AbstractWorker(context, workerParams) {

    override fun onWork(): Result {
        val setExperimentUserProperty = inputData.getBoolean(SET_EXPERIMENT_USER_PROPERTY, false)
        FirebaseRemoteConfigLoader.get().fetch(setExperimentUserProperty, null)
        return Result.success()
    }

    companion object {

        const val WORK_MANAGER_TAG = "firebase_remote_config"
        private const val SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty"

        @JvmStatic
        fun enqueue() {
            enqueue(false)
        }

        @JvmStatic
        fun enqueue(setExperimentUserProperty: Boolean) {
            val requestBuilder = OneTimeWorkRequest.Builder(FirebaseRemoteConfigFetchWorker::class.java)
            val dataBuilder = Data.Builder()
            dataBuilder.putBoolean(SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty)
            requestBuilder.setInputData(dataBuilder.build())
            val constrainsBuilder = Constraints.Builder()
            constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
            requestBuilder.setConstraints(constrainsBuilder.build())
            requestBuilder.addTag(WORK_MANAGER_TAG)
            WorkManager.getInstance(AbstractApplication.get())
                .beginUniqueWork(FirebaseRemoteConfigFetchWorker::class.java.simpleName, ExistingWorkPolicy.KEEP, requestBuilder.build())
                .enqueue()
        }
    }
}
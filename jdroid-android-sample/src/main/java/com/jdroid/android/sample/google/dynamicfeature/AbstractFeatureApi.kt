package com.jdroid.android.sample.google.dynamicfeature

import com.google.android.play.core.tasks.OnFailureListener
import com.google.android.play.core.tasks.OnSuccessListener
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.google.splitinstall.SplitInstallHelper
import com.jdroid.android.google.splitinstall.SplitInstallHelper.LOGGER

abstract class AbstractFeatureApi {

    fun invokeFeatureAsync(action: () -> Unit, onFailure: (Exception) -> Unit = { }) {

        val onFailureListener = OnFailureListener {
            LOGGER.debug(getModuleName() + " onFailureListener called when installing it")
            onFailure.invoke(it)
        }

        val onSuccessListener = OnSuccessListener<Any> {
            try {
                action.invoke()
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
                onFailureListener.onFailure(e)
            }
        }
        AppExecutors.getNetworkIOExecutor().execute(object : Runnable {
            override fun run() {
                SplitInstallHelper.installModuleSilently(getModuleName(), onSuccessListener, onFailureListener)
            }
        })
    }

    protected abstract fun getModuleName(): String
}
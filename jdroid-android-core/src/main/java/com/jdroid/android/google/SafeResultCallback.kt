package com.jdroid.android.google

import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.ResultCallback
import com.jdroid.android.application.AbstractApplication

abstract class SafeResultCallback<R : Result> : ResultCallback<R> {

    override fun onResult(result: R) {
        try {
            if (result.status.isSuccess) {
                onSuccessResult(result)
            } else {
                onFailedResult(result)
            }
        } catch (e: Exception) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }

    abstract fun onSuccessResult(result: R)

    abstract fun onFailedResult(result: R)
}

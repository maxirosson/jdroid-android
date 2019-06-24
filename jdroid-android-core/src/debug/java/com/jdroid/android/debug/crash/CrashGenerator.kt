package com.jdroid.android.debug.crash

import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.exception.DialogErrorDisplayer
import com.jdroid.java.exception.AbstractException

object CrashGenerator {

    fun crash(exceptionType: ExceptionType, executeOnNewThread: Boolean) {
        val runnable = Runnable {
            try {
                exceptionType.crash()
            } catch (e: AbstractException) {
                DialogErrorDisplayer.markAsNotGoBackOnError(e)
                throw e
            }
        }
        if (executeOnNewThread) {
            AppExecutors.networkIOExecutor.execute(runnable)
        } else {
            runnable.run()
        }
    }
}

package com.jdroid.android.concurrent

import com.jdroid.android.application.AbstractApplication

abstract class SafeRunnable : Runnable {

    override fun run() {
        try {
            doRun()
        } catch (e: RuntimeException) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }

    abstract fun doRun()
}

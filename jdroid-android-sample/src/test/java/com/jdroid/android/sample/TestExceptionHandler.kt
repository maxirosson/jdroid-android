package com.jdroid.android.sample

import com.jdroid.android.exception.DefaultExceptionHandler

class TestExceptionHandler : DefaultExceptionHandler() {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun logHandledException(errorMessage: String, throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun logHandledException(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun logWarningException(errorMessage: String) {
        println(errorMessage)
    }

    override fun logWarningException(errorMessage: String, throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun logWarningException(throwable: Throwable) {
        throwable.printStackTrace()
    }
}

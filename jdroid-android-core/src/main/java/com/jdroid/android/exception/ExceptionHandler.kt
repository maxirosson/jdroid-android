package com.jdroid.android.exception

import com.jdroid.java.utils.LoggerUtils.ExceptionLogger

import java.lang.Thread.UncaughtExceptionHandler

interface ExceptionHandler : UncaughtExceptionHandler, ExceptionLogger {

    fun setDefaultExceptionHandler(uncaughtExceptionHandler: UncaughtExceptionHandler)

    fun logHandledException(errorMessage: String, throwable: Throwable)

    fun logHandledException(errorMessage: String)

    fun logWarningException(errorMessage: String, throwable: Throwable)

    fun logWarningException(throwable: Throwable)

    fun logWarningException(errorMessage: String)

    fun logIgnoreStackTraceWarningException(errorMessage: String)
}

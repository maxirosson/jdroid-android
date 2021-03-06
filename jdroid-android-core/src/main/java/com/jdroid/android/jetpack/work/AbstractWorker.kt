package com.jdroid.android.jetpack.work

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.perf.metrics.Trace
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.performance.TraceHelper
import com.jdroid.java.date.DateUtils.formatDuration
import com.jdroid.java.date.DateUtils.nowMillis
import com.jdroid.java.http.exception.ConnectionException
import com.jdroid.java.utils.LoggerUtils

abstract class AbstractWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @WorkerThread
    final override fun doWork(): Result {
        var trace: Trace? = null
        var result: Result? = null
        try {
            if (isTimingTrackingEnabled()) {
                trace = TraceHelper.startTrace(trackingTag)
            }
            LoggerUtils.getLogger(trackingTag).info("Executing Worker.")
            val startTime = nowMillis()
            result = onWork()
            val executionTime = nowMillis() - startTime
            LoggerUtils.getLogger(trackingTag)
                .info("Worker finished. Result: $result. Run attempt: $runAttemptCount. Execution time: " + formatDuration(executionTime))
        } catch (e: Throwable) {
            result = getResult(e)
            LoggerUtils.getLogger(trackingTag)
                .error("Worker finished with exception. Result: $result. Run attempt: $runAttemptCount")
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        } finally {
            if (trace != null) {
                if (result != null) {
                    trace.putAttribute("result", result.javaClass.simpleName)
                    trace.incrementMetric(result.javaClass.simpleName, 1)
                }
                trace.stop()
            }
        }
        return result!!
    }

    @WorkerThread
    protected abstract fun onWork(): Result

    protected open fun getResult(throwable: Throwable?): Result {
        return if (throwable is ConnectionException) Result.retry() else Result.failure()
    }

    protected open fun isTimingTrackingEnabled(): Boolean {
        return true
    }

    protected open val trackingTag: String = javaClass.simpleName
}

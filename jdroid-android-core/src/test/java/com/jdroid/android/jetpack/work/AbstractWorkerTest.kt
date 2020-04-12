package com.jdroid.android.jetpack.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.testing.TestWorkerBuilder
import androidx.work.workDataOf
import com.jdroid.android.AbstractUnitTest
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.http.exception.ConnectionException
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.Executor

class AbstractWorkerTest : AbstractUnitTest() {

    @Test
    fun `GIVEN a successful worker WHEN invoke doWork THEN expect a success result`() {
        val worker = TestWorkerBuilder<SuccessWorker>(AbstractApplication.get(), FakeExecutor(), workDataOf()).build()
        Assert.assertTrue(worker.doWork() is ListenableWorker.Result.Success)
    }

    @Test
    fun `GIVEN a worker that throws a ConnectionException WHEN invoke doWork THEN expect a retry result`() {
        val worker = TestWorkerBuilder<ConnectionExceptionWorker>(AbstractApplication.get(), FakeExecutor(), workDataOf()).build()
        Assert.assertTrue(worker.doWork() is ListenableWorker.Result.Retry)
    }

    @Test
    fun `GIVEN a worker that throws a RuntimeException WHEN invoke doWork THEN expect a failure result`() {
        val worker = TestWorkerBuilder<RuntimeExceptionWorker>(AbstractApplication.get(), FakeExecutor(), workDataOf()).build()
        Assert.assertTrue(worker.doWork() is ListenableWorker.Result.Failure)
    }

    class FakeExecutor : Executor {
        override fun execute(command: Runnable?) {
            // Do nothing
        }
    }

    class SuccessWorker(appContext: Context, workerParams: WorkerParameters) : AbstractWorker(appContext, workerParams) {
        override fun onWork(): Result {
            return Result.success()
        }

        override fun isTimingTrackingEnabled(): Boolean {
            return false
        }
    }

    class ConnectionExceptionWorker(appContext: Context, workerParams: WorkerParameters) : AbstractWorker(appContext, workerParams) {
        override fun onWork(): Result {
            throw ConnectionException("")
        }

        override fun isTimingTrackingEnabled(): Boolean {
            return false
        }
    }

    class RuntimeExceptionWorker(appContext: Context, workerParams: WorkerParameters) : AbstractWorker(appContext, workerParams) {
        override fun onWork(): Result {
            throw RuntimeException()
        }

        override fun isTimingTrackingEnabled(): Boolean {
            return false
        }
    }
}

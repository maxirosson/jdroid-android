package com.jdroid.android.sample.ui.service

import android.content.Context
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.jdroid.android.jetpack.work.AbstractWorker
import com.jdroid.java.concurrent.ExecutorUtils.sleep
import java.util.concurrent.TimeUnit

class SampleWorker3(context: Context, workerParams: WorkerParameters) : AbstractWorker(context, workerParams) {
    override fun onWork(): Result {
        sleep(30, TimeUnit.SECONDS)
        val outputData = workDataOf("result" to "ok")
        return Result.success(outputData)
    }
}
package com.jdroid.android.sample.ui.service

import android.content.Context
import androidx.work.WorkerParameters
import com.jdroid.android.jetpack.work.AbstractWorker
import com.jdroid.java.concurrent.ExecutorUtils.sleep
import java.util.concurrent.TimeUnit

class SampleWorker4(context: Context, workerParams: WorkerParameters) :
    AbstractWorker(context, workerParams) {
    override fun onWork(): Result {
        sleep(20, TimeUnit.SECONDS)
        return Result.success()
    }

    // TODO
    //	@Nullable
    //	@Override
    //	protected Long getMinimumTimeBetweenExecutions() {
    //		return TimeUnit.MINUTES.toMillis(1);
    //	}
}
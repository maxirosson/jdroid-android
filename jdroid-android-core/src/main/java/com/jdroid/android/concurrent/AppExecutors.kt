package com.jdroid.android.concurrent

import android.os.Handler
import android.os.Looper

import com.jdroid.java.concurrent.NormalPriorityThreadFactory

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation
 * (e.g. disk reads don't wait behind webservice requests).
 */
object AppExecutors {

    val diskIOExecutor: Executor = Executors.newSingleThreadExecutor(NormalPriorityThreadFactory("jdroid-disk"))
    val networkIOExecutor: Executor = Executors.newFixedThreadPool(3, NormalPriorityThreadFactory("jdroid-network"))
    val mainThreadExecutor: Executor = MainThreadExecutor()

    private class MainThreadExecutor : Executor {

        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}

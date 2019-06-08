package com.jdroid.android.application

import androidx.annotation.WorkerThread

interface UpdateStep {

    @WorkerThread
    fun update()

    fun getVersionCode(): Int
}

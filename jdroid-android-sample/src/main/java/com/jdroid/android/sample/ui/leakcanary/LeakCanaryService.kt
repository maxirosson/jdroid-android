package com.jdroid.android.sample.ui.leakcanary

import android.content.Intent

import com.jdroid.android.service.AbstractWorkerService

class LeakCanaryService : AbstractWorkerService() {

    override fun doExecute(intent: Intent) {
        LEAK = this
    }

    companion object {
        var LEAK: AbstractWorkerService? = null
    }
}

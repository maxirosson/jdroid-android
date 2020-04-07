package com.jdroid.android.sample.ui.service

import android.os.Bundle

import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.Job
import com.firebase.jobdispatcher.Trigger
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand

abstract class AbstractSampleServiceCommand : ServiceCommand() {

    override fun createJobBuilder(dispatcher: FirebaseJobDispatcher, bundle: Bundle): Job.Builder {
        val builder = super.createJobBuilder(dispatcher, bundle)
        val delay = bundle.getInt("delay")
        if (delay != 0) {
            builder.trigger = Trigger.executionWindow(delay, delay + 10)
        }
        return builder
    }
}

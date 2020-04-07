package com.jdroid.android.sample.ui.service

import android.content.Context
import android.os.Bundle

import com.jdroid.java.concurrent.ExecutorUtils

import java.util.concurrent.TimeUnit

class SampleServiceCommand3 : AbstractSampleServiceCommand() {

    override fun execute(context: Context, bundle: Bundle): Boolean {
        ExecutorUtils.sleep(30, TimeUnit.SECONDS)
        return false
    }
}

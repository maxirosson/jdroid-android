package com.jdroid.android.sample.ui.leakcanary

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.service.AbstractWorkerService

class LeakCanaryActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return LeakCanaryFragment::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LEAK = this

        AbstractWorkerService.runIntentInService(AbstractApplication.get(), Intent(), LeakCanaryService::class.java)
    }

    companion object {
        var LEAK: LeakCanaryActivity? = null
    }
}

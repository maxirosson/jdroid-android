package com.jdroid.android.sample.ui.service

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class ServiceActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return ServiceFragment::class.java
    }
}

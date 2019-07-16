package com.jdroid.android.sample.ui.http

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class HttpActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return HttpFragment::class.java
    }
}
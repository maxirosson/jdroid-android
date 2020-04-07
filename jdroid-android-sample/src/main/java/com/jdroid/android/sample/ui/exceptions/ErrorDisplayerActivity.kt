package com.jdroid.android.sample.ui.exceptions

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class ErrorDisplayerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return ErrorDisplayerFragment::class.java
    }
}

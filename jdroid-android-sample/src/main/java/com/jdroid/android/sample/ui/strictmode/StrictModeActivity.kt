package com.jdroid.android.sample.ui.strictmode

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class StrictModeActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return StrictModeFragment::class.java
    }
}

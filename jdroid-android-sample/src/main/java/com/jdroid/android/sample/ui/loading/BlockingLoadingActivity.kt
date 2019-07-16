package com.jdroid.android.sample.ui.loading

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class BlockingLoadingActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return BlockingLoadingFragment::class.java
    }
}
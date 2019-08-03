package com.jdroid.android.sample.ui.loading

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class LoadingActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return LoadingFragment::class.java
    }
}

package com.jdroid.android.sample.ui.navdrawer

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class NoNavDrawerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return NoNavDrawerFragment::class.java
    }

    override fun isNavDrawerEnabled(): Boolean {
        return false
    }
}
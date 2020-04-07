package com.jdroid.android.sample.ui.navdrawer

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class NavDrawerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return NavDrawerFragment::class.java
    }
}

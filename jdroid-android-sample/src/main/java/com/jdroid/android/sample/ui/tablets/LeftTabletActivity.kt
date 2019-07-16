package com.jdroid.android.sample.ui.tablets

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class LeftTabletActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return LeftTabletFragment::class.java
    }
}

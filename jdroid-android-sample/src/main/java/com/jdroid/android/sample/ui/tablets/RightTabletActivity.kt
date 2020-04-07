package com.jdroid.android.sample.ui.tablets

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class RightTabletActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return RightTabletFragment::class.java
    }
}

package com.jdroid.android.debug.info

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class DebugInfoActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment>? {
        return DebugInfoFragment::class.java
    }
}

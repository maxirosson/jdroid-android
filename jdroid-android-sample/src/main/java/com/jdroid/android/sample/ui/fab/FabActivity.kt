package com.jdroid.android.sample.ui.fab

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FabActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FabFragment::class.java
    }
}

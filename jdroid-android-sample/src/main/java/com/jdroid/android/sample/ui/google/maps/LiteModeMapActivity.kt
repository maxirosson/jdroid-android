package com.jdroid.android.sample.ui.google.maps

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class LiteModeMapActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return LiteModeFragment::class.java
    }
}

package com.jdroid.android.sample.ui.google.maps

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class StreetViewActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return StreetViewFragment::class.java
    }
}

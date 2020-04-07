package com.jdroid.android.sample.ui.rateme

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class RateAppActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return RateAppFragment::class.java
    }
}

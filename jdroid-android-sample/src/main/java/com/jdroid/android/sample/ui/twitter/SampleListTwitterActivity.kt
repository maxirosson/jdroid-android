package com.jdroid.android.sample.ui.twitter

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class SampleListTwitterActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return SampleListTwitterFragment::class.java
    }
}

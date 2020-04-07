package com.jdroid.android.sample.ui.twitter

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class CyclingTwitterActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return CyclingTwitterFragment::class.java
    }
}

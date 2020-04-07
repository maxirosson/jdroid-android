package com.jdroid.android.sample.ui.google.admob

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FragmentBannerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FragmentBannerFragment::class.java
    }
}

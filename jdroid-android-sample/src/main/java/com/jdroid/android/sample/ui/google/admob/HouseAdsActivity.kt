package com.jdroid.android.sample.ui.google.admob

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class HouseAdsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return HouseAdsFragment::class.java
    }
}

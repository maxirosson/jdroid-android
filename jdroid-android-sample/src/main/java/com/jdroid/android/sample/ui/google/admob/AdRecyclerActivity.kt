package com.jdroid.android.sample.ui.google.admob

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class AdRecyclerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AdRecyclerFragment::class.java
    }
}

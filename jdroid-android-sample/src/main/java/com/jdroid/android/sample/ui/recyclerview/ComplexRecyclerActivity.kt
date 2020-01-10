package com.jdroid.android.sample.ui.recyclerview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class ComplexRecyclerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return ComplexRecyclerFragment::class.java
    }
}

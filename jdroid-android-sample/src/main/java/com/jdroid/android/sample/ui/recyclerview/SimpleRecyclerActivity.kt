package com.jdroid.android.sample.ui.recyclerview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class SimpleRecyclerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return SimpleRecyclerFragment::class.java
    }
}
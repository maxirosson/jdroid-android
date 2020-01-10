package com.jdroid.android.sample.ui.recyclerview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class RecyclerViewActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return RecyclerViewFragment::class.java
    }
}

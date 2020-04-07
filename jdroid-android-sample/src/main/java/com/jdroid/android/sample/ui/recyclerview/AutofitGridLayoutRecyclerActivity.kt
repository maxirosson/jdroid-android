package com.jdroid.android.sample.ui.recyclerview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class AutofitGridLayoutRecyclerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AutofitGridLayoutRecyclerFragment::class.java
    }
}

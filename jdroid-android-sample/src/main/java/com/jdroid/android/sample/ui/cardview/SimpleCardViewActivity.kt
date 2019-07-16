package com.jdroid.android.sample.ui.cardview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class SimpleCardViewActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return SimpleCardViewFragment::class.java
    }
}
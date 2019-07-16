package com.jdroid.android.sample.ui.cardview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class CardViewActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return CardViewFragment::class.java
    }
}
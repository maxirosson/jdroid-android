package com.jdroid.android.sample.ui.twitter

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class TwitterActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return TwitterFragment::class.java
    }
}
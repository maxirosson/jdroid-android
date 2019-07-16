package com.jdroid.android.sample.ui.hero

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class HeroActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return HeroFragment::class.java
    }
}
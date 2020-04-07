package com.jdroid.android.sample.ui.glide

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class GlideActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return GlideFragment::class.java
    }
}

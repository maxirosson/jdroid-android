package com.jdroid.android.sample.ui.toasts

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class ToastsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return ToastsFragment::class.java
    }
}

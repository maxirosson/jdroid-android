package com.jdroid.android.sample.ui.firebase.dynamiclinks

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class DynamicLinksActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return DynamicLinksFragment::class.java
    }
}

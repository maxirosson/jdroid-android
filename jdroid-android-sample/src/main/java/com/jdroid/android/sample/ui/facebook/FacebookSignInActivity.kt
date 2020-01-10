package com.jdroid.android.sample.ui.facebook

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FacebookSignInActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FacebookSignInFragment::class.java
    }
}

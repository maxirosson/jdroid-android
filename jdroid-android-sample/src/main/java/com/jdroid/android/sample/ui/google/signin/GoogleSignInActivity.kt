package com.jdroid.android.sample.ui.google.signin

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class GoogleSignInActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return GoogleSignInFragment::class.java
    }
}

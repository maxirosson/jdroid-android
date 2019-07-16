package com.jdroid.android.sample.ui.google.playservices

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class GooglePlayServicesActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return GooglePlayServicesFragment::class.java
    }

    override fun isGooglePlayServicesVerificationEnabled(): Boolean {
        return true
    }
}
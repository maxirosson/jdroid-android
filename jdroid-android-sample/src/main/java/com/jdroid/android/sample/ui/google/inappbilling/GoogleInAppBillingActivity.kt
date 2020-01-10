package com.jdroid.android.sample.ui.google.inappbilling

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class GoogleInAppBillingActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return GoogleInAppBillingFragment::class.java
    }
}

package com.jdroid.android.sample.ui.firebase.fcm

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FcmActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FcmFragment::class.java
    }
}

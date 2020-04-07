package com.jdroid.android.firebase.remoteconfig

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FirebaseRemoteConfigActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FirebaseRemoteConfigFragment::class.java
    }
}

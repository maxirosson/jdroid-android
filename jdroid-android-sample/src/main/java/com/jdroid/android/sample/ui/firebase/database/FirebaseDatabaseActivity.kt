package com.jdroid.android.sample.ui.firebase.database

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class FirebaseDatabaseActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return FirebaseDatabaseFragment::class.java
    }
}

package com.jdroid.android.debug

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class DebugSettingsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return DebugSettingsFragment::class.java
    }
}

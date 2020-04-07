package com.jdroid.android.sample.androidx

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class ArchitectureActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return ArchitectureFragment::class.java
    }
}

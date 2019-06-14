package com.jdroid.android.sample.google.dynamicfeature

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class SplitInstallActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment>? {
        return SplitInstallFragment::class.java
    }
}

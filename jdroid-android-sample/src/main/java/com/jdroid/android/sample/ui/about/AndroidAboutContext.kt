package com.jdroid.android.sample.ui.about

import com.jdroid.android.about.AboutContext
import com.jdroid.android.fragment.AbstractFragment

class AndroidAboutContext : AboutContext() {

    override fun getSpreadTheLoveFragmentClass(): Class<out AbstractFragment>? {
        return AndroidSpreadTheLoveFragment::class.java
    }

    override fun isBetaTestingEnabled(): Boolean {
        return true
    }
}

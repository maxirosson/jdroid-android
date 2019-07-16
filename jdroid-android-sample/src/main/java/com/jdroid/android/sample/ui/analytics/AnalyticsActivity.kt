package com.jdroid.android.sample.ui.analytics

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class AnalyticsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AnalyticsFragment::class.java
    }
}

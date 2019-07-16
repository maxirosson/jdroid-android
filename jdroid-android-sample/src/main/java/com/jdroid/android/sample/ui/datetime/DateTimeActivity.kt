package com.jdroid.android.sample.ui.datetime

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class DateTimeActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return DateTimeFragment::class.java
    }
}

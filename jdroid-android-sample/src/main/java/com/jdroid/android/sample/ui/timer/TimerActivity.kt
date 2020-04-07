package com.jdroid.android.sample.ui.timer

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class TimerActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return TimerFragment::class.java
    }
}

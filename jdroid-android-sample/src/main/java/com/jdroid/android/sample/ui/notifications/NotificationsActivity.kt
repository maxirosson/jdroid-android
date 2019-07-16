package com.jdroid.android.sample.ui.notifications

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class NotificationsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return NotificationsFragment::class.java
    }
}

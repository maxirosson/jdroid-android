package com.jdroid.android.sample.ui.navdrawer

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.navdrawer.NavDrawer
import com.jdroid.android.sample.R

class LeftCustomNavDrawerActivity : FragmentContainerActivity() {

    override fun getContentView(): Int {
        return R.layout.left_nav_fragment_container_activity
    }

    override fun getFragmentClass(): Class<out Fragment> {
        return LeftCustomNavDrawerFragment::class.java
    }

    override fun createNavDrawer(activity: AbstractFragmentActivity, appBar: Toolbar): NavDrawer {
        return LeftSampleNavDrawer(activity, appBar)
    }
}

package com.jdroid.android.sample.ui.home

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.navdrawer.NavDrawer
import com.jdroid.android.uri.UriHandler

class HomeActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return HomeFragment::class.java
    }

    override fun createNavDrawer(activity: AbstractFragmentActivity, appBar: Toolbar): NavDrawer {
        val navDrawer = super.createNavDrawer(activity, appBar)
        navDrawer.setIsNavDrawerTopLevelView(true)
        return navDrawer
    }

    override fun createUriHandler(): UriHandler<*>? {
        return HomeUriHandler()
    }
}

package com.jdroid.android.sample.ui

import androidx.appcompat.widget.Toolbar
import com.jdroid.android.about.AboutActivity
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityHelper
import com.jdroid.android.navdrawer.DefaultNavDrawer
import com.jdroid.android.navdrawer.DefaultNavDrawerItem
import com.jdroid.android.navdrawer.NavDrawer
import com.jdroid.android.navdrawer.NavDrawerHeader
import com.jdroid.android.navdrawer.NavDrawerItem
import com.jdroid.android.sample.R
import com.jdroid.android.sample.ui.home.HomeActivity

class AndroidActivityHelper(activity: AbstractFragmentActivity) : ActivityHelper(activity) {

    override fun isNavDrawerEnabled(): Boolean {
        return true
    }

    override fun createNavDrawer(activity: AbstractFragmentActivity, appBar: Toolbar): NavDrawer {
        return object : DefaultNavDrawer(activity, appBar) {

            override fun createNavDrawerItems(): List<NavDrawerItem> {
                val navDrawerItems = mutableListOf<NavDrawerItem>()
                navDrawerItems.add(DefaultNavDrawerItem(R.id.home, HomeActivity::class.java))
                navDrawerItems.add(DefaultNavDrawerItem(R.id.about, AboutActivity::class.java))
                return navDrawerItems
            }

            override fun initNavDrawerHeader(navDrawerHeader: NavDrawerHeader) {
                super.initNavDrawerHeader(navDrawerHeader)
                navDrawerHeader.setBackground(R.drawable.hero)
            }
        }
    }
}

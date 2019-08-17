package com.jdroid.android.sample.ui.navdrawer

import android.os.Bundle
import android.view.View

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class NavDrawerFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.navdrawer_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.leftCustomNavDrawer).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                LeftCustomNavDrawerActivity::class.java
            )
        }
        findView<View>(R.id.rightCustomNavDrawer).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                RightCustomNavDrawerActivity::class.java
            )
        }
        findView<View>(R.id.userNavDrawer).setOnClickListener { ActivityLauncher.startActivity(activity, UserNavDrawerActivity::class.java) }
        findView<View>(R.id.noNavDrawer).setOnClickListener { ActivityLauncher.startActivity(activity, NoNavDrawerActivity::class.java) }
    }
}

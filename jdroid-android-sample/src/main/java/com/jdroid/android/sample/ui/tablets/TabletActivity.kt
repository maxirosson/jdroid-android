package com.jdroid.android.sample.ui.tablets

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.HorizontalFragmentsContainerActivity

class TabletActivity : HorizontalFragmentsContainerActivity() {

    override fun getLeftFragmentClass(): Class<out Fragment>? {
        return LeftTabletFragment::class.java
    }

    override fun getRightFragmentClass(): Class<out Fragment>? {
        return RightTabletFragment::class.java
    }
}

package com.jdroid.android.sample.ui.navdrawer

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.navdrawer.NavDrawer
import com.jdroid.android.sample.R

class RightSampleNavDrawer(activity: AbstractFragmentActivity, appBar: Toolbar) : NavDrawer(activity, appBar) {

    override fun createContentView(): View {
        return activity.findViewById(R.id.drawer)
    }
}

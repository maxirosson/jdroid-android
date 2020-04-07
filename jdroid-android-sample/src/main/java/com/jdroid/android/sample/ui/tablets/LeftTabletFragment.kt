package com.jdroid.android.sample.ui.tablets

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ScreenUtils
import com.jdroid.android.utils.ToastUtils

class LeftTabletFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.left_tablet_fragment
    }

    override fun getMenuResourceId(): Int? {
        return R.menu.left_tablet_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openDetail = findView<Button>(R.id.openDetail)
        if (ScreenUtils.is10Inches()) {
            openDetail.visibility = View.GONE
        } else {
            openDetail.visibility = View.VISIBLE
            openDetail.setOnClickListener { ActivityLauncher.startActivity(activity, RightTabletActivity::class.java) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.leftAdd -> {
                ToastUtils.showToast(R.string.leftAction)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
